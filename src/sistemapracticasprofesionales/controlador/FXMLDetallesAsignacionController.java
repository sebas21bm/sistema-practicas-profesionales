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
 * Descripción: Clase controladora para la vista de los detalles de una 
 * asignación.
 */
public class FXMLDetallesAsignacionController implements Initializable {

    @FXML
    private Label lb_nombreProyecto;
    @FXML
    private Label lb_descripcionProyecto;
    @FXML
    private Label lb_organizacionVinculada;
    @FXML
    private Label lb_nombreResponsable;
    @FXML
    private Label lb_correoResponsable;
    @FXML
    private Label lb_nombreEstudiante;
    @FXML
    private Label lb_matricula;
    @FXML
    private Label lb_correoEstudiante;
    @FXML
    private Label lb_fechaFinalizacion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicCerrar(ActionEvent event) {
    }
    
}
