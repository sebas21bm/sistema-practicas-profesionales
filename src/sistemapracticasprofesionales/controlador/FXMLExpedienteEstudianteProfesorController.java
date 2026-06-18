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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author macol
 */
public class FXMLExpedienteEstudianteProfesorController implements Initializable {

    @FXML
    private Label lbl_nombreEstudiante;
    @FXML
    private Label lbl_calificacionTotal;
    @FXML
    private Label lbl_porcentajeDocumentosIniciales;
    @FXML
    private TableView<?> tv_documentosIniciales;
    @FXML
    private TableColumn<?, ?> col_documentoInicial;
    @FXML
    private TableColumn<?, ?> col_estadoDocumentoInicial;
    @FXML
    private TableView<?> tv_reportesEvaluaciones;
    @FXML
    private TableColumn<?, ?> col_documento;
    @FXML
    private TableColumn<?, ?> col_estado;
    @FXML
    private TableColumn<?, ?> col_valor;
    @FXML
    private TableColumn<?, ?> col_porcentajeObtenido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicCalcularCalificacion(ActionEvent event) {
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicConsultarExpedientes(ActionEvent event) {
    }

    @FXML
    private void clicSubirFormatos(ActionEvent event) {
    }

    @FXML
    private void clicFechasEntrega(ActionEvent event) {
    }
    
}
