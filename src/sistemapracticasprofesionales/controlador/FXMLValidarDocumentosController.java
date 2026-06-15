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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author macol
 */
public class FXMLValidarDocumentosController implements Initializable {

    @FXML
    private TextField txt_busqueda;
    @FXML
    private ComboBox<?> cb_filtros;
    @FXML
    private TableView<?> tv_entregas;
    @FXML
    private TableColumn<?, ?> col_matricula;
    @FXML
    private TableColumn<?, ?> col_estudiante;
    @FXML
    private TableColumn<?, ?> col_documento;
    @FXML
    private TableColumn<?, ?> col_fechaEntrega;
    @FXML
    private TableColumn<?, ?> col_fechaSubida;
    @FXML
    private TableColumn<?, ?> col_estado;
    @FXML
    private TextField txt_archivo;
    @FXML
    private TextArea ta_comentarios;
    @FXML
    private Label lbl_error;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
    }

    @FXML
    private void clicVerDocumento(ActionEvent event) {
    }

    @FXML
    private void clicValidarDocumento(ActionEvent event) {
    }

    @FXML
    private void clicRechazarDocumento(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
