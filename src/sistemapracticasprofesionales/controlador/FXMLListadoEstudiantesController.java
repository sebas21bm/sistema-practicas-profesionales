package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.servicio.EstudianteServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador del listado de estudiantes registrados.
 */
public class FXMLListadoEstudiantesController implements Initializable {

    private static final String FILTRO_MATRICULA = "Por matrícula";
    private static final String FILTRO_NOMBRE = "Por nombre";
    private static final String FILTRO_TODOS = "Ver todos";

    @FXML
    private TableView<Estudiante> tv_estudiantes;
    @FXML
    private TableColumn<Estudiante, String> col_matricula;
    @FXML
    private TableColumn<Estudiante, String> col_nombre;
    @FXML
    private TableColumn<Estudiante, String> col_apellidoPaterno;
    @FXML
    private TableColumn<Estudiante, String> col_apellidoMaterno;
    @FXML
    private TableColumn<Estudiante, String> col_telefono;
    @FXML
    private TableColumn<Estudiante, String> col_correo;
    @FXML
    private TableColumn<Estudiante, String> col_activo;
    @FXML
    private TextField txt_busqueda;
    @FXML
    private ComboBox<String> cb_filtros;
    @FXML
    private Button btn_actualizar;

    private ObservableList<String> filtros = FXCollections.observableArrayList(
            FILTRO_MATRICULA,
            FILTRO_NOMBRE,
            FILTRO_TODOS
    );
    private String filtroBusqueda;
    private ObservableList<Estudiante> estudiantes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroBusqueda = "";
        cb_filtros.setItems(filtros);
        cb_filtros.getSelectionModel().select(FILTRO_TODOS);
        configurarTabla();
        configurarSeleccionFiltro();
        cargarInformacionEstudiantes();
        btn_actualizar.setDisable(true);
    }

    private void configurarTabla() {
        col_matricula.setCellValueFactory(
                new PropertyValueFactory<>("matricula"));
        col_nombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));
        col_apellidoPaterno.setCellValueFactory(
                new PropertyValueFactory<>("apellidoPaterno"));
        col_apellidoMaterno.setCellValueFactory(
                new PropertyValueFactory<>("apellidoMaterno"));
        col_telefono.setCellValueFactory(
                new PropertyValueFactory<>("telefono"));
        col_correo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));
        col_activo.setCellValueFactory(
                new PropertyValueFactory<>("estado"));
    }

    private void configurarSeleccionFiltro() {
        cb_filtros.valueProperty().addListener(
                new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue) {
                if (newValue != null) {
                    if (newValue.equals(FILTRO_TODOS)) {
                        txt_busqueda.setText("");
                        filtroBusqueda = "";
                        cargarInformacionEstudiantes();
                    } else {
                        filtroBusqueda = newValue;
                    }
                }
            }
        });
    }

    private void cargarInformacionEstudiantes() {
        try {
            estudiantes = FXCollections.observableArrayList();
            List<Estudiante> estudiantesBD =
                    EstudianteServicio.obtenerEstudiantes();

            estudiantes.addAll(estudiantesBD);
            tv_estudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al cargar estudiantes",
                    "Lo sentimos, la información de los estudiantes "
                    + "no puede ser cargada en este momento.",
                    Alert.AlertType.WARNING);
        }
    }

    private void actualizarInformacion() {
        if (filtroBusqueda.equals("")) {
            cargarInformacionEstudiantes();
        } else {
            buscarPorFiltro();
        }
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        buscarPorFiltro();
    }

    private void buscarPorFiltro() {
        String campoBusqueda = txt_busqueda.getText().trim();

        if (filtroBusqueda.equals("")
                || cb_filtros.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple(
                    "Sin filtro",
                    "Por favor selecciona un filtro para realizar "
                    + "la búsqueda.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (campoBusqueda.isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                    "Sin búsqueda",
                    "Ingresa un dato para realizar la búsqueda.",
                    Alert.AlertType.WARNING);
            return;
        }

        try {
            estudiantes = FXCollections.observableArrayList();
            List<Estudiante> estudiantesBD =
                    buscarEstudiantesBD(campoBusqueda);

            estudiantes.addAll(estudiantesBD);
            tv_estudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al buscar estudiantes",
                    "No fue posible realizar la búsqueda.",
                    Alert.AlertType.WARNING);
        }
    }

    private List<Estudiante> buscarEstudiantesBD(String campoBusqueda)
            throws SQLException, NullPointerException {
        if (filtroBusqueda.equals(FILTRO_MATRICULA)) {
            return EstudianteServicio.buscarEstudiantesPorMatricula(
                    campoBusqueda);
        }

        if (filtroBusqueda.equals(FILTRO_NOMBRE)) {
            return EstudianteServicio.buscarEstudiantesPorNombre(
                    campoBusqueda);
        }

        return EstudianteServicio.obtenerEstudiantes();
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
        actualizarInformacion();
    }

    @FXML
    private void clicRegistrarEstudiante(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioEstudiante");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Registro de estudiante");
            escenarioFormulario.setResizable(false);
            escenarioFormulario.centerOnScreen();
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();

            actualizarInformacion();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicListadoEstudiantes(ActionEvent event) {
        cb_filtros.getSelectionModel().select(FILTRO_TODOS);
        txt_busqueda.setText("");
        filtroBusqueda = "";
        cargarInformacionEstudiantes();
    }

    @FXML
    private void clicRegistrarProfesor(ActionEvent event) {
        cambiarVentana("FXMLFormularioProfesor",
                "Registro de profesor");
    }

    @FXML
    private void clicListadoProfesores(ActionEvent event) {
        cambiarVentana("FXMLListadoProfesores",
                "Listado de profesores");
    }

    @FXML
    private void clicRegistrarCoordinador(ActionEvent event) {
        cambiarVentana("FXMLFormularioCoordinador",
                "Registro de coordinador");
    }

    @FXML
    private void clicListadoCoordinadores(ActionEvent event) {
        cambiarVentana("FXMLListadoCoordinadores",
                "Listado de coordinadores");
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLMenuAdministrador", "Inicio administrador");
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
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}