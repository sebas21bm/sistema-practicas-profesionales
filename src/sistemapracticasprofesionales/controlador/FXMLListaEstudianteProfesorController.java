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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.ExpedienteServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Controlador de la vista para listar estudiantes con
 *              expediente asignado al profesor.
 */
public class FXMLListaEstudianteProfesorController
        implements Initializable {

    private static final String FILTRO_MATRICULA = "Matrícula";
    private static final String FILTRO_NOMBRE = "Nombre";

    @FXML
    private TextField txt_busqueda;
    @FXML
    private ComboBox<String> cb_filtro;
    @FXML
    private TableView<ExpedienteEstudiante> tv_estudiantes;
    @FXML
    private TableColumn<ExpedienteEstudiante, String> col_matricula;
    @FXML
    private TableColumn<ExpedienteEstudiante, String>
            col_nombreEstudiante;
    @FXML
    private TableColumn<ExpedienteEstudiante, String> col_proyecto;
    @FXML
    private TableColumn<ExpedienteEstudiante, String> col_calificacion;

    private int idExperienciaEducativa;
    private ObservableList<ExpedienteEstudiante> expedientesTabla;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarFiltros();
        cargarExperienciaEducativaSesion();
        cargarExpedientesEstudiantes();
    }

    private void configurarTabla() {
        col_matricula.setCellValueFactory(
                new PropertyValueFactory<>("matricula"));
        col_nombreEstudiante.setCellValueFactory(
                new PropertyValueFactory<>("nombreEstudiante"));
        col_proyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombreProyecto"));
        col_calificacion.setCellValueFactory(
                new PropertyValueFactory<>("calificacionTexto"));
    }

    private void configurarFiltros() {
        cb_filtro.setItems(FXCollections.observableArrayList(
                FILTRO_MATRICULA,
                FILTRO_NOMBRE
        ));
        cb_filtro.getSelectionModel().select(FILTRO_MATRICULA);
    }

    private void cargarExperienciaEducativaSesion() {
        idExperienciaEducativa = Sesion.getIdExperienciaEducativa();

        if (idExperienciaEducativa <= 0) {
            Utilidades.mostrarAlertaSimple(
                    "Experiencia educativa no identificada",
                    "No se identificó la experiencia educativa "
                    + "del profesor.",
                    Alert.AlertType.WARNING);
        }
    }

    private void cargarExpedientesEstudiantes() {
        if (idExperienciaEducativa <= 0) {
            mostrarExpedientes(FXCollections.observableArrayList());
            return;
        }

        try {
            List<ExpedienteEstudiante> expedientes =
                    ExpedienteServicio.
                            obtenerExpedientesEstudiantesProfesor(
                                    idExperienciaEducativa);
            mostrarExpedientes(expedientes);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    "No fue posible recuperar los estudiantes.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    private void mostrarExpedientes(
            List<ExpedienteEstudiante> expedientes) {
        expedientesTabla = FXCollections.observableArrayList();
        expedientesTabla.addAll(expedientes);
        tv_estudiantes.setItems(expedientesTabla);
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        buscarExpedientes();
    }

    private void buscarExpedientes() {
        String filtro = cb_filtro.getSelectionModel().getSelectedItem();
        String criterio = txt_busqueda.getText();

        try {
            List<ExpedienteEstudiante> expedientes =
                    ExpedienteServicio.
                            buscarExpedientesEstudiantesProfesor(
                                    idExperienciaEducativa,
                                    filtro,
                                    criterio);
            mostrarExpedientes(expedientes);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al buscar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al buscar",
                    "No fue posible realizar la búsqueda.",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    ex.getMessage(),
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicVerExpediente(ActionEvent event) {
        ExpedienteEstudiante expedienteSeleccionado =
                tv_estudiantes.getSelectionModel().getSelectedItem();

        if (expedienteSeleccionado == null) {
            Utilidades.mostrarAlertaSimple(
                    "Estudiante no seleccionado",
                    "Debe seleccionar un estudiante para consultar "
                    + "su expediente.",
                    Alert.AlertType.WARNING);
            return;
        }

        Sesion.setExpedienteSeleccionado(expedienteSeleccionado);
        cambiarVentana("FXMLExpedienteEstudianteProfesor",
                "Expediente estudiante");
    }

    @FXML
    private void clicConsultarExpedientes(ActionEvent event) {
        cargarExperienciaEducativaSesion();
        cargarExpedientesEstudiantes();
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
        cambiarVentana("FXMLInicioProfesor", "Inicio profesor");
    }

    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario =
                    (Stage) tv_estudiantes.getScene().getWindow();
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