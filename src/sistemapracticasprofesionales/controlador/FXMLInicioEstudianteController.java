package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sistemapracticasprofesionales.modelo.pojo.Sesion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del menu del estudiante.
 */
public class FXMLInicioEstudianteController implements Initializable {

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
    private void clicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clicConsultarExpediente(ActionEvent event) {
    }

    @FXML
    private void clicDescargarFormatos(ActionEvent event) {
    }
    
}
