package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista de información de los dcumentos.
 */
public class FXMLInfoDocumentoController implements Initializable {

    @FXML
    private Label lb_nombreDocumento;
    @FXML
    private Label lb_estadoTitulo;
    @FXML
    private Label lb_calificacionTitulo;
    @FXML
    private Label lb_comentariosTitulo;
    @FXML
    private Label lb_calificacion;
    @FXML
    private Label lb_estado;
    @FXML
    private Label lb_comentarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicConsultarExpediente(ActionEvent event) {
    }

    @FXML
    private void clicDescargarFormatos(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicVerDocumento(ActionEvent event) {
    }

    @FXML
    private void clicSubirDocumento(ActionEvent event) {
    }
    
}
