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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.servicio.DetalleEvaluacionServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para subir evaluaciones realizadas
 *              por el profesor.
 */
public class FXMLDetallesDocumentoProfesorPFController
        implements Initializable {

    private static final int TAMANIO_MAXIMO_ARCHIVO = 10 * 1024 * 1024;

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
    private File archivoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarCampos();
        configurarComboDocumentos();
    }

    public void inicializarInformacion(
            DetalleEvaluacion detalleEvaluacion,
            ExpedienteEstudiante expedienteEstudiante) {
        if (detalleEvaluacion == null
                || detalleEvaluacion.getIdDetallesEvaluacion() <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Evaluación no seleccionada",
                    "No se recibió una evaluación válida.",
                    Alert.AlertType.WARNING);
            return;
        }

        this.detalleEvaluacion = detalleEvaluacion;
        this.expedienteEstudiante = expedienteEstudiante;

        mostrarInformacionEvaluacion();
    }

    private void configurarCampos() {
        txt_archivo.setEditable(false);
        txt_calificacion.setEditable(true);

        if (vb_calificacion != null) {
            vb_calificacion.setVisible(true);
            vb_calificacion.setManaged(true);
        }
    }

    private void configurarComboDocumentos() {
        cb_documentos.setConverter(new StringConverter<DetalleEvaluacion>() {
            @Override
            public String toString(DetalleEvaluacion detalleEvaluacion) {
                if (detalleEvaluacion == null) {
                    return "";
                }

                return detalleEvaluacion.getDocumentoEntrega();
            }

            @Override
            public DetalleEvaluacion fromString(String texto) {
                return null;
            }
        });
    }

    private void mostrarInformacionEvaluacion() {
        lbl_documento.setText(
                obtenerTextoSeguro(
                        detalleEvaluacion.getDocumentoEntrega()));

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
        if (detalleEvaluacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Evaluación no seleccionada",
                    "No se recibió una evaluación para subir archivo.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (detalleEvaluacion.tieneArchivo()
                && !confirmarReemplazoArchivo()) {
            return;
        }

        seleccionarArchivo();
    }

    private boolean confirmarReemplazoArchivo() {
        return Utilidades.mostrarAlertaConfirmacion(
                "Archivo existente",
                "Ya existe un archivo cargado para esta evaluación. "
                + "Si continúa, se reemplazará el archivo anterior. "
                + "¿Desea continuar?");
    }

    private void seleccionarArchivo() {
        FileChooser selectorArchivo = new FileChooser();

        selectorArchivo.setTitle("Seleccionar evaluación del profesor");
        selectorArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Documentos permitidos", "*.pdf", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Word", "*.docx")
        );

        File archivo = selectorArchivo.showOpenDialog(
                lbl_documento.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        if (!validarArchivoSeleccionado(archivo)) {
            return;
        }

        archivoSeleccionado = archivo;
        txt_archivo.setText(archivo.getName());
    }

    private boolean validarArchivoSeleccionado(File archivo) {
        if (archivo == null
                || !archivo.exists()
                || !archivo.isFile()
                || !archivo.canRead()) {
            Utilidades.mostrarAlertaSimple(
                    "Archivo inválido",
                    "Debe seleccionar un archivo válido y legible.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (!esExtensionPermitida(archivo.getName())) {
            Utilidades.mostrarAlertaSimple(
                    "Formato no permitido",
                    "El archivo debe tener formato PDF o DOCX.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (archivo.length() <= 0
                || archivo.length() > TAMANIO_MAXIMO_ARCHIVO) {
            Utilidades.mostrarAlertaSimple(
                    "Archivo inválido",
                    "El archivo no puede estar vacío ni superar "
                    + "los 10 MB.",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private boolean esExtensionPermitida(String nombreArchivo) {
        String nombreSeguro;

        if (nombreArchivo == null) {
            return false;
        }

        nombreSeguro = nombreArchivo.toLowerCase();

        return nombreSeguro.endsWith(".pdf")
                || nombreSeguro.endsWith(".docx");
    }

    @FXML
    private void clicGuardarCambios(ActionEvent event) {
        if (detalleEvaluacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Evaluación no seleccionada",
                    "No se recibió una evaluación para guardar.",
                    Alert.AlertType.WARNING);
            return;
        }

        guardarEvaluacionProfesor();
    }

    private void guardarEvaluacionProfesor() {
        if (!confirmarGuardado()) {
            return;
        }

        try {
            if (!asignarDatosEvaluacion()) {
                return;
            }

            RespuestaOperacion respuesta =
                    DetalleEvaluacionServicio.subirEvaluacionProfesor(
                            detalleEvaluacion);

            if (respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Datos inválidos",
                        respuesta.getMensaje(),
                        Alert.AlertType.WARNING);
                return;
            }

            Utilidades.mostrarAlertaSimple(
                    "Evaluación guardada",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION);
            regresarAExpediente();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (IOException | SecurityException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al leer archivo",
                    "No fue posible leer el archivo seleccionado.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    "No fue posible guardar la evaluación.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean confirmarGuardado() {
        return Utilidades.mostrarAlertaConfirmacion(
                "Confirmar evaluación",
                "¿Seguro que desea guardar la evaluación del profesor?");
    }

    private boolean asignarDatosEvaluacion()
            throws IOException {
        if (!asignarCalificacionIngresada()) {
            return false;
        }

        if (archivoSeleccionado == null) {
            Utilidades.mostrarAlertaSimple(
                    "Archivo no seleccionado",
                    "Debe seleccionar el archivo de la evaluación.",
                    Alert.AlertType.WARNING);
            return false;
        }

        detalleEvaluacion.setNombreOriginal(
                archivoSeleccionado.getName());
        detalleEvaluacion.setArchivo(
                Files.readAllBytes(archivoSeleccionado.toPath()));
        detalleEvaluacion.setObservaciones("");

        return true;
    }

    private boolean asignarCalificacionIngresada() {
        String calificacionTexto = txt_calificacion.getText();

        if (calificacionTexto == null
                || calificacionTexto.trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    "Debe ingresar una calificación.",
                    Alert.AlertType.WARNING);
            return false;
        }

        try {
            detalleEvaluacion.setCalificacion(
                    Double.parseDouble(
                            calificacionTexto.trim()
                                    .replace(",", ".")));
            return true;
        } catch (NumberFormatException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    "La calificación debe ser un número válido.",
                    Alert.AlertType.WARNING);
            return false;
        }
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