package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del listado de proyectos.
 */
public class FXMLListadoProyectosController implements Initializable {

    @FXML
    private TableView<ProyectoPracticas> tv_proyectos;
    @FXML
    private TableColumn<?, ?> col_nombre;
    @FXML
    private TableColumn<?, ?> col_estudiante;
    @FXML
    private TableColumn<?, ?> col_organizacionVinculada;
    @FXML
    private TableColumn<?, ?> col_fechaFinalizacion;
    @FXML
    private TextField txt_nombreBusqueda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clilcRegresar(ActionEvent event) {
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
    }

    @FXML
    private void clilcGenerarReporteIndicadores(ActionEvent event) {
    }

    @FXML
    private void clilcRegistrarProyecto(ActionEvent event) {
    }

    @FXML
    private void clicVerListadoProyecto(ActionEvent event) {
    }

    @FXML
    private void clilcAsignarProyecto(ActionEvent event) {
    }

    @FXML
    private void clilcGenerarOficioDeAsignacion(ActionEvent event) {
    }

    @FXML
    private void clilcRegistrarOrganizacionVinculada(ActionEvent event) {
    }

    @FXML
    private void clilcVerListadoOrganizacionesVinculadas(ActionEvent event) {
    }

    @FXML
    private void clilcVerListadoEstudiantes(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }
    
}
