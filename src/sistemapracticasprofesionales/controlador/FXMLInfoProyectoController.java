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
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLInfoProyectoController implements Initializable {

    @FXML
    private Label lb_nombreProyecto;
    @FXML
    private Label lb_nombreEncargado;
    @FXML
    private Label lb_telefonoEncargado;
    @FXML
    private Label lb_cuposRestantes;
    @FXML
    private Label lb_fechaFinalizacion;
    @FXML
    private Label lb_descripcionProyecto;
    @FXML
    private Label lb_nombreOrganizacionVinculada;
    @FXML
    private Label lb_ubicacion;
    @FXML
    private Label lb_tipoOrganizacion;
    @FXML
    private Label lb_telefonoOrganizacion;
    @FXML
    private Label lb_nombreOrganizacionVinculada1;
    @FXML
    private TableView<Estudiante> tv_estudiantesAsignados;
    @FXML
    private TableColumn<?, ?> col_nombreCompletoEstudiante;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private void clicActualizar(ActionEvent event) {
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clilcGenerarReporteIndicadores(ActionEvent event) {
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
    
}
