package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	
	
	//crea grafo semplice, non orientato, non pesato
	
	private Graph <Author, DefaultEdge> grafo;
	PortoDAO dao = new PortoDAO();
	
	
	public void creaGrafo() {
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
		
	}
	
	
	public List <Author> getCoautori (Author autoreSelezionato)
	{
		return dao.getCoautori(autoreSelezionato);
	}
	//ES 2: OBIETTIVO -> trova lista di articoli che collegano i due autori
	
	//trova camm minimo sul grafo, per ogni coppia di vertici adiacenti dal database vedi se c'è almeno un articolo  che abbia questi autori --> cioè tra i vicini di a1 ci deve essere a2
	
	public List<Paper> trovaSequenzaArticoli(Author a1, Author a2){
		
		List <Paper> listaPaper = new ArrayList<>();
		List <DefaultEdge> camminoMin = this.trovaCamminoMinimo(a1, a2);
		
		for(DefaultEdge de : camminoMin) {
				//cioè sono adiacenti--> sono coautori di uno stesso articolo --> prendi l'articolo che li unisce
			
			Author aut1 = grafo.getEdgeSource(de);
			Author aut2 = grafo.getEdgeTarget(de);
			
				listaPaper=dao.getPaperDiAutori(aut1.getId(),aut2.getId());
				
		}
		
		
		return listaPaper;
		
	}
	
	
	public List <DefaultEdge> trovaCamminoMinimo (Author a1, Author a2){
		DijkstraShortestPath <Author, DefaultEdge> dijkstra = new DijkstraShortestPath<>(this.grafo);
		GraphPath <Author, DefaultEdge> path = dijkstra.getPath(a1, a2);
		
		return path.getEdgeList(); //perchè solo gli archi sono restituiti in ordine, i vertici no
		
	}
	
	public List <Author> getAutori(){
		
		return dao.getTuttiAutori();
	}
	
	
	

}
