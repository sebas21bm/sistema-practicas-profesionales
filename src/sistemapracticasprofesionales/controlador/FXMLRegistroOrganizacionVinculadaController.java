package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista del registro de
 * organizaciones vinculadas
 */
public class FXMLRegistroOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField txt_nombreOrganizacionVinculada;
    @FXML
    private TextField txt_correoElectronico;
    @FXML
    private ComboBox<?> cb_tipoOrganizacion;
    @FXML
    private TextField txt_calle;
    @FXML
    private TextField txt_colonia;
    @FXML
    private TextField txt_codigoPostal;
    @FXML
    private TextField txt_telefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void clicRegresar(ActionEvent event) {
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
    private void clicRegistrar(ActionEvent event) {
    }
    
}
