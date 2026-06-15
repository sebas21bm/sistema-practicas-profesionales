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
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador para la ventana de informacion de la organización
 * vinculada.
 */
public class FXMLInfoOrganizacionVinculadaController implements Initializable {

    @FXML
    private Label lb_nombreOrganización;
    @FXML
    private Label lb_estado;
    @FXML
    private Label lb_ubicacion;
    @FXML
    private Label lb_telefono;
    @FXML
    private Label lb_correo;
    @FXML
    private Label lb_tipo;
    @FXML
    private TableView<?> tv_proyectos;
    @FXML
    private TableColumn<?, ?> col_nombreProyecto;
    @FXML
    private TableView<?> tv_responsablesProyecto;
    @FXML
    private TableColumn<?, ?> col_nombreCompletoEstudiante;
    @FXML
    private TableColumn<?, ?> col_correo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicGenerarReporteIndicadores(ActionEvent event) {
    }

    @FXML
    private void clicRegistrarProyecto(ActionEvent event) {
    }

    @FXML
    private void clicVerListadoProyecto(ActionEvent event) {
    }

    @FXML
    private void clicAsignarProyecto(ActionEvent event) {
    }

    @FXML
    private void clicGenerarOficioDeAsignacion(ActionEvent event) {
    }

    @FXML
    private void clicRegistrarOrganizacionVinculada(ActionEvent event) {
    }

    @FXML
    private void clicVerListadoOrganizacionesVinculadas(ActionEvent event) {
    }

    @FXML
    private void clicVerListadoEstudiantes(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clicVerDetallesProyecto(ActionEvent event) {
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }
    
}
