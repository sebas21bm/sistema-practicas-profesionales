package sistemapracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.FormatoDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.FormatoDocumentoServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para subir formatos de documentos.
 */
public class FXMLFormatosProfesorController implements Initializable {

    private static final String EXTENSION_PDF = ".pdf";
    private static final String EXTENSION_DOCX = ".docx";

    @FXML
    private ComboBox<FormatoDocumento> cb_documentos;
    @FXML
    private TextField txt_archivo;
    @FXML
    private Label lbl_error;
    @FXML
    private TableView<FormatoDocumento> tv_formatos;
    @FXML
    private TableColumn<FormatoDocumento, String> col_tipoDocumento;
    @FXML
    private TableColumn<FormatoDocumento, String> col_estadoFormato;

    private int idExperienciaEducativa;
    private File archivoSeleccionado;
    private byte[] archivoBytes;
    private ObservableList<FormatoDocumento> formatosDocumento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarExperienciaEducativaSesion();
        cargarFormatosDocumento();
    }

    private void configurarTabla() {
        col_tipoDocumento.setCellValueFactory(
                new PropertyValueFactory<>("tipoDocumento"));
        col_estadoFormato.setCellValueFactory(
                new PropertyValueFactory<>("estadoFormato"));
    }

    private void cargarExperienciaEducativaSesion() {
        idExperienciaEducativa = Sesion.getIdExperienciaEducativa();

        if (idExperienciaEducativa <= 0) {
            lbl_error.setText(
                    "Sin experiencia educativa asociada al profesor.");
        } else {
            lbl_error.setText("");
        }
    }

    private void cargarFormatosDocumento() {
        if (idExperienciaEducativa <= 0) {
            formatosDocumento = FXCollections.observableArrayList();
            tv_formatos.setItems(formatosDocumento);
            cb_documentos.setItems(formatosDocumento);
            return;
        }

        try {
            formatosDocumento = FXCollections.observableArrayList();
            List<FormatoDocumento> formatosBD =
                    FormatoDocumentoServicio.obtenerFormatosDocumento(
                            idExperienciaEducativa);

            formatosDocumento.addAll(formatosBD);
            tv_formatos.setItems(formatosDocumento);
            cb_documentos.setItems(formatosDocumento);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    "No es posible mostrar los documentos. "
                    + "Error al recuperar la información. Intente de nuevo.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
        FormatoDocumento formatoDocumento =
                cb_documentos.getSelectionModel().getSelectedItem();

        if (idExperienciaEducativa <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Experiencia educativa no identificada",
                    "No es posible subir formatos porque no se identificó "
                    + "la experiencia educativa del profesor.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (formatoDocumento == null) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "Debe seleccionar un documento para subir su formato.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (formatoDocumento.getIdArchivo() != null) {
            Utilidades.mostrarAlertaSimple(
                    "Formato cargado previamente",
                    "Ya existe un formato guardado para este documento. "
                    + "Si continúa, el formato anterior será reemplazado "
                    + "y dejará de estar disponible.",
                    Alert.AlertType.WARNING);
        }

        seleccionarArchivo();
    }

    private void seleccionarArchivo() {
        FileChooser selectorArchivo = new FileChooser();

        selectorArchivo.setTitle("Seleccionar formato de documento");
        selectorArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Formatos permitidos", "*.pdf", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Word", "*.docx")
        );

        File archivo = selectorArchivo.showOpenDialog(
                txt_archivo.getScene().getWindow());

        if (archivo == null) {
            return;
        }

        if (!tieneExtensionPermitida(archivo.getName())) {
            Utilidades.mostrarAlertaSimple(
                    "Tipo de archivo no permitido",
                    "No es posible cargar el archivo. El tipo de archivo "
                    + "seleccionado no es permitido. Seleccione un archivo "
                    + "válido para continuar.",
                    Alert.AlertType.WARNING);
            limpiarArchivoSeleccionado();
            return;
        }

        cargarArchivo(archivo);
    }

    private void cargarArchivo(File archivo) {
        try {
            archivoSeleccionado = archivo;
            archivoBytes = Files.readAllBytes(archivo.toPath());
            txt_archivo.setText(archivo.getName());
            lbl_error.setText("");
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al cargar archivo",
                    "No se pudo cargar el archivo. Hubo un problema con "
                    + "la subida. Intente con otro archivo o más tarde.",
                    Alert.AlertType.ERROR);
            limpiarArchivoSeleccionado();
        }
    }

    @FXML
    private void clicSubirFormato(ActionEvent event) {
        FormatoDocumento formatoSeleccionado =
                cb_documentos.getSelectionModel().getSelectedItem();

        FormatoDocumento formatoDocumento =
                obtenerFormatoDocumento(formatoSeleccionado);

        guardarFormatoDocumento(formatoDocumento);
    }

    private FormatoDocumento obtenerFormatoDocumento(
            FormatoDocumento formatoSeleccionado) {
        FormatoDocumento formatoDocumento = new FormatoDocumento();

        formatoDocumento.setIdExperienciaEducativa(
                idExperienciaEducativa);

        if (formatoSeleccionado != null) {
            formatoDocumento.setIdFormatoDocumento(
                    formatoSeleccionado.getIdFormatoDocumento());
            formatoDocumento.setIdDocumento(
                    formatoSeleccionado.getIdDocumento());
            formatoDocumento.setTipoDocumento(
                    formatoSeleccionado.getTipoDocumento());
        }

        if (archivoSeleccionado != null) {
            formatoDocumento.setNombreOriginal(
                    archivoSeleccionado.getName());
        }

        formatoDocumento.setArchivo(archivoBytes);

        return formatoDocumento;
    }

    private void guardarFormatoDocumento(FormatoDocumento formatoDocumento) {
        try {
            RespuestaOperacion respuesta =
                    FormatoDocumentoServicio.subirFormatoDocumento(
                            formatoDocumento);

            if (!respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Formato subido",
                        "El formato se ha subido correctamente.",
                        Alert.AlertType.INFORMATION);
                limpiarArchivoSeleccionado();
                cargarFormatosDocumento();
            } else {
                Utilidades.mostrarAlertaSimple(
                        "Datos inválidos",
                        respuesta.getMensaje(),
                        Alert.AlertType.WARNING);
            }
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    "No fue posible guardar el formato en el sistema. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    private boolean tieneExtensionPermitida(String nombreArchivo) {
        String nombreMinusculas = nombreArchivo.toLowerCase();

        return nombreMinusculas.endsWith(EXTENSION_PDF)
                || nombreMinusculas.endsWith(EXTENSION_DOCX);
    }

    private void limpiarArchivoSeleccionado() {
        archivoSeleccionado = null;
        archivoBytes = null;
        txt_archivo.clear();
    }

    @FXML
    private void clicConsultarExpedientes(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Consultar expedientes");
    }

    @FXML
    private void clicSubirFormatos(ActionEvent event) {
        cargarExperienciaEducativaSesion();
        cargarFormatosDocumento();
    }

    @FXML
    private void clicFechasEntrega(ActionEvent event) {
        cambiarVentana("FXMLAsignarFechaEntregaDocumento",
                "Fechas de entrega");
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioProfesor", "Inicio profesor");
    }

    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario =
                    (Stage) tv_formatos.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle(titulo);
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir la pantalla solicitada.",
                    Alert.AlertType.ERROR);
        }
    }
}