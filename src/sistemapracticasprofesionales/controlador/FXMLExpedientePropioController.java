package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del expediente del estudiante.
 */
public class FXMLExpedientePropioController implements Initializable {

    @FXML
    private Label lb_nombreProyecto;
    @FXML
    private Label lb_avanceCalificacion;
    @FXML
    private Button btn_verDetalles;
    @FXML
    private Button btn_regresar;
    @FXML
    private Label lb_porcentajeObtenidoDocumentosIniciales;
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
    private void clicVerDetalles(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
