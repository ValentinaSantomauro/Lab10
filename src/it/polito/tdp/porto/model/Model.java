package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	
	
	//crea grafo semplice, non orientato, non pesato
	
	private Graph <Author, DefaultEdge> grafo;
	
	List <Author> autori;
	
	
	
	public void creaGrafo() {
		PortoDAO dao = new PortoDAO();
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		//i vertici sono gli autori --> metodo nel dao per avere la lista di tutti gli autori
		
		List <Author>vertex = dao.getTuttiAutori(); 
		Graphs.addAllVertices(grafo, vertex);
		
		//identifica tutti i coautori dell'autoreSelezionato --> gli archi ci sono solo per unire i coautori
		
		for(Author a : grafo.vertexSet()) {
			
			List <Author> coautori = dao.getCoautori(a);
			
			for(Author a2: coautori) {
				if(this.grafo.containsVertex(a2) && this.grafo.containsVertex(a))
					this.grafo.addEdge(a, a2) ;
			}
			
		}
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
		
	}
	
	
	//ES 2: OBIETTIVO -> trova lista di articoli che collegano i due autori
	
	//trova camm minimo sul grafo, per ogni coppia di vertici adiacenti dal database vedi se c'è almeno un articolo  che abbia questi autori --> cioè tra i vicini di a1 ci deve essere a2
	
	public List<Paper> trovaSequenzaArticoli(Author a1, Author a2){
		PortoDAO dao = new PortoDAO();
		List <Paper> listaPaper = new ArrayList<>();
		List <DefaultEdge> camminoMin = this.trovaCamminoMinimo(a1, a2);
		
		for(DefaultEdge de : camminoMin) {
				//cioè sono adiacenti--> sono coautori di uno stesso articolo --> prendi l'articolo che li unisce
			
			Author aut1 = grafo.getEdgeSource(de);
			Author aut2 = grafo.getEdgeTarget(de);
			
				Paper p =dao.getPaperDiAutori(aut1.getId(),aut2.getId());
				if(p==null)
					throw new InternalError("Paper not found...");
				listaPaper.add(p);
		}
		
		
		return listaPaper;
		
	}
	
	
	public List <DefaultEdge> trovaCamminoMinimo (Author a1, Author a2){
		ShortestPathAlgorithm<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<Author, DefaultEdge>(this.grafo);
		GraphPath <Author, DefaultEdge> path =dijkstra.getPath(a1, a2);
		List <DefaultEdge> edges= new ArrayList<DefaultEdge>();
				edges=path.getEdgeList();
		return edges ; //perchè solo gli archi sono restituiti in ordine, i vertici no
		
	}
	
	public List <Author> getAutori(){
		
		if(this.autori==null) {
			PortoDAO dao = new PortoDAO() ;
			this.autori = dao.getTuttiAutori() ;
			if(this.autori==null) 
				throw new RuntimeException("Errore con il database") ;
		}
		
		return this.autori ;
	}
	

	public List <Author> getCoautori (Author a)
	{
		if(this.grafo==null)
			creaGrafo() ;
		
		List<Author> coautori = Graphs.neighborListOf(this.grafo, a) ;
		return coautori ;
	}
	
	
	

}
