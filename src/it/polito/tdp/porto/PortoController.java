package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	Model model;
	public void setModel(Model model) {
		this.model =model;
		boxPrimo.getItems().addAll(model.getAutori());
		
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
    	boxSecondo.getItems().addAll(model.getAutori());
    	
    	if(boxPrimo.getValue().equals(boxSecondo.getValue())) {
    		txtResult.setText("Non puoi selezionare lo stesso autore! \n");
    	}
    	Author autoreSelezionato = boxPrimo.getValue();
    	model.creaGrafo();
    	
    	txtResult.setText(String.format("I coautori di %s sono : %s \n", autoreSelezionato.toString(), model.getCoautori(autoreSelezionato)));
    	
    	
    	
    	

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	
}
