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
 * Descripción: Clase controladora para la vista del menu del profesor.
 */
public class FXMLInicioProfesorController implements Initializable {

    @FXML
    private Label lb_nombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    
    @FXML
    private void clicConsultarExpedientesEstudiantes(ActionEvent event) {
        cambiarVentana("FXMLListaEstudianteProfesor", 
                "Expedientes de estudiantes");
    }

    @FXML
    private void clicSubirFormatos(ActionEvent event) {
        cambiarVentana("FXMLFormatosProfesor", "Subir formatos");
        
    }

    @FXML
    private void clicFechasEntrega(ActionEvent event) {
        cambiarVentana("FXMLAsignarFechaEntregaDocumento", "Fechas de Entrega");
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
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
