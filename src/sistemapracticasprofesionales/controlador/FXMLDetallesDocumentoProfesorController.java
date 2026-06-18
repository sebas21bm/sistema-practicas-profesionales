package sistemapracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.servicio.DetalleEvaluacionServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para validar documentos iniciales y
 *              consultar detalles de documentos del expediente.
 */
public class FXMLDetallesDocumentoProfesorController
        implements Initializable {

    private static final String ESTADO_APROBADO = "Aprobado";
    private static final String ESTADO_RECHAZADO = "Rechazado";
    private static final int LONGITUD_MAXIMA_NOMBRE_ARCHIVO = 120;

    @FXML
    private Label lbl_documento;
    @FXML
    private ComboBox<String> cb_estado;
    @FXML
    private TextArea ta_comentarios;
    @FXML
    private TextField txt_calificacion;

    private DetalleEvaluacion detalleEvaluacion;
    private ExpedienteEstudiante expedienteEstudiante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarEstados();
        bloquearEdicionCalificacion();
    }

    public void inicializarInformacion(
            DetalleEvaluacion detalleEvaluacion,
            ExpedienteEstudiante expedienteEstudiante) {
        if (detalleEvaluacion == null
                || detalleEvaluacion.getIdDetallesEvaluacion() <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "No se recibió un documento válido.",
                    Alert.AlertType.WARNING);
            return;
        }

        this.detalleEvaluacion = detalleEvaluacion;
        this.expedienteEstudiante = expedienteEstudiante;

        mostrarInformacionDocumento();
        configurarVistaPorTipoDocumento();
    }

    private void configurarEstados() {
        cb_estado.setItems(FXCollections.observableArrayList(
                ESTADO_APROBADO,
                ESTADO_RECHAZADO
        ));
    }

    private void bloquearEdicionCalificacion() {
        txt_calificacion.setEditable(false);
    }

    private void mostrarInformacionDocumento() {
        lbl_documento.setText(
                obtenerTextoSeguro(
                        detalleEvaluacion.getDocumentoEntrega()));
        cb_estado.getSelectionModel().select(
                detalleEvaluacion.getEstado());
        ta_comentarios.setText(
                obtenerTextoSeguro(
                        detalleEvaluacion.getObservaciones()));
        txt_calificacion.setText(
                detalleEvaluacion.getCalificacionTexto());
    }

    private void configurarVistaPorTipoDocumento() {
        if (detalleEvaluacion.esDocumentoInicial()) {
            ocultarCalificacion();
        } else {
            mostrarCalificacion();
            cb_estado.setDisable(true);
            ta_comentarios.setEditable(false);
        }
    }

    private void ocultarCalificacion() {
        Node contenedorCalificacion = txt_calificacion.getParent();

        if (contenedorCalificacion != null) {
            contenedorCalificacion.setVisible(false);
            contenedorCalificacion.setManaged(false);
        }
    }

    private void mostrarCalificacion() {
        Node contenedorCalificacion = txt_calificacion.getParent();

        if (contenedorCalificacion != null) {
            contenedorCalificacion.setVisible(true);
            contenedorCalificacion.setManaged(true);
        }
    }

    @FXML
    private void clicVerDocumento(ActionEvent event) {
        if (detalleEvaluacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "No se recibió un documento para descargar.",
                    Alert.AlertType.WARNING);
            return;
        }

        descargarDocumento();
    }

    private void descargarDocumento() {
        try {
            DetalleEvaluacion documentoArchivo =
                    DetalleEvaluacionServicio.obtenerArchivoDocumento(
                            detalleEvaluacion.getIdDetallesEvaluacion());

            if (documentoArchivo == null
                    || documentoArchivo.getArchivo() == null) {
                Utilidades.mostrarAlertaSimple(
                        "Archivo no disponible",
                        "El documento seleccionado no tiene archivo.",
                        Alert.AlertType.WARNING);
                return;
            }

            guardarArchivoEnEquipo(documentoArchivo);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar archivo",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (IOException | SecurityException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al descargar archivo",
                    "No fue posible descargar el documento seleccionado.",
                    Alert.AlertType.ERROR);
        }
    }

    private void guardarArchivoEnEquipo(
            DetalleEvaluacion documentoArchivo) throws IOException {
        FileChooser selectorArchivo = new FileChooser();
        selectorArchivo.setTitle("Descargar documento");
        selectorArchivo.setInitialFileName(
                obtenerNombreArchivoSeguro(
                        documentoArchivo.getNombreOriginal()));

        File archivoDestino =
                selectorArchivo.showSaveDialog(
                        lbl_documento.getScene().getWindow());

        if (archivoDestino == null) {
            return;
        }

        Files.write(archivoDestino.toPath(),
                documentoArchivo.getArchivo());

        Utilidades.mostrarAlertaSimple(
                "Descarga completada",
                "El documento se descargó correctamente.",
                Alert.AlertType.INFORMATION);
    }

    private String obtenerNombreArchivoSeguro(String nombreArchivo) {
        String nombreSeguro;

        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return "documento_entregado";
        }

        nombreSeguro = nombreArchivo.trim()
                .replaceAll("[\\\\/:*?\"<>|]", "_");

        if (nombreSeguro.length() > LONGITUD_MAXIMA_NOMBRE_ARCHIVO) {
            nombreSeguro = nombreSeguro.substring(
                    0,
                    LONGITUD_MAXIMA_NOMBRE_ARCHIVO);
        }

        return nombreSeguro;
    }

    @FXML
    private void clicGuardarCambios(ActionEvent event) {
        if (detalleEvaluacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "No se recibió un documento para validar.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (!detalleEvaluacion.esDocumentoInicial()) {
            Utilidades.mostrarAlertaSimple(
                    "Función pendiente",
                    "La evaluación de reportes se implementará "
                    + "en el CU-26.",
                    Alert.AlertType.INFORMATION);
            return;
        }

        guardarValidacionDocumentoInicial();
    }

    private void guardarValidacionDocumentoInicial() {
        if (!confirmarGuardado()) {
            return;
        }

        detalleEvaluacion.setEstado(
                cb_estado.getSelectionModel().getSelectedItem());
        detalleEvaluacion.setObservaciones(
                ta_comentarios.getText());

        try {
            RespuestaOperacion respuesta =
                    DetalleEvaluacionServicio.validarDocumentoInicial(
                            detalleEvaluacion);

            if (respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Datos inválidos",
                        respuesta.getMensaje(),
                        Alert.AlertType.WARNING);
                return;
            }

            Utilidades.mostrarAlertaSimple(
                    "Validación registrada",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION);
            regresarAExpediente();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    "No fue posible guardar la validación.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean confirmarGuardado() {
        return Utilidades.mostrarAlertaConfirmacion(
                "Confirmar validación",
                "¿Desea guardar los cambios del documento?");
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

            navegarA (vista, "Expediente estudiante");
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

            navegarA(vista, titulo);
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir la pantalla solicitada.",
                    Alert.AlertType.ERROR);
        }
    }

    private void navegarA(Parent vista, String titulo) {
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