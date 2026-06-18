package sistemapracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.servicio.AsignacionServicio;
import sistemapracticasprofesionales.servicio.OficioAsignacionServicio;
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
        if (asignacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Asignación no encontrada",
                    "No se recibió la información de la asignación.",
                    Alert.AlertType.WARNING);
            return;
        }

        generarOficioAsignacion();
    }
    
    private void generarOficioAsignacion() {
        try {
            Asignacion detallesAsignacion =
                    AsignacionServicio.recuperarDetallesAsignacion(
                            asignacion.getNumProyecto(),
                            asignacion.getIdEstudiante());

            if (detallesAsignacion == null) {
                mostrarErrorRecuperarInformacion();
                return;
            }

            File archivoDestino =
                    seleccionarUbicacionOficio(detallesAsignacion);

            if (archivoDestino == null) {
                return;
            }

            OficioAsignacionServicio.generarOficioAsignacion(
                    detallesAsignacion, archivoDestino);

            Utilidades.mostrarAlertaSimple(
                    "Oficio descargado",
                    "El oficio de asignación se ha descargado "
                    + "en tu computadora.",
                    Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            mostrarErrorRecuperarInformacion();
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al generar oficio",
                    "No se pudo crear el oficio de asignación. "
                    + "Ocurrió un problema al llenar el formato. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al descargar oficio",
                    "No se logró descargar el oficio de asignación. "
                    + "Ocurrió un problema con la descarga.",
                    Alert.AlertType.ERROR);
        }
    }

    private File seleccionarUbicacionOficio(Asignacion asignacion) {
        FileChooser selectorArchivo = new FileChooser();
        selectorArchivo.setTitle("Guardar oficio de asignación");
        selectorArchivo.setInitialFileName(
                crearNombreArchivoSugerido(asignacion));
        selectorArchivo.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        return selectorArchivo.showSaveDialog(
                lb_nombreProyecto.getScene().getWindow());
    }

    private String crearNombreArchivoSugerido(Asignacion asignacion) {
        String matricula = asignacion.getMatricula();

        if (matricula == null || matricula.trim().isEmpty()) {
            matricula = "estudiante";
        }

        return "oficio_asignacion_" + matricula + ".pdf";
    }

    private void mostrarErrorRecuperarInformacion() {
        Utilidades.mostrarAlertaSimple(
                "Error al recuperar información",
                "No se pudo recuperar toda la información de los "
                + "elementos que contiene el oficio de asignación. "
                + "Hubo un problema con sus registros. "
                + "Intente nuevamente.",
                Alert.AlertType.ERROR);
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