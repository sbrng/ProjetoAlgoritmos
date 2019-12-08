package sistema.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sistema.beans.UsuarioTerraplanista;
import sistema.repositorios.RepositorioUsuariosTerraplanistas;

public class Controlador {
	private RepositorioUsuariosTerraplanistas repositorioUsuarioTerraPlanistas;
	private UsuarioTerraplanista usuarioLogado;
	public UsuarioTerraplanista getUsuarioLogado() {
		return usuarioLogado;
	}
	public void setUsuarioLogado(UsuarioTerraplanista usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
	public void adicionarUsuario(UsuarioTerraplanista usuario) {
		repositorioUsuarioTerraPlanistas.adicionarUsuario(usuario);
		
	}
	public void removerUsuario(UsuarioTerraplanista usuario) {
		repositorioUsuarioTerraPlanistas.removerUsuario(usuario);
		
	}
	public void editarUsuario(UsuarioTerraplanista usuarioVelho, UsuarioTerraplanista usuarioNovo) {
		repositorioUsuarioTerraPlanistas.editarUsuario(usuarioVelho, usuarioNovo);
		
	}
	public List<UsuarioTerraplanista> getUsuarios() {
		return repositorioUsuarioTerraPlanistas.getUsuarios();
	}
	public void salvar() {
		repositorioUsuarioTerraPlanistas.salvar();
	}
	public void carregar() throws IOException, ClassNotFoundException {
		repositorioUsuarioTerraPlanistas.carregar();
	}
	public ArrayList<UsuarioTerraplanista> pesquisarPorNome(String nome) {
		return repositorioUsuarioTerraPlanistas.pesquisarPorNome(nome);
	}
	public UsuarioTerraplanista pesquisarPorLogin(String login) {
		return repositorioUsuarioTerraPlanistas.pesquisarPorLogin(login);
	}
	static private Controlador instancia = null;
	static public Controlador getInstancia() {
		if (instancia == null) {
			instancia = new Controlador();
		}
		return instancia;
	}
	private Controlador () {
		repositorioUsuarioTerraPlanistas = new RepositorioUsuariosTerraplanistas();
	}
	
}
