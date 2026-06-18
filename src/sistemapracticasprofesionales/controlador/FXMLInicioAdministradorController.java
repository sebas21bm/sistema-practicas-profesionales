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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del menu del administrador.
 */
public class FXMLInicioAdministradorController implements Initializable {

    @FXML
    private Label lb_nombre;
    @FXML
    private Button btnRegistrarCoordinador;
    @FXML
    private Button btnListadoCoordinadores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnRegistrarCoordinador.setDisable(true);
        btnListadoCoordinadores.setDisable(true);
        cargarNombreUsuario();
    }    
    
    private void cargarNombreUsuario() {
        if (Sesion.getUsuarioActual() != null
                && Sesion.getUsuarioActual().getNombreReal() != null
                && !Sesion.getUsuarioActual().getNombreReal().trim().isEmpty()) {
            lb_nombre.setText(Sesion.getUsuarioActual().getNombreReal());
        } else {
            lb_nombre.setText("Usuario");
        }
    }

    private void clicRegistrarCoordinador(ActionEvent event) {
        cambiarVentana("FXMLFormularioCoordinador",
                "Registro de coordinador");
    }

    private void clicVerListadoCoordinadores(ActionEvent event) {
        cambiarVentana("FXMLListadoCoordinadores",
                "Listado de coordinadores");
    }

    @FXML
    private void clicRegistrarProfesor(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioProfesor");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Registro de profesor");
            escenarioFormulario.setResizable(false);
            escenarioFormulario.centerOnScreen();
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVerListadoProfesores(ActionEvent event) {
        cambiarVentana("FXMLListadoProfesores",
                "Listado de profesores");
    }

    @FXML
    private void clicRegistrarEstudiante(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioEstudiante");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Registro de estudiante");
            escenarioFormulario.setResizable(false);
            escenarioFormulario.centerOnScreen();
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVerListadoEstudiantes(ActionEvent event) {
        cambiarVentana("FXMLListadoEstudiantes",
                "Listado de estudiantes");
    }

    @FXML
    private void clicCerrarSesión(ActionEvent event) {
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
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
