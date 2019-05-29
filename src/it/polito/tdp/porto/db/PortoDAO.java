package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List <Author> getTuttiAutori( ){
		
		final String sql = "SELECT id, lastname, firstname "+
				 " FROM author "+
				"ORDER BY lastname ASC, firstname ASC ";
		List <Author> result = new ArrayList<Author>();
		Connection conn = DBConnect.getConnection();

		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(autore);
				
				
			}

			conn.close();
			
			
			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
		
	}
	
	public List <Author> getCoautori (Author autoreSelezionato){
		//seleziono le info dell'autore da due tagb creator che contengono due id diversi per autore ma stesso eprint
		//l'id dell'autore 1 deve essere quello passato come parametro e l'id del primo autore è diverso da quello del secondo 
		
		String sql = "SELECT DISTINCT a2.id, a2.lastname, a2.firstname "+
				"FROM creator c1, creator c2, author a2 "+
				"WHERE c1.eprintid = c2.eprintid "+
				"AND c2.authorid = a2.id "
				+ "AND c1.authorid = ? "
				+ "AND a2.id <> c1.authorid "+
				"ORDER BY a2.lastname ASC, a2.firstname ASC ";
		
		Connection conn = DBConnect.getConnection() ;
		
		List<Author> result = new ArrayList<>() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, autoreSelezionato.getId());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				result.add(new Author(res.getInt("id"), res.getString("lastname"), res.getString("firstname"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	


	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public Paper getPaperDiAutori(Integer idA1, Integer idA2) {
		//controllo che abbiano un articolo in comune
		//prendo l'articolo 
		
		String sql = "SELECT paper.eprintid, title, issn, publication, type, types\n" + 
				"				FROM paper, creator c1, creator c2 \n" + 
				"				WHERE paper.eprintid=c1.eprintid \n" + 
				"				AND paper.eprintid=c2.eprintid \n" + 
				"				AND c1.authorid= ?\n" + 
				"				AND c2.authorid=?\n" + 
				"				LIMIT 1";
		Connection conn = DBConnect.getConnection() ;
		
		
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, idA1);
			st.setInt(2, idA2);
			ResultSet res = st.executeQuery() ;
			
			Paper p =null; 
			if(res.next()) {
				//c'è almeno un articolo...ritornalo!
				p = new Paper(res.getInt("eprintid"), res.getString("title"), res.getString("issn"),res.getString("publication"),res.getString("TYPE"),res.getString("types")) ;
			}
			
			conn.close();
			
			return p;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
}