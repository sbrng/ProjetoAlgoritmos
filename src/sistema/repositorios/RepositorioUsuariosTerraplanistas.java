package sistema.repositorios;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;

import sistema.beans.UsuarioTerraplanista;

public class RepositorioUsuariosTerraplanistas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 132234234L;
	private List<UsuarioTerraplanista> usuarios = new ArrayList<UsuarioTerraplanista>();
	private HashMap<String, String> convites = new HashMap<String, String>();
	private Set<Set<UsuarioTerraplanista>> cliques;
	
	public boolean temConvitePara(UsuarioTerraplanista esseCara) {
		if(convites.keySet().contains(esseCara.getLogin())) {
			return true;
		}
		else {
			return false;
		}
	}
	public HashMap<String, String> getConvites() {
		return convites;
	}
	public void setConvites(HashMap<String, String> convites) {
		this.convites = convites;
	}
	public void enviarConvite(UsuarioTerraplanista desseCara, UsuarioTerraplanista praEsseCara) {
		if(convites.containsKey(praEsseCara.getLogin())) {
			convites.put(praEsseCara.getLogin(), convites.get(praEsseCara.getLogin())+","+desseCara.getLogin());
		}
		else{
			convites.put(praEsseCara.getLogin(), ","+desseCara.getLogin());
		}
		salvarConvites();
	}
	public boolean removerConvite(UsuarioTerraplanista desseCara, UsuarioTerraplanista praEsseCara) {
		try{
			String[] convidados = convites.get(praEsseCara.getLogin()).split(",");
			if(convidados.length>2) {
				convites.put(praEsseCara.getLogin(), convites.get(praEsseCara.getLogin()).replace("," + desseCara.getLogin(), ""));
			}
			else {
				convites.remove(praEsseCara.getLogin());
			}
			salvarConvites();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	public List<UsuarioTerraplanista> convitesPara(UsuarioTerraplanista esseCara){
		List<UsuarioTerraplanista> retorno = new ArrayList<UsuarioTerraplanista>();
		if(convites.containsKey(esseCara.getLogin())) {
			String[] pessoas = convites.get(esseCara.getLogin()).split(",");
			for(String a : pessoas) {
				if(!a.isEmpty()) {
					retorno.add(pesquisarPorLogin(a));
				}
			}
		}
		return retorno;
	}
	public int numeroDeConvitesPara(UsuarioTerraplanista esseCara) {
		try {
			String[] pessoas = convites.get(esseCara.getLogin()).split(",");
			return pessoas.length-1;
		}catch(Exception e) {
			return 0;
		}
	}
	public List<UsuarioTerraplanista> convitesEnviadosPor(UsuarioTerraplanista esseCara){
		List<UsuarioTerraplanista> lista = new ArrayList<UsuarioTerraplanista>();
		try {
			for(String login : convites.keySet()) {
				
				if (convites.get(login).contains(","+esseCara.getLogin())) {
					
					lista.add(pesquisarPorLogin(login));
				}
			
			}
			return lista;
		}catch(Exception e) {
			return new ArrayList<>();
		}
		
	}
	public List<UsuarioTerraplanista> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<UsuarioTerraplanista> usuarios) {
		this.usuarios = usuarios;
	}
	
	public void adicionarUsuario(UsuarioTerraplanista usuario) {
		this.usuarios.add(usuario);
		salvar();
	}
	
	public void	removerUsuario(UsuarioTerraplanista usuario) {
		this.usuarios.remove(usuario);
		salvar();
	}
	
	public void editarUsuario(UsuarioTerraplanista usuarioVelho, UsuarioTerraplanista usuarioNovo) {
		
		usuarioVelho.setAmigos(usuarioNovo.getAmigos());
		usuarioVelho.setDataNascimento(usuarioNovo.getDataNascimento());
		usuarioVelho.setImage(usuarioNovo.getImage());
		usuarioVelho.setInteresses(usuarioNovo.getInteresses());
		usuarioVelho.setLogin(usuarioNovo.getLogin());
		usuarioVelho.setNome(usuarioNovo.getNome());
		usuarioVelho.setRecomendacoes(usuarioNovo.getRecomendacoes());
		usuarioVelho.setSenha(usuarioNovo.getSenha());
		salvar();
	}
	public RepositorioUsuariosTerraplanistas() {
		try {
			carregar();
			carregarConvites();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("deu rim no carregar");
		}
	}
	public List<List<UsuarioTerraplanista>> seitasDe3Pessoas(){
		List<List<UsuarioTerraplanista>> listaDeListas = new ArrayList<>(); //criando uma lista de seitas, ou seja uma lista de lista
		for(UsuarioTerraplanista usuario:usuarios) { //para cada usuario do programa
			for(UsuarioTerraplanista amigo:usuario.getAmigos()) { //para cada amigo desse usuario
				for(UsuarioTerraplanista comum:compararListaAmigos(usuario.getAmigos(), amigo.getAmigos())) { //para cada pessoas em comum na lista dos 2
					List<UsuarioTerraplanista> listaAux = new ArrayList<>(); 
					listaAux.add(comum);
					listaAux.add(usuario);
					listaAux.add(amigo);//bota eu, o amigo e o cara em comum na lista
					Collections.sort(listaAux,
			                 new Comparator<UsuarioTerraplanista>()
	                 {
	                     public int compare(UsuarioTerraplanista f1, UsuarioTerraplanista f2)
	                     {
	                         return f1.getLogin().compareTo(f2.getLogin());
	                     }        
	                 }); //função pra ordenar a lista baseado no tostring
					//System.out.println(listaAux);
					if(!listaDeListas.contains(listaAux)) // se ainda não tem essa seita bota ela na lista
						listaDeListas.add(listaAux);
				}
			}
		}
		//System.out.println(listaDeListas);
		return listaDeListas;
	}
	
	private List<UsuarioTerraplanista> compararListaAmigos(List<UsuarioTerraplanista> lista1, List<UsuarioTerraplanista> lista2) {
		List<UsuarioTerraplanista> listaAux = new ArrayList<>(); 
		for(UsuarioTerraplanista u: lista1) { //para cada cara da lista 1
			for(UsuarioTerraplanista a: lista2) { //para cada cara da lista 2
				if(u.getLogin().contentEquals(a.getLogin())) { 
					listaAux.add(u); // se os 2 tem o mesmo amigo é uma interseção, bota na lista de em comum
				}
			}
		}
		
		return listaAux;
	}
	public void salvar()  {
		
		FileOutputStream fos = null;
		
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream("Repositorios.hnf");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(usuarios);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public void carregar() throws IOException, ClassNotFoundException {

		FileInputStream fis = new FileInputStream("Repositorios.hnf");
		ObjectInputStream ois = new ObjectInputStream(fis);
		this.usuarios = (ArrayList<UsuarioTerraplanista>) ois.readObject();
		ois.close();
	}
	public void salvarConvites()  {
		
		FileOutputStream fos = null;
		
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream("Convites.hnf");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(convites);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public void carregarConvites() throws IOException, ClassNotFoundException {

		FileInputStream fis = new FileInputStream("Convites.hnf");
		ObjectInputStream ois = new ObjectInputStream(fis);
		this.convites =  (HashMap<String, String>) ois.readObject();
		ois.close();
	}
	public ArrayList<UsuarioTerraplanista> pesquisarPorNome(String nome) {
		nome = nome.toUpperCase();
		ArrayList<UsuarioTerraplanista> ret = new ArrayList<UsuarioTerraplanista>();
		if (nome == null)
			return null;
		
		for (UsuarioTerraplanista user : this.usuarios) {
			if (user.getNome().toUpperCase().contains(nome)) {
				ret.add(user);
			}
		}
		return ret;
	}
	
	 public UsuarioTerraplanista pesquisarPorLogin(String login) {
		 	login = login.toUpperCase();
			
			if (login == null)
				return null;

			for (UsuarioTerraplanista user : this.usuarios) {
				if (user.getLogin().toUpperCase().equals(login)) {
					return user;
				}
			}
			return null;
	 }
	 public List<UsuarioTerraplanista> indicacaoPorInteresse(UsuarioTerraplanista user)
		{
			Map<String, UsuarioTerraplanista> mapa = new TreeMap<String, UsuarioTerraplanista>();
			
			if(user.getInteresses() == null) {
				System.out.println("Não há interesses para comparar");
				return null;
			}
			
			for(int i = 0; i<this.usuarios.size(); i++) { //percorrendo a lista de todos os usuários
				
				int temp = 1;
				
				if(!(this.usuarios.get(i).equals(user)) && user.getInteresses() != null && this.usuarios.get(i).getInteresses() != null && !(user.getAmigos().contains(this.usuarios.get(i)))) {
					for(int j = 0; j<this.usuarios.get(i).getInteresses().size(); j++) { //percorrendo a lista de interesses de um determinado usuário
						for(int k = 0; k<user.getInteresses().size(); k++) { //percorrer a lista de interesses do usuário que queremos recomendar
							if(this.usuarios.get(i).getInteresses().get(j).equalsIgnoreCase(user.getInteresses().get(k))){
								temp++;
							}
						}
					}
					
					if(temp > 1) {
						while(!(mapa.containsValue(this.usuarios.get(i)))) {
							if(mapa.containsKey(Integer.toString(temp))) {
								temp = temp * 10;
							}
							else{
								mapa.put(Integer.toString(temp), this.usuarios.get(i));
								temp = 1;
							}
							
						}
					}
				}
			}
			
			List<UsuarioTerraplanista> indicados = new ArrayList<UsuarioTerraplanista>(mapa.values());
			Collections.reverse(indicados);
			for(UsuarioTerraplanista a:user.getAmigos()) {
				if(indicados.contains(a)) {
					indicados.remove(a);
				}
			}
			return indicados;
		}
	 
	 
	 
	 
		public List<UsuarioTerraplanista> indicacaoAmigoComum(UsuarioTerraplanista user)
		{
			if(user.getAmigos() == null) {
				System.out.println("Não há amigos para comparar");
				return null;
			}

			int temp = 1;
			
			Map<String, UsuarioTerraplanista> mapa = new TreeMap<String, UsuarioTerraplanista>();
			
			for(int i = 0; i<this.usuarios.size(); i++) { //percorrendo a lista de todos os usuários
				if(!(this.usuarios.get(i).equals(user)) && !(user.getAmigos().contains(this.usuarios.get(i)))) {
					for(int j = 0; j<this.usuarios.get(i).getAmigos().size(); j++) { //percorrendo a lista de amigos de um determinado usuário
							if(user.getAmigos().contains(this.usuarios.get(i).getAmigos().get(j))) {
								temp++;
							}
						}
					
					if(temp > 1) {
						while(!(mapa.containsValue(this.usuarios.get(i)))) {
							if(mapa.containsKey(Integer.toString(temp))) {
								temp = temp * 10;
							}
							else{
								mapa.put(Integer.toString(temp), this.usuarios.get(i));
								temp = 1;
							}
							
						}
					}
				}
			}	
			
			List<UsuarioTerraplanista> indicados = new ArrayList<UsuarioTerraplanista>(mapa.values());
			Collections.reverse(indicados);
			return indicados;
		}
		public List<UsuarioTerraplanista> recomendacoesPara(UsuarioTerraplanista usuarioTerraplanista){
			
			List<UsuarioTerraplanista> retorno = new ArrayList<UsuarioTerraplanista>();
			List<UsuarioTerraplanista> lista1 = new ArrayList<>();
			HashMap<String, Integer> mapa = new HashMap<>();
			if(usuarioTerraplanista.getAmigos().size()>0) {
				lista1 = indicacaoAmigoComum(usuarioTerraplanista);
				for(int i = 0; i < lista1.size() ; i++) {
					mapa.put(lista1.get(i).getLogin(), 50 - i);
				}
			}
			if(usuarioTerraplanista.getInteresses().size()>0){
				List<UsuarioTerraplanista> lista2 = indicacaoPorInteresse(usuarioTerraplanista);
				for(int i = 0; i < lista2.size() ; i++) {
					
					if(usuarioTerraplanista.getAmigos().size()>0&&mapa.containsKey(lista2.get(i).getLogin())){
						mapa.replace(lista2.get(i).getLogin(), mapa.get(lista2.get(i).getLogin())+50-i);
					}
					else {
						mapa.put(lista2.get(i).getLogin(), 50 - i);
					}
				}
			}
			
			List<String> logins = new ArrayList<>(mapa.keySet());
			List<Integer> valores = new ArrayList<>(mapa.values());
			Collections.sort(valores);
			Collections.reverse(valores);
			//System.out.println(valores);
			//.out.println(logins);
			for(int i = 0; i<valores.size(); i++) {
				for(int j = 0; j < logins.size(); j ++) {
					//System.out.println(mapa.get(logins.get(j)));
					if (mapa.get(logins.get(j))==valores.get(i)) {
						retorno.add(pesquisarPorLogin(logins.get(j)));
						logins.remove(j);
						j-=1;
					}
					
				}
				valores.remove(i);
				i-=1;
			}
			return retorno;
		}
		
		public List<UsuarioTerraplanista> getAmigosComuns(UsuarioTerraplanista user, UsuarioTerraplanista outro) {
			List<UsuarioTerraplanista> amigosComuns = new ArrayList<UsuarioTerraplanista>();
			
			if(user.equals(outro) || user == null || outro == null) {
				return null;
			}
			
			for(int i = 0; i<user.getAmigos().size(); i++) {
				if(outro.getAmigos().contains(user.getAmigos().get(i))) {
					amigosComuns.add(user.getAmigos().get(i));
				}
			}
			
			return amigosComuns;
		}
		
		public List<UsuarioTerraplanista> addSemRepetir(List<UsuarioTerraplanista> lista, List<UsuarioTerraplanista> outra) {
			
			List<UsuarioTerraplanista> list = new ArrayList<UsuarioTerraplanista>(lista);
			
			for(int i = 0; i<outra.size(); i++) {
				if(!(lista.contains(outra.get(i)))){
					if(checkSeTemTodosAmg(lista, outra.get(i)) == true) {
						lista.add(outra.get(i));
					}
				}
			}
			
			return list;
		}
		
		public List<UsuarioTerraplanista> tratarPanelinha(List<UsuarioTerraplanista> panelinha) {
			
			if(panelinha == null) {
				return null;
			}
			
			for(int i = 0; i<panelinha.size(); i++) {
				if(checkSeTemTodosAmg(panelinha, panelinha.get(i)) == false) {
					panelinha.remove(i);
				}
				
			}
			
			return panelinha;
		}
		
		public boolean checkSeTemTodosAmg(List<UsuarioTerraplanista> lista, UsuarioTerraplanista user) {
			
			boolean result = false;
			
			boolean lixo;
			
			List<UsuarioTerraplanista> temp = new ArrayList<UsuarioTerraplanista>(lista);
			
			if(temp.contains(user)) {
				lixo = temp.remove(user);
			}
			
			if(user.getAmigos().containsAll(temp)){
				result = true;
			}
			
			return result;
		}
		
		public List<List<UsuarioTerraplanista>> pessoasParaSeita(){
			
			List<List<UsuarioTerraplanista>> panelinhas = new ArrayList<List<UsuarioTerraplanista>>();
			
			for(int i = 0; i<this.usuarios.size(); i++) {
				
				UsuarioTerraplanista user1 = new UsuarioTerraplanista(this.usuarios.get(i));
				
				List<UsuarioTerraplanista> temp = new ArrayList<UsuarioTerraplanista>();
				
				temp.add(user1);
				
				for(int j = 0; j<user1.getAmigos().size(); j++) {
					UsuarioTerraplanista user2 = new UsuarioTerraplanista(this.usuarios.get(i).getAmigos().get(j));
					
					List<UsuarioTerraplanista> amigosC = new ArrayList<UsuarioTerraplanista>(getAmigosComuns(user1, user2));
					if(amigosC != null) {
						
						for(int k = 0; k < amigosC.size(); k++) {
							List<UsuarioTerraplanista> amgComumUser = new ArrayList<UsuarioTerraplanista>(getAmigosComuns(user1, amigosC.get(k)));
							amgComumUser.addAll(getAmigosComuns(user2, amigosC.get(k)));
							
							temp = addSemRepetir(temp, amgComumUser);
							//temp.addAll(amgComumUser);

						}
					}
				}
				
				//temp = tratarPanelinha(temp);
				panelinhas.add(temp);
			}
			return panelinhas;
		}
		
		public List<UsuarioTerraplanista> possiveisPastores() {
			List<UsuarioTerraplanista> retorno = new ArrayList<>();
			Map<String, Integer> mapa = new HashMap<>();
			for (int i = 0; i < usuarios.size() ; i++) {
				if(!usuarios.get(i).isPastor()) {
					if(mapa.containsKey(usuarios.get(i).getLogin())) {
						mapa.replace(usuarios.get(i).getLogin(), mapa.get(usuarios.get(i).getLogin()) + (usuarios.get(i).getRecomendacoes()));
					}
					else {
						mapa.put(usuarios.get(i).getLogin(), usuarios.get(i).getRecomendacoes());
					}
				}
			}
			
			List<LocalDateTime> listaDeDatas = new ArrayList<> (0);
			for(UsuarioTerraplanista u : usuarios) {
				listaDeDatas.add(u.getHoraCriaçãoConta());
			}
			Collections.sort(listaDeDatas);
			Collections.reverse(listaDeDatas);
			for (int i = 0; i < usuarios.size() ; i++) {
				for(LocalDateTime ldt : listaDeDatas) {
					if(!usuarios.get(i).isPastor() && ldt.isEqual(usuarios.get(i).getHoraCriaçãoConta())) {
						if(mapa.containsKey(usuarios.get(i).getLogin())) {
							mapa.replace(usuarios.get(i).getLogin(), mapa.get(usuarios.get(i).getLogin()) + (usuarios.size() - listaDeDatas.indexOf(ldt)));
						}
						else {
							mapa.put(usuarios.get(i).getLogin(),usuarios.size() - listaDeDatas.indexOf(ldt));
						}
					}
				}												
			}
			for(UsuarioTerraplanista u : usuarios) {
				if(!u.isPastor()) {
					if(mapa.containsKey(u.getLogin())) {
						mapa.replace(u.getLogin(), mapa.get(u.getLogin()) + u.getAmigos().size()/5);
					}
					else {
						mapa.put(u.getLogin(), u.getAmigos().size()/5);
					}
				}
			}
			for(UsuarioTerraplanista u : usuarios) {
				if(!u.isPastor()) {
					if(mapa.containsKey(u.getLogin())) {
						mapa.replace(u.getLogin(), mapa.get(u.getLogin()) + (u.temInteressePor("salamandras")?5:0));
					}
					else {
						mapa.put(u.getLogin(), 5);
					}
				}
			}
			Map<String, Integer> mapaOrdenado = mapa.entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
			for(String str : mapaOrdenado.keySet()) {
				retorno.add(pesquisarPorLogin(str));
			}
			//mapaOrdenado.forEach((key, value) -> System.out.println(value + " - " + key));
			Collections.reverse(retorno);
			return retorno;
		}
		
		public List<List<UsuarioTerraplanista>> maxCliques(){
	        cliques = new HashSet<>();
	        List<UsuarioTerraplanista> potential_clique = new ArrayList<UsuarioTerraplanista>();
	        List<UsuarioTerraplanista> candidates = new ArrayList<UsuarioTerraplanista>();
	        List<UsuarioTerraplanista> already_found = new ArrayList<UsuarioTerraplanista>();
	        candidates.addAll(this.usuarios);
	        findCliques(potential_clique,candidates,already_found);
	        
	        List<List<UsuarioTerraplanista>> clique = new ArrayList<List<UsuarioTerraplanista>>();
	        
	        for(Set<UsuarioTerraplanista> c : cliques) {
	        	if(c.size() >= 3) {
		        	List<UsuarioTerraplanista> temp = new ArrayList<UsuarioTerraplanista>(c);
		        	clique.add(temp);
	        	}
	        }
	        
	        return clique;
	    }
	    
	    private void findCliques(List<UsuarioTerraplanista> potential_clique, List<UsuarioTerraplanista> candidates, List<UsuarioTerraplanista> already_found) {
	    	List<UsuarioTerraplanista> candidates_array = new ArrayList(candidates);
	        if (!end(candidates, already_found)) {
	            // for each candidate_node in candidates do
	            for (UsuarioTerraplanista candidate : candidates_array) {
	                List<UsuarioTerraplanista> new_candidates = new ArrayList<UsuarioTerraplanista>();
	                List<UsuarioTerraplanista> new_already_found = new ArrayList<UsuarioTerraplanista>();

	                // move candidate node to potential_clique
	                potential_clique.add(candidate);
	                candidates.remove(candidate);

	                // create new_candidates by removing nodes in candidates not
	                // connected to candidate node
	                for (UsuarioTerraplanista new_candidate : candidates) {
	                    if (candidate.getAmigos().contains(new_candidate))
	                    {
	                        new_candidates.add(new_candidate);
	                    }
	                }

	                // create new_already_found by removing nodes in already_found
	                // not connected to candidate node
	                for (UsuarioTerraplanista new_found : already_found) {
	                    if (candidate.getAmigos().contains(new_found)) {
	                        new_already_found.add(new_found);
	                    }
	                }

	                // if new_candidates and new_already_found are empty
	                if (new_candidates.isEmpty() && new_already_found.isEmpty()) {
	                    // potential_clique is maximal_clique
	                    cliques.add(new HashSet<UsuarioTerraplanista>(potential_clique));
	                }
	                else {
	                    findCliques(
	                        potential_clique,
	                        new_candidates,
	                        new_already_found);
	                }

	                // move candidate_node from potential_clique to already_found;
	                already_found.add(candidate);
	                potential_clique.remove(candidate);
	            }
	        }
	    }
	    private boolean end(List<UsuarioTerraplanista> candidates, List<UsuarioTerraplanista> already_found)
	    {
	        // if a node in already_found is connected to all nodes in candidates
	        boolean end = false;
	        int edgecounter;
	        for (UsuarioTerraplanista found : already_found) {
	            edgecounter = 0;
	            for (UsuarioTerraplanista candidate : candidates) {
	                if (found.getAmigos().contains(candidate)) {
	                    edgecounter++;
	                }
	            }
	            if (edgecounter == candidates.size()) {
	                end = true;
	            }
	        }
	        return end;
	    }
}
