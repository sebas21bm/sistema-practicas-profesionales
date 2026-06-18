package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
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
    
    private Asignacion asignacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void inicializarInformacion(Asignacion asignacion) {
        this.asignacion = asignacion;
        cargarInformacionAsignacion();
    }
    
    private void cargarInformacionAsignacion() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        lb_nombreProyecto.setText(asignacion.getNombreProyecto());
        lb_descripcionProyecto.setText(asignacion.getDescripcionProyecto());
        lb_organizacionVinculada.setText(
                asignacion.getOrganizacionVinculada());
        lb_nombreResponsable.setText(asignacion.getNombreResponsable());
        lb_correoResponsable.setText(asignacion.getCorreoResponsable());
        lb_nombreEstudiante.setText(asignacion.getNombreEstudiante());
        lb_matricula.setText(asignacion.getMatricula());
        lb_correoEstudiante.setText(asignacion.getCorreoEstudiante());
        
        if (asignacion.getFechaFinalizacion() != null) {
            lb_fechaFinalizacion.setText(formatoFecha.format(
                    asignacion.getFechaFinalizacion()));
        } else {
            lb_fechaFinalizacion.setText("");
        }
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto", "Asignacion de proyectos");
    }

    @FXML
    private void clicGenerarOficio(ActionEvent event) {
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) lb_nombreProyecto.getScene()
                    .getWindow();
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