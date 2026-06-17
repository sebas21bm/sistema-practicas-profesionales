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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author macol
 */
public class FXMLAsignarFechaEntregaDocumentoController implements Initializable {

    @FXML
    private ComboBox<?> cb_documentos;
    @FXML
    private DatePicker dp_fechaEntrega;
    @FXML
    private Label lbl_error;
    @FXML
    private TableView<?> tv_fechasEntrega;
    @FXML
    private TableColumn<?, ?> col_tipoDocumento;
    @FXML
    private TableColumn<?, ?> col_valor;
    @FXML
    private TableColumn<?, ?> col_fechaEntrega;
    @FXML
    private TableColumn<?, ?> col_estadoFecha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicAsignarFecha(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
