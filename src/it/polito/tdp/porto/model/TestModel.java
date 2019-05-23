package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo();
		Author a1 = new Author (85, "Belforte", "Gustavo" );
		Author a2 = new Author (278, "Codegone", "Marco" );
		
		System.out.println(model.trovaSequenzaArticoli(a1, a2));
		
	}

}
