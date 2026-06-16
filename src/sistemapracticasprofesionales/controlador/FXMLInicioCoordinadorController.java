package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del menu del coordinador.
 */
public class FXMLInicioCoordinadorController implements Initializable {

    @FXML
    private Label lb_nombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void clicRegistrarProyecto(ActionEvent event) {
        cambiarVentana("FXMLRegistroProyecto", 
                "Registro de proyecto");
    }

    @FXML
    private void clicVerListadoProyectos(ActionEvent event) {
        cambiarVentana("FXMLListadoProyectos", 
                "Listado de proyectos");
    }

    @FXML
    private void clicAsignarProyecto(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto", 
                "Asignacion de proyectos");
    }

    @FXML
    private void clicVerAsignaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Listado de asignaciones");
    }

    @FXML
    private void clicRegistrarOrganizacionVinculada(ActionEvent event) {
        cambiarVentana("FXMLRegistroOrganizacionVinculada",
                "Registro de organización vinculada");
    }

    @FXML
    private void clicVerListadoOrganizaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoOrganizacionesVinculadas",
                "Listado de organizaciones vinculadas");
    }
    
    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }
    
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario =
                    (Stage) lb_nombre.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle(titulo);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
