package it.polito.tdp.flight;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller del turno A --> modificare per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtDistanzaInput;

    @FXML
    private TextField txtPasseggeriInput;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	try {
    		
    		double chilometri;
    		try {
    			
    			chilometri = Double.parseDouble(this.txtDistanzaInput.getText());
    			this.model.creaGrafo(chilometri);
    			this.txtResult.appendText("Grafo creato!\n# vertici: "+this.model.getNvertici()+"\n# archi: "+this.model.getNarchi());
    			this.txtResult.appendText("\n"+this.model.cercaRaggiungibilita());
    			
    		}catch(NumberFormatException nfe) {
    			
    			this.txtResult.appendText("ERRORE! Inserire solo valori numerici (es. 800, 600, ...)");
    			return;
    		}
    		
    	}catch(Exception e) {
    		
    		this.txtResult.appendText("ERRORE nella creazione del grafo!");
    		e.printStackTrace();
			return;
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	this.txtResult.clear();
    	try {
    		
    		int passeggeri;
    		
			try {

				passeggeri = Integer.parseInt(this.txtPasseggeriInput.getText());
				this.txtResult.appendText(this.model.simulazione(passeggeri));

			} catch (NumberFormatException nfe) {

				this.txtResult.appendText("ERRORE! Inserire solo valori numerici (es. 800, 600, ...)");
				return;
			}
    		
    	}catch(Exception e) {
    		this.txtResult.appendText("ERRORE nella simulazione!");
    		e.printStackTrace();
			return;
    	}
    }

    @FXML
    void initialize() {
        assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Flight.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
