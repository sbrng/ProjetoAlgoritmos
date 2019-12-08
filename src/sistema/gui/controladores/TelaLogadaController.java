
package sistema.gui.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sistema.beans.Grafo;
import sistema.beans.UsuarioTerraplanista;
import sistema.controlador.Controlador;
import sistema.gui.TerraPlanizer;

public class TelaLogadaController {
	
	Controlador controlador = Controlador.getInstancia();
	UsuarioTerraplanista contaLogada;
	List <String> listaInteresses = controlador.getUsuarioLogado().getInteresses();
    @FXML
    private Pane painel;

		@FXML
	    private JFXButton bnt_chat;

	    @FXML
	    private JFXButton bnt_amigos;

	    @FXML
	    private JFXButton bnt_doaçao;

	    @FXML
	    private JFXButton bnt_perfil;

	    @FXML
	    private JFXButton bnt_contrato;

	    @FXML
	    private Pane pane_doaçao;

	    @FXML
	    private JFXButton bnt_minimizar;

	    @FXML
	    private JFXButton bnt_sair;

	    @FXML
	    private Pane pane_chat;

	    @FXML
	    private JFXButton bnt_minimizar2;

	    @FXML
	    private JFXButton bnt_sair2;

	    @FXML
	    private Pane pane_amigos;

	    @FXML
	    private JFXListView<?> llv_amigos;

	    @FXML
	    private JFXTextField tf_pesquisa;

	    @FXML
	    private JFXListView<String> lv_pesquisa;

	    @FXML
	    private FontAwesomeIconView icon_lupa;

	    @FXML
	    private JFXButton bnt_remover;

	    @FXML
	    private JFXButton bnt_add;

	    @FXML
	    private Pane pane_contrato;

	    @FXML
	    private Pane pane_perfil;

	    @FXML
	    private Circle circleFoto;

	    @FXML
	    private Label label_name;

	    @FXML
	    private Label label_login;

	    @FXML
	    private Label lavel_data;

	    @FXML
	    private JFXListView<String> lvInteresses;

	    @FXML
	    private FontAwesomeIconView removeInteresses;

	    @FXML
	    private FontAwesomeIconView addInteresses;

	    @FXML
	    private ComboBox<String> comboboxInteresses;

	    @FXML
	    private JFXTextField tfOutro;

	    @FXML
	    private JFXButton bnt_editar;

	    @FXML
	    private Label label_senha;
    
	    
	public void setarContaLogada(UsuarioTerraplanista contaLogada) {
		this.contaLogada = contaLogada;
	}
	
    @FXML
    void sair(ActionEvent e) {
    	TerraPlanizer.trocarTela("login");
    	controlador.setUsuarioLogado(null);
    }
    
    @FXML
    void minimizar(ActionEvent e) {
    	TerraPlanizer.minimizarTela();
    }
    
    @FXML
    void telaFuncional(ActionEvent e) {
    	if(e.getSource() == bnt_chat) {
			pane_chat.toFront();
		}
		else if(e.getSource() == bnt_amigos) {
			pane_amigos.toFront();
		}
		else if(e.getSource() == bnt_contrato) {
			pane_contrato.toFront();
		}
		else if(e.getSource() == bnt_perfil) {
			pane_perfil.toFront();
		}
		else if(e.getSource() == bnt_doaçao) {
			pane_doaçao.toFront();
		}
    }
    
    @FXML
    void initialize() {
    	pane_perfil.toFront();
    	inicializaComboBoxInteresses();
    	label_name.setText(controlador.getUsuarioLogado().getNome());
    	label_login.setText(controlador.getUsuarioLogado().getLogin());
    	label_senha.setText(controlador.getUsuarioLogado().getSenha());
    	lavel_data.setText(controlador.getUsuarioLogado().getDataNascimento().toString());
    	if(controlador.getUsuarioLogado().getImage()==null) {
        	circleFoto.setFill(new ImagePattern(new Image("/images/user.png")));
    	}
    	else {
    		circleFoto.setFill(new ImagePattern(new Image(controlador.getUsuarioLogado().getImage())));
    	}
    	lvInteresses.setItems(FXCollections.observableList(controlador.getUsuarioLogado().getInteresses()));
    	inicializaPane();
    }

	private void inicializaPane() {
		Grafo g1= Grafo.getInstancia();
		Graph g = g1.getGrafo();
		FxViewer v = new FxViewer(g, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		g1.construirgrafo(true);
		g.setAttribute("ui.antialias");
		g.setAttribute("ui.quality");
		g.setAttribute("ui.stylesheet", "graph {fill-color: #DDD; padding: 60px;}");
		v.enableAutoLayout();
		FxViewPanel panel = (FxViewPanel)v.addDefaultView(false, new FxGraphRenderer());
		
		//GIULIA AQUI
		panel.setPrefHeight(200);
		panel.setPrefWidth(200);
		
		
		Pane newLoadedPane =  panel;
		painel.getChildren().add(newLoadedPane);	
			
	}
    @FXML
    void abrirGrafoGrande(MouseEvent event) {
    	
    	TerraPlanizer.abrirGrafo();
    }
	private void inicializaComboBoxInteresses() {
		addInteresses.setDisable(true);
		tfOutro.setDisable(true);
		List<String> lista = new ArrayList<String>();
    	lista.add("Domo terrestre");
    	lista.add("Densidade");
    	lista.add("Tratado da Antártida");
    	lista.add("Resfriamento global");
    	lista.add("FlatCon");
    	lista.add("Geocentrismo");
    	lista.add("Salamandras");
    	Collections.sort(lista);
    	lista.add("Outro...");
    	comboboxInteresses.setItems(FXCollections.observableList(lista));		
	}
	
	@FXML
    void addInteresse(ActionEvent event) {
    	if(comboboxInteresses.getSelectionModel().getSelectedItem().contentEquals("Outro...")) {
    		addInteresses.setDisable(false);
    		tfOutro.setDisable(false);
    	}
    	else if (!verificaSeJaTem(comboboxInteresses.getSelectionModel().getSelectedItem())) {
    		listaInteresses.add(comboboxInteresses.getSelectionModel().getSelectedItem());
    		atualizarListaInteresses();
    		addInteresses.setDisable(true);
    		tfOutro.setDisable(true);
    	}
    	
    }
	private boolean verificaSeJaTem(String usuario) {
    	for (String aux : listaInteresses) {
    		if (aux.toUpperCase().contentEquals(usuario.toUpperCase())) {
    			return true;
    		}
    	}
		return false;
	}
	@FXML
    void addOutroInteresse() {
    	if (!verificaSeJaTem(tfOutro.getText())&&!tfOutro.getText().isEmpty()) {
    		listaInteresses.add(tfOutro.getText());
    		atualizarListaInteresses();
    		tfOutro.setText("");
    	}
    }
    
    void atualizarListaInteresses() {
    	Collections.sort(listaInteresses);
    	lvInteresses.setItems(FXCollections.observableList(listaInteresses));
    }
    @FXML
    void removerInteresse(MouseEvent event) {
    	if(lvInteresses.getSelectionModel().getSelectedItem()!=null) {
    		listaInteresses.remove(lvInteresses.getSelectionModel().getSelectedItem());
        	atualizarListaInteresses();
    	}
    	else {
    		//Avisar em alguma label que tem q selecionar
    	}
    }
    
    
}
