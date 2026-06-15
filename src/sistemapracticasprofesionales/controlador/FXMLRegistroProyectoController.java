package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del registro de proyecto.
 */
public class FXMLRegistroProyectoController implements Initializable {

    @FXML
    private TextField txt_nombreProyecto;
    @FXML
    private TextField txt_descripcion;
    @FXML
    private DatePicker dp_fechaFinalizacion;
    @FXML
    private TextField txt_cupo;
    @FXML
    private ComboBox<?> cb_organizacionVinculada;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_apellidoPaterno;
    @FXML
    private TextField txt_apellidoMaterno;
    @FXML
    private TextField txt_telefono;
    @FXML
    private TextField txt_correo;
    @FXML
    private TextField txt_puesto;
    @FXML
    private RadioButton rbtn_nuevoResponsable;
    @FXML
    private ToggleGroup tg_seleccionResponsable;
    @FXML
    private RadioButton rbtn_responsableExistente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
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

    
}
