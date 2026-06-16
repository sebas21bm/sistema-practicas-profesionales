package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 16/06/2026
 * Descripción: Clase controladora para la vista del listado de asignaciones.
 */
public class FXMLListadoAsignacionesController implements Initializable {

    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private ComboBox<?> cb_periodoEscolar;
    @FXML
    private TableView<?> tv_asignaciones;
    @FXML
    private TableColumn<?, ?> col_estudiante;
    @FXML
    private TableColumn<?, ?> col_proyecto;

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
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicGenerarOficio(ActionEvent event) {
    }

    @FXML
    private void clicCancelarAsignacion(ActionEvent event) {
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }
    
}
