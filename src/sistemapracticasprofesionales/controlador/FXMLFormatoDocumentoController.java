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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author macol
 */
public class FXMLFormatoDocumentoController implements Initializable {

    @FXML
    private ComboBox<?> cb_documentos;
    @FXML
    private TextField txt_archivo;
    @FXML
    private Label lbl_error;
    @FXML
    private TableView<?> tv_formatos;
    @FXML
    private TableColumn<?, ?> col_tipoDocumento;
    @FXML
    private TableColumn<?, ?> col_valor;
    @FXML
    private TableColumn<?, ?> col_nombreOriginal;
    @FXML
    private TableColumn<?, ?> col_fechaSubida;
    @FXML
    private TableColumn<?, ?> col_estadoFormato;

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
    private void clicSubirFormato(ActionEvent event) {
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
