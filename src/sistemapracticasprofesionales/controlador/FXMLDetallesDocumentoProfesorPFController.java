package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para subir evaluaciones realizadas
 *              por el profesor.
 */
public class FXMLDetallesDocumentoProfesorPFController
        implements Initializable {

    @FXML
    private Label lbl_documento;
    @FXML
    private VBox vb_calificacion;
    @FXML
    private TextField txt_calificacion;
    @FXML
    private ComboBox<DetalleEvaluacion> cb_documentos;
    @FXML
    private TextField txt_archivo;

    private DetalleEvaluacion detalleEvaluacion;
    private ExpedienteEstudiante expedienteEstudiante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_archivo.setEditable(false);
    }

    public void inicializarInformacion(
            DetalleEvaluacion detalleEvaluacion,
            ExpedienteEstudiante expedienteEstudiante) {
        if (detalleEvaluacion == null
                || detalleEvaluacion.getIdDetallesEvaluacion() <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "No se recibió una evaluación válida.",
                    Alert.AlertType.WARNING);
            return;
        }

        this.detalleEvaluacion = detalleEvaluacion;
        this.expedienteEstudiante = expedienteEstudiante;

        mostrarInformacionEvaluacion();
    }

    private void mostrarInformacionEvaluacion() {
        lbl_documento.setText(
                " " + detalleEvaluacion.getDocumentoEntrega());
        txt_calificacion.setText(
                detalleEvaluacion.getCalificacionTexto());
        txt_archivo.setText(
                obtenerTextoSeguro(
                        detalleEvaluacion.getNombreOriginal()));

        cb_documentos.setItems(
                FXCollections.observableArrayList(detalleEvaluacion));
        cb_documentos.getSelectionModel().select(detalleEvaluacion);
        cb_documentos.setDisable(true);
    }

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
        Utilidades.mostrarAlertaSimple(
                "Función pendiente",
                "La selección de archivo se implementará en el CU-28.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clicGuardarCambios(ActionEvent event) {
        Utilidades.mostrarAlertaSimple(
                "Función pendiente",
                "La carga de evaluación del profesor se implementará "
                + "en el CU-28.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        regresarAExpediente();
    }

    private void regresarAExpediente() {
        if (expedienteEstudiante == null) {
            cambiarVentana("FXMLListaEstudianteProfesor",
                    "Estudiantes");
            return;
        }

        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML(
                            "FXMLExpedienteEstudianteProfesor");
            Parent vista = cargador.load();

            FXMLExpedienteEstudianteProfesorController controlador =
                    cargador.getController();
            controlador.inicializarInformacion(expedienteEstudiante);

            mostrarVista(vista, "Expediente estudiante");
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible regresar al expediente.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicConsultarExpedientes(ActionEvent event) {
        cambiarVentana("FXMLListaEstudianteProfesor", "Estudiantes");
    }

    @FXML
    private void clicSubirFormatos(ActionEvent event) {
        cambiarVentana("FXMLFormatosProfesor",
                "Formatos de documentos");
    }

    @FXML
    private void clicFechasEntrega(ActionEvent event) {
        cambiarVentana("FXMLAsignarFechaEntregaDocumento",
                "Fechas de entrega");
    }

    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();

            mostrarVista(vista, titulo);
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir la pantalla solicitada.",
                    Alert.AlertType.ERROR);
        }
    }

    private void mostrarVista(Parent vista, String titulo) {
        Scene escena = new Scene(vista);
        Stage escenario =
                (Stage) lbl_documento.getScene().getWindow();

        escenario.setScene(escena);
        escenario.setTitle(titulo);
        escenario.setResizable(false);
        escenario.centerOnScreen();
        escenario.show();
    }

    private String obtenerTextoSeguro(String texto) {
        return texto == null ? "" : texto;
    }
}
