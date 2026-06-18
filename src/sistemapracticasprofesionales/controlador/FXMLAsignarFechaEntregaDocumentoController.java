package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sistemapracticasprofesionales.modelo.pojo.EntregaDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.EntregaDocumentoServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;
/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para asignar fechas de entrega a
 *              documentos de una experiencia educativa.
 */
public class FXMLAsignarFechaEntregaDocumentoController
        implements Initializable {

    @FXML
    private ComboBox<EntregaDocumento> cb_documentos;
    @FXML
    private DatePicker dp_fechaEntrega;
    @FXML
    private Label lbl_error;
    @FXML
    private TableView<EntregaDocumento> tv_fechasEntrega;
    @FXML
    private TableColumn<EntregaDocumento, String> col_tipoDocumento;
    @FXML
    private TableColumn<EntregaDocumento, Double> col_valor;
    @FXML
    private TableColumn<EntregaDocumento, String> col_fechaEntrega;
    @FXML
    private TableColumn<EntregaDocumento, String> col_estadoFecha;

    private int idExperienciaEducativa;
    private ObservableList<EntregaDocumento> entregasDocumento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarDatePicker();
        configurarSeleccionDocumento();
        cargarExperienciaEducativaSesion();
        cargarEntregasDocumento();
    }
    

    private void configurarTabla() {
        col_tipoDocumento.setCellValueFactory(
                new PropertyValueFactory<>("descripcionDocumento"));
        col_valor.setCellValueFactory(
                new PropertyValueFactory<>("valor"));
        col_fechaEntrega.setCellValueFactory(
                new PropertyValueFactory<>("fechaEntregaTexto"));
        col_estadoFecha.setCellValueFactory(
                new PropertyValueFactory<>("estadoFecha"));
    }

    private void configurarSeleccionDocumento() {
        cb_documentos.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        dp_fechaEntrega.setValue(
                                seleccionado.getFechaEntrega());
                        lbl_error.setText("");
                    }
                });
    }
    
    private void configurarDatePicker() {
    dp_fechaEntrega.setDayCellFactory(
            new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate fecha, boolean vacio) {
                    super.updateItem(fecha, vacio);

                    if (fecha != null
                            && fecha.isBefore(LocalDate.now())) {
                        setDisable(true);
                    }
                }
            };
        }
    });
}

    private void cargarExperienciaEducativaSesion() {
        idExperienciaEducativa = Sesion.getIdExperienciaEducativa();

        if (idExperienciaEducativa <= 0) {
            lbl_error.setText(
                    "No se identificó la experiencia educativa.");
        } else {
            lbl_error.setText("");
        }
    }

    private void cargarEntregasDocumento() {
        if (idExperienciaEducativa <= 0) {
            entregasDocumento = FXCollections.observableArrayList();
            tv_fechasEntrega.setItems(entregasDocumento);
            cb_documentos.setItems(entregasDocumento);
            return;
        }

        try {
            entregasDocumento = FXCollections.observableArrayList();
            List<EntregaDocumento> entregasBD =
                    EntregaDocumentoServicio.obtenerEntregasDocumento(
                            idExperienciaEducativa);

            entregasDocumento.addAll(entregasBD);
            tv_fechasEntrega.setItems(entregasDocumento);
            cb_documentos.setItems(entregasDocumento);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    "No fue posible recuperar las fechas de entrega.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicAsignarFecha(ActionEvent event) {
        EntregaDocumento entregaSeleccionada =
                cb_documentos.getSelectionModel().getSelectedItem();
        LocalDate fechaEntrega = dp_fechaEntrega.getValue();

        EntregaDocumento entregaDocumento =
                crearEntregaDocumento(
                        entregaSeleccionada,
                        fechaEntrega);

        guardarFechaEntrega(entregaDocumento);
    }

    private EntregaDocumento crearEntregaDocumento(
            EntregaDocumento entregaSeleccionada,
            LocalDate fechaEntrega) {
        EntregaDocumento entregaDocumento = new EntregaDocumento();

        if (entregaSeleccionada != null) {
            entregaDocumento.setIdEntregaDocumento(
                    entregaSeleccionada.getIdEntregaDocumento());
            entregaDocumento.setTipoDocumento(
                    entregaSeleccionada.getTipoDocumento());
            entregaDocumento.setNumeroEntrega(
                    entregaSeleccionada.getNumeroEntrega());
        }

        entregaDocumento.setFechaEntrega(fechaEntrega);

        return entregaDocumento;
    }

    private void guardarFechaEntrega(
            EntregaDocumento entregaDocumento) {
        try {
            RespuestaOperacion respuesta =
                    EntregaDocumentoServicio.asignarFechaEntrega(
                            entregaDocumento);

            if (!respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Fecha asignada",
                        respuesta.getMensaje(),
                        Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarEntregasDocumento();
            } else {
                lbl_error.setText(respuesta.getMensaje());
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
                    "No fue posible guardar la fecha de entrega.",
                    Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        cb_documentos.getSelectionModel().clearSelection();
        dp_fechaEntrega.setValue(null);
        lbl_error.setText("");
    }

    
    // NAVEGACIÓN
    
    @FXML
    private void clicConsultarExpedientes(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Consultar expedientes");
    }

    @FXML
    private void clicSubirFormatos(ActionEvent event) {
        cambiarVentana("FXMLFormatosProfesor",
                "Formatos de documentos");
    }

    @FXML
    private void clicFechasEntrega(ActionEvent event) {
        cargarExperienciaEducativaSesion();
        cargarEntregasDocumento();
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
                    (Stage) tv_fechasEntrega.getScene().getWindow();
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