package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista del listado de
 * organizaciones vinculadas
 */
public class FXMLListadoOrganizacionesVinculadasController implements Initializable {

    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private TableView<OrganizacionVinculada> tv_organizacionesVinculadas;
    @FXML
    private TableColumn<?, ?> col_nombreOrganizacion;
    @FXML
    private TableColumn<?, ?> col_telefono;
    @FXML
    private TableColumn<?, ?> col_correo;
    @FXML
    private TableColumn<?, ?> col_tipo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }
    
    //Navegación

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
    
}
