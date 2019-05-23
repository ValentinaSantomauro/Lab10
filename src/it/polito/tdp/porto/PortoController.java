package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	Model model;
	public void setModel(Model model) {
		this.model =model;
		boxPrimo.getItems().addAll(model.getAutori());
		boxSecondo.getItems().clear() ;
	}
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    
    
    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	
    	Author autorePrimo  = boxPrimo.getValue();
    	
    	if(autorePrimo == null) {
    		txtResult.appendText("Errore: seleziona un autore!");
    		return;
    	}
    	
    	
    	List<Author> coautori = model.getCoautori(autorePrimo);
    	//IMPORTANTE: RICORDA CHE NEL BOXSECONDO NON PUOI METTERE I COAUTORI DEL BOXPRIMO!! DEVONO ESSERE AUTORI DIVERSI
    	//creo la lista di autori escludendo i coautori e questa lista sarà quella di boxSecondo
    	
    	List<Author> noncoautori = new ArrayList<>(model.getAutori());
    	noncoautori.removeAll(coautori);
    	noncoautori.remove(autorePrimo);
    	
    	for(Author a : coautori) {
    	
    	txtResult.setText(String.format("I coautori di %s sono : %s \n", autorePrimo.toString(), a.toString()));
    	
    	    }
    	
    	//riempi ed abilita la boxSecondo 
    	boxSecondo.getItems().clear();
    	boxSecondo.getItems().addAll(noncoautori);
    	boxSecondo.setDisable(false);
    	
    	
    }
    @FXML
    void handleSequenza(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Author autorePrimo  = boxPrimo.getValue();
        Author autoreSecondo = boxSecondo.getValue();
    	
    	
    	if(autorePrimo==null || autoreSecondo==null) {
    		txtResult.setText("Devi selezionare due autori! \n");
    		return;
    	}
    	else {
    		List <Paper> paperList = model.trovaSequenzaArticoli(autorePrimo, autoreSecondo);
    		for(Paper p : paperList) {
    		
    	txtResult.setText(String.format("La sequenza di autori che collega %s a %s è : %s \n ", autorePrimo, autoreSecondo, p.toString()));
    	}
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
        boxSecondo.setDisable(true);
    }

	
}
