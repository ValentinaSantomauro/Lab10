package it.polito.tdp.porto.db;

import it.polito.tdp.porto.model.Author;

public class TestPortoDAO {
	
	public static void main(String args[]) {
		PortoDAO pd = new PortoDAO();
		System.out.println(pd.getAutore(85));
		Author a = new Author (85, "Belforte", "Gustavo" );
		System.out.println(pd.getCoautori(a));
	//	System.out.println(pd.getArticolo(2293546));
	//	System.out.println(pd.getArticolo(1941144));

	}

}
