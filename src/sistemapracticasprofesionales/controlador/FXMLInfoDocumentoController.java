package sistemapracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
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
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.DetalleEvaluacionServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista de información de documentos.
 */
public class FXMLInfoDocumentoController implements Initializable {

    private static final int TAMANIO_MAXIMO_ARCHIVO = 10 * 1024 * 1024;

    @FXML
    private Label lb_nombreDocumento;
    @FXML
    private Label lb_estadoTitulo;
    @FXML
    private Label lb_calificacionTitulo;
    @FXML
    private Label lb_comentariosTitulo;
    @FXML
    private Label lb_calificacion;
    @FXML
    private Label lb_estado;
    @FXML
    private Label lb_comentarios;
    
    private DetalleEvaluacion detalleEvaluacion;
    private File archivoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public boolean inicializarInformacion(DetalleEvaluacion detalleEvaluacion) {
        if (!esDetalleEvaluacionValido(detalleEvaluacion)) {
            return false;
        }

        this.detalleEvaluacion = detalleEvaluacion;
        cargarInformacionDocumento();
        return true;
    }
    
    private boolean esDetalleEvaluacionValido(
        DetalleEvaluacion detalleEvaluacion) {

        return detalleEvaluacion != null
                && detalleEvaluacion.getIdDetallesEvaluacion() > 0
                && detalleEvaluacion.getDocumentoEntrega() != null
                && !detalleEvaluacion.getDocumentoEntrega().trim().isEmpty()
                && detalleEvaluacion.getEstado() != null
                && !detalleEvaluacion.getEstado().trim().isEmpty();
    }
    
    private void cargarInformacionDocumento() {
        if (detalleEvaluacion == null) {
            return;
        }
        
        lb_nombreDocumento.setText(
                obtenerTextoSeguro(
                        detalleEvaluacion.getDocumentoEntrega()));
        lb_estado.setText(
                obtenerTextoSeguro(detalleEvaluacion.getEstado()));
        lb_calificacion.setText(
                detalleEvaluacion.getCalificacionTexto());
        lb_comentarios.setText(
                obtenerTextoSeguro(detalleEvaluacion.getObservaciones()));
    }

    @FXML
    private void clicConsultarExpediente(ActionEvent event) {
        cambiarVentana("FXMLExpedientePropio", "Expediente propio");
    }

    @FXML
    private void clicDescargarFormatos(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLExpedientePropio", "Expediente propio");
    }

    @FXML
    private void clicVerDocumento(ActionEvent event) {
        if (detalleEvaluacion == null
                || !detalleEvaluacion.tieneArchivo()) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no disponible",
                    "No hay un archivo cargado para este documento.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        descargarDocumento();
    }
    
    private void descargarDocumento() {
        try {
            DetalleEvaluacion documentoArchivo =
                    DetalleEvaluacionServicio
                            .obtenerArchivoDocumentoEstudiante(
                                    detalleEvaluacion
                                            .getIdDetallesEvaluacion());
            
            if (documentoArchivo == null
                    || documentoArchivo.getArchivo() == null) {
                Utilidades.mostrarAlertaSimple(
                        "Documento no disponible",
                        "No fue posible recuperar el archivo.",
                        Alert.AlertType.WARNING);
                return;
            }
            
            guardarArchivoEnEquipo(documentoArchivo);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar archivo",
                    "No se pudo recuperar la información del documento. "
                    + "Hubo un error con su registro. Intente más tarde.",
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
                        lb_nombreDocumento.getScene().getWindow());
        
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

    @FXML
    private void clicSubirDocumento(ActionEvent event) {
        if (detalleEvaluacion == null) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "No se recibió un documento para subir.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        if (detalleEvaluacion.tieneArchivo()) {
            Utilidades.mostrarAlertaSimple(
                    "Documento cargado previamente",
                    "Ya has subido este documento. Se reemplazará "
                    + "y dejará de estar disponible.",
                    Alert.AlertType.WARNING);
        }
        
        seleccionarArchivo();
    }
    
    private void seleccionarArchivo() {
        FileChooser selectorArchivo = new FileChooser();
        
        selectorArchivo.setTitle("Seleccionar documento");
        selectorArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Documentos permitidos", "*.pdf", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Word", "*.docx")
        );
        
        File archivo = selectorArchivo.showOpenDialog(
                lb_nombreDocumento.getScene().getWindow());
        
        if (archivo == null) {
            return;
        }
        
        if (!validarArchivoSeleccionado(archivo)) {
            return;
        }
        
        archivoSeleccionado = archivo;
        confirmarSubidaDocumento();
    }
    
    private boolean validarArchivoSeleccionado(File archivo) {
        if (archivo == null || !archivo.exists()
                || !archivo.isFile() || !archivo.canRead()) {
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
    
    private void confirmarSubidaDocumento() {
        boolean respuesta = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar subida de documento",
                "¿Seguro que desea subir este documento?");
        
        if (respuesta) {
            subirDocumento();
        }
    }
    
    private void subirDocumento() {
        try {
            DetalleEvaluacion documentoSubir =
                    DetalleEvaluacionServicio
                            .obtenerDetalleEvaluacionEstudiantePorId(
                                    detalleEvaluacion
                                            .getIdDetallesEvaluacion());
            
            documentoSubir.setNombreOriginal(
                    archivoSeleccionado.getName());
            documentoSubir.setArchivo(
                    Files.readAllBytes(archivoSeleccionado.toPath()));
            
            RespuestaOperacion respuesta =
                    DetalleEvaluacionServicio
                            .subirDocumentoEstudiante(documentoSubir);
            
            if (respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Error al subir documento",
                        respuesta.getMensaje(),
                        Alert.AlertType.WARNING);
                return;
            }
            
            Utilidades.mostrarAlertaSimple(
                    "Documento subido",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION);
            
            actualizarInformacionDocumento();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar archivo",
                    "No se pudo guardar el archivo en el sistema. "
                    + "Hubo un problema con la subida. "
                    + "Intente con otro archivo o más tarde.",
                    Alert.AlertType.ERROR);
        } catch (IOException | SecurityException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al cargar archivo",
                    "No se pudo cargar el archivo. Hubo un problema "
                    + "con la subida. Intente con otro archivo "
                    + "o más tarde.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void actualizarInformacionDocumento() {
        try {
            detalleEvaluacion = DetalleEvaluacionServicio
                    .obtenerDetalleEvaluacionEstudiantePorId(
                            detalleEvaluacion.getIdDetallesEvaluacion());
            cargarInformacionDocumento();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al actualizar información",
                    "El documento se subió, pero no fue posible "
                    + "actualizar la información en pantalla.",
                    Alert.AlertType.WARNING);
        }
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
    
    private String obtenerNombreArchivoSeguro(String nombreArchivo) {
        String nombreSeguro;
        
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return "documento";
        }
        
        nombreSeguro = nombreArchivo.trim()
                .replaceAll("[\\\\/:*?\"<>|]", "_");
        
        if (nombreSeguro.length() > 120) {
            nombreSeguro = nombreSeguro.substring(0, 120);
        }
        
        return nombreSeguro;
    }
    
    private String obtenerTextoSeguro(String texto) {
        return texto == null ? "" : texto;
    }
    
    private boolean esExtensionPermitida(File archivo) {
        return archivo != null && esExtensionPermitida(archivo.getName());
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) lb_nombreDocumento.getScene()
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