package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.DetalleEvaluacionServicio;
import sistemapracticasprofesionales.servicio.ExpedienteServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del expediente del estudiante.
 */
public class FXMLExpedientePropioController implements Initializable {

    @FXML
    private Label lb_nombreProyecto;
    @FXML
    private Label lb_avanceCalificacion;
    @FXML
    private Button btn_verDetalles;
    @FXML
    private Button btn_regresar;
    @FXML
    private Label lb_porcentajeObtenidoDocumentosIniciales;
    @FXML
    private TableView<DetalleEvaluacion> tv_documentosIniciales;
    @FXML
    private TableColumn<DetalleEvaluacion, String> col_documentoInicial;
    @FXML
    private TableColumn<DetalleEvaluacion, String>
            col_estadoDocumentoInicial;
    @FXML
    private TableView<DetalleEvaluacion> tv_reportesEvaluaciones;
    @FXML
    private TableColumn<DetalleEvaluacion, String> col_documento;
    @FXML
    private TableColumn<DetalleEvaluacion, String> col_estado;
    @FXML
    private TableColumn<DetalleEvaluacion, String> col_valor;
    @FXML
    private TableColumn<DetalleEvaluacion, String> col_porcentajeObtenido;
    
    private ExpedienteEstudiante expedientePropio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        configurarSeleccionTablas();
        cargarExpedientePropio();
    }
    
    private void configurarTablas() {
        col_documentoInicial.setCellValueFactory(
                new PropertyValueFactory<>("documentoEntrega"));
        col_estadoDocumentoInicial.setCellValueFactory(
                new PropertyValueFactory<>("estado"));
        col_documento.setCellValueFactory(
                new PropertyValueFactory<>("documentoEntrega"));
        col_estado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));
        col_valor.setCellValueFactory(
                new PropertyValueFactory<>("valorTexto"));
        col_porcentajeObtenido.setCellValueFactory(
                new PropertyValueFactory<>("porcentajeObtenidoTexto"));
    }
    
    private void configurarSeleccionTablas() {
        tv_documentosIniciales.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        tv_reportesEvaluaciones.getSelectionModel()
                                .clearSelection();
                    }
                });
        
        tv_reportesEvaluaciones.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        tv_documentosIniciales.getSelectionModel()
                                .clearSelection();
                    }
                });
    }
    
    private void cargarExpedientePropio() {
        try {
            expedientePropio = ExpedienteServicio.obtenerExpedientePropio();
            
            if (expedientePropio == null) {
                Utilidades.mostrarAlertaSimple(
                        "Expediente no encontrado",
                        "No se encontró un expediente activo para "
                        + "el periodo actual.",
                        Alert.AlertType.WARNING);
                return;
            }
            
            mostrarInformacionExpediente();
            cargarDocumentosExpediente();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar expediente",
                    "No se pudo recuperar la información de tus documentos. "
                    + "Hubo un error con sus registros. Intente más tarde.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar expediente",
                    "No fue posible recuperar la información de la sesión.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarInformacionExpediente() {
        lb_nombreProyecto.setText(expedientePropio.getNombreProyecto());
        lb_avanceCalificacion.setText(
                expedientePropio.getCalificacionTexto());
    }
    
    private void cargarDocumentosExpediente() {
        try {
            List<DetalleEvaluacion> documentosIniciales =
                    DetalleEvaluacionServicio
                            .obtenerDocumentosInicialesExpediente(
                                    expedientePropio.getIdExpediente());
            List<DetalleEvaluacion> reportesEvaluaciones =
                    DetalleEvaluacionServicio
                            .obtenerReportesEvaluacionesExpediente(
                                    expedientePropio.getIdExpediente());
            
            tv_documentosIniciales.setItems(
                    FXCollections.observableArrayList(
                            documentosIniciales));
            tv_reportesEvaluaciones.setItems(
                    FXCollections.observableArrayList(
                            reportesEvaluaciones));
            
            calcularPorcentajeDocumentosIniciales(documentosIniciales);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar expediente",
                    "No se pudo recuperar la información de tus documentos. "
                    + "Hubo un error con sus registros. Intente más tarde.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void calcularPorcentajeDocumentosIniciales(
            List<DetalleEvaluacion> documentosIniciales) {
        
        double porcentajeObtenido = 0.0;
        
        for (DetalleEvaluacion detalle : documentosIniciales) {
            porcentajeObtenido += detalle.getPorcentajeObtenido();
        }
        
        lb_porcentajeObtenidoDocumentosIniciales.setText(
                String.format("%.2f", porcentajeObtenido));
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
    private void clicVerDetalles(ActionEvent event) {
        DetalleEvaluacion detalleSeleccionado =
                obtenerDetalleSeleccionado();
        
        if (detalleSeleccionado == null) {
            Utilidades.mostrarAlertaSimple(
                    "Documento no seleccionado",
                    "Debe seleccionar un documento del expediente.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        abrirInformacionDocumento(detalleSeleccionado);
    }
    
    private DetalleEvaluacion obtenerDetalleSeleccionado() {
        DetalleEvaluacion detalleSeleccionado =
                tv_documentosIniciales.getSelectionModel()
                        .getSelectedItem();
        
        if (detalleSeleccionado != null) {
            return detalleSeleccionado;
        }
        
        return tv_reportesEvaluaciones.getSelectionModel()
                .getSelectedItem();
    }
    
    private void abrirInformacionDocumento(
            DetalleEvaluacion detalleSeleccionado) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLInfoDocumento");
            Parent vista = cargador.load();
            
            FXMLInfoDocumentoController controlador =
                    cargador.getController();
            
            boolean informacionValida =
            controlador.inicializarInformacion(detalleSeleccionado);

            if (!informacionValida) {
                Utilidades.mostrarAlertaSimple(
                        "Error al recuperar documento",
                        "No se pudo recuperar la información del documento. "
                        + "Hubo un error con su registro. "
                        + "Intente más tarde.",
                        Alert.AlertType.ERROR);
                return;
            }
            
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) btn_verDetalles.getScene()
                    .getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Información del documento");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir la información del documento.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioEstudiante", "Menu principal");
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) btn_regresar.getScene()
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