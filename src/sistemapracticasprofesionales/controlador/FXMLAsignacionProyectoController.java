package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del la asignación de proyectos.
 */
public class FXMLAsignacionProyectoController implements Initializable {

    @FXML
    private TableView<?> tv_estudiantesSinAsignacion;
    @FXML
    private TableColumn<?, ?> col_matricula;
    @FXML
    private TableColumn<?, ?> col_nombre;
    @FXML
    private TableColumn<?, ?> col_paterno;
    @FXML
    private TableColumn<?, ?> col_materno;
    @FXML
    private TableView<?> tv_proyectosDisponibles;
    @FXML
    private TableColumn<?, ?> col_proyecto;
    @FXML
    private TableColumn<?, ?> col_cupos;

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
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
    }
    
}
