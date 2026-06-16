package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del menu del estudiante.
 */
public class FXMLMenuEstudianteController implements Initializable {

    @FXML
    private Label lb_nombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
