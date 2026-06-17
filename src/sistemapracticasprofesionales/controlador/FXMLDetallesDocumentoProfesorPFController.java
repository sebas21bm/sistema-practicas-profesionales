/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author macol
 */
public class FXMLDetallesDocumentoProfesorPFController implements Initializable {

    @FXML
    private Label lbl_documento;
    @FXML
    private VBox vb_calificacion;
    @FXML
    private TextField txt_calificacion;
    @FXML
    private ComboBox<?> cb_documentos;
    @FXML
    private TextField txt_archivo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
    }

    @FXML
    private void clicGuardarCambios(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
