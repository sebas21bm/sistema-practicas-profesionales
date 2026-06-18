package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.servicio.DetalleEvaluacionServicio;
import sistemapracticasprofesionales.servicio.ExpedienteServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para consultar el expediente de un
 *              estudiante desde el rol profesor.
 */
public class FXMLExpedienteEstudianteProfesorController
        implements Initializable {

    @FXML
    private Label lbl_nombreEstudiante;
    @FXML
    private Label lbl_calificacionTotal;
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

    private ExpedienteEstudiante expedienteSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        configurarSeleccionTablas();
        limpiarVista();
    }

    public void inicializarInformacion(
            ExpedienteEstudiante expedienteSeleccionado) {
        if (expedienteSeleccionado == null
                || expedienteSeleccionado.getIdExpediente() <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Expediente no seleccionado",
                    "No se recibió un expediente válido.",
                    Alert.AlertType.WARNING);
            return;
        }

        this.expedienteSeleccionado = expedienteSeleccionado;
        mostrarInformacionEstudiante();
        cargarDetallesExpediente();
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

    private void limpiarVista() {
        lbl_nombreEstudiante.setText("");
        lbl_calificacionTotal.setText("");
        limpiarTablas();
    }

    private void mostrarInformacionEstudiante() {
        lbl_nombreEstudiante.setText(
                obtenerTextoSeguro(
                        expedienteSeleccionado.getNombreEstudiante()));
        lbl_calificacionTotal.setText(
                expedienteSeleccionado.getCalificacionTexto());
    }

    private void cargarDetallesExpediente() {
        try {
            List<DetalleEvaluacion> documentosIniciales =
                    DetalleEvaluacionServicio
                            .obtenerDocumentosInicialesExpediente(
                                    expedienteSeleccionado
                                            .getIdExpediente());

            List<DetalleEvaluacion> reportesEvaluaciones =
                    DetalleEvaluacionServicio
                            .obtenerReportesEvaluacionesExpediente(
                                    expedienteSeleccionado
                                            .getIdExpediente());

            mostrarDocumentosIniciales(documentosIniciales);
            mostrarReportesEvaluaciones(reportesEvaluaciones);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    "No fue posible recuperar los documentos "
                    + "del expediente.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    private void mostrarDocumentosIniciales(
            List<DetalleEvaluacion> documentos) {
        ObservableList<DetalleEvaluacion> documentosTabla =
                FXCollections.observableArrayList();

        documentosTabla.addAll(documentos);
        tv_documentosIniciales.setItems(documentosTabla);
    }

    private void mostrarReportesEvaluaciones(
            List<DetalleEvaluacion> documentos) {
        ObservableList<DetalleEvaluacion> documentosTabla =
                FXCollections.observableArrayList();

        documentosTabla.addAll(documentos);
        tv_reportesEvaluaciones.setItems(documentosTabla);
    }

    private void limpiarTablas() {
        tv_documentosIniciales.setItems(
                FXCollections.observableArrayList());
        tv_reportesEvaluaciones.setItems(
                FXCollections.observableArrayList());
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

        abrirDetallesDocumento(detalleSeleccionado);
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

    private void abrirDetallesDocumento(
            DetalleEvaluacion detalleSeleccionado) {
        if (detalleSeleccionado.esEvaluacionProfesor()) {
            abrirDetallesEvaluacionProfesor(detalleSeleccionado);
        } else {
            abrirDetallesDocumentoProfesor(detalleSeleccionado);
        }
    }

    private void abrirDetallesDocumentoProfesor(
            DetalleEvaluacion detalleSeleccionado) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLDetallesDocumentoProfesor");
            Parent vista = cargador.load();

            FXMLDetallesDocumentoProfesorController controlador =
                    cargador.getController();
            controlador.inicializarInformacion(
                    detalleSeleccionado,
                    expedienteSeleccionado);

            mostrarVista(vista, "Detalles del documento");
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir los detalles del documento.",
                    Alert.AlertType.ERROR);
        }
    }

    private void abrirDetallesEvaluacionProfesor(
            DetalleEvaluacion detalleSeleccionado) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML(
                            "FXMLDetallesDocumentoProfesorPF");
            Parent vista = cargador.load();

            FXMLDetallesDocumentoProfesorPFController controlador =
                    cargador.getController();
            controlador.inicializarInformacion(
                    detalleSeleccionado,
                    expedienteSeleccionado);

            mostrarVista(vista, "Evaluación del profesor");
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error de navegación",
                    "No fue posible abrir la evaluación del profesor.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCalcularCalificacion(ActionEvent event) {
        if (expedienteSeleccionado == null
                || expedienteSeleccionado.getIdExpediente() <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Expediente no seleccionado",
                    "No se recibió un expediente válido.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (!confirmarCalculoCalificacion()) {
            return;
        }

        calcularCalificacionFinal();
    }
    
    private boolean confirmarCalculoCalificacion() {
    return Utilidades.mostrarAlertaConfirmacion(
            "Confirmar cálculo",
            "¿Desea calcular la calificación final del expediente?");
}

private void calcularCalificacionFinal() {
    try {
        RespuestaOperacion respuesta =
                ExpedienteServicio.calcularCalificacionExpediente(
                        expedienteSeleccionado.getIdExpediente(),
                        expedienteSeleccionado.getIdExperienciaEducativa());

        if (respuesta.getError()) {
            Utilidades.mostrarAlertaSimple(
                    "Expediente incompleto",
                    respuesta.getMensaje(),
                    Alert.AlertType.WARNING);
            lbl_calificacionTotal.setText("");
            return;
        }

        Utilidades.mostrarAlertaSimple(
                "Calificación calculada",
                respuesta.getMensaje(),
                Alert.AlertType.INFORMATION);

        actualizarExpedienteDesdeBaseDatos();
    } catch (SQLException ex) {
        Utilidades.mostrarAlertaSimple(
                "Error al calcular",
                ex.getMessage(),
                Alert.AlertType.ERROR);
    } catch (NullPointerException ex) {
        Utilidades.mostrarAlertaSimple(
                "Error al calcular",
                "No fue posible calcular la calificación final.",
                Alert.AlertType.ERROR);
    } catch (IllegalArgumentException ex) {
        Utilidades.mostrarAlertaSimple(
                "Datos inválidos",
                ex.getMessage(),
                Alert.AlertType.WARNING);
    }
}

    private void actualizarExpedienteDesdeBaseDatos() {
        try {
            ExpedienteEstudiante expedienteActualizado =
                    ExpedienteServicio.obtenerExpedienteEstudianteProfesorPorId(
                            expedienteSeleccionado.getIdExpediente(),
                            expedienteSeleccionado
                                    .getIdExperienciaEducativa());

            if (expedienteActualizado == null) {
                Utilidades.mostrarAlertaSimple(
                        "Expediente no encontrado",
                        "No fue posible actualizar la información "
                        + "del expediente.",
                        Alert.AlertType.WARNING);
                return;
            }

            expedienteSeleccionado = expedienteActualizado;
            mostrarInformacionEstudiante();
            cargarDetallesExpediente();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al actualizar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al actualizar",
                    "No fue posible actualizar la información "
                    + "del expediente.",
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

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLListaEstudianteProfesor", "Estudiantes");
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
                (Stage) tv_documentosIniciales.getScene().getWindow();

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