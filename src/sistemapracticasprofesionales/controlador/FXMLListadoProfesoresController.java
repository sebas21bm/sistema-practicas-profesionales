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
import sistemapracticasprofesionales.modelo.pojo.Profesor;
import sistemapracticasprofesionales.servicio.ProfesorServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador del listado de profesores registrados.
 */
public class FXMLListadoProfesoresController implements Initializable {

    private static final String FILTRO_NUMERO_EMPLEADO =
            "Por número de empleado";
    private static final String FILTRO_NOMBRE = "Por nombre";
    private static final String FILTRO_TODOS = "Ver todos";

    @FXML
    private TableView<Profesor> tv_profesores;
    @FXML
    private TableColumn<Profesor, String> col_numeroEmpleado;
    @FXML
    private TableColumn<Profesor, String> col_nombre;
    @FXML
    private TableColumn<Profesor, String> col_apellidoPaterno;
    @FXML
    private TableColumn<Profesor, String> col_apellidoMaterno;
    @FXML
    private TableColumn<Profesor, String> col_telefono;
    @FXML
    private TableColumn<Profesor, String> col_correo;
    @FXML
    private TableColumn<Profesor, String> col_activo;
    @FXML
    private TextField txt_busqueda;
    @FXML
    private ComboBox<String> cb_filtros;
    @FXML
    private Button btn_actualizar;

    private ObservableList<String> filtros = FXCollections.observableArrayList(
            FILTRO_NUMERO_EMPLEADO,
            FILTRO_NOMBRE,
            FILTRO_TODOS
    );
    private String filtroBusqueda;
    private ObservableList<Profesor> profesores;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroBusqueda = "";
        cb_filtros.setItems(filtros);
        cb_filtros.getSelectionModel().select(FILTRO_TODOS);
        configurarTabla();
        configurarSeleccionFiltro();
        cargarInformacionProfesores();
        btn_actualizar.setDisable(true);
    }

    private void configurarTabla() {
        col_numeroEmpleado.setCellValueFactory(
                new PropertyValueFactory<>("numeroEmpleado"));
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
                        cargarInformacionProfesores();
                    } else {
                        filtroBusqueda = newValue;
                    }
                }
            }
        });
    }

    private void cargarInformacionProfesores() {
        try {
            profesores = FXCollections.observableArrayList();
            List<Profesor> profesoresBD =
                    ProfesorServicio.obtenerProfesores();

            profesores.addAll(profesoresBD);
            tv_profesores.setItems(profesores);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al cargar profesores",
                    "Lo sentimos, la información de los profesores "
                    + "no puede ser cargada en este momento.",
                    Alert.AlertType.WARNING);
        }
    }

    private void actualizarInformacion() {
        if (filtroBusqueda.equals("")) {
            cargarInformacionProfesores();
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
            profesores = FXCollections.observableArrayList();
            List<Profesor> profesoresBD = buscarProfesoresBD(campoBusqueda);

            profesores.addAll(profesoresBD);
            tv_profesores.setItems(profesores);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    ex.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al buscar profesores",
                    "No fue posible realizar la búsqueda.",
                    Alert.AlertType.WARNING);
        }
    }

    private List<Profesor> buscarProfesoresBD(String campoBusqueda)
            throws SQLException, NullPointerException {
        if (filtroBusqueda.equals(FILTRO_NUMERO_EMPLEADO)) {
            return ProfesorServicio.buscarProfesoresPorNumeroEmpleado(
                    campoBusqueda);
        }

        if (filtroBusqueda.equals(FILTRO_NOMBRE)) {
            return ProfesorServicio.buscarProfesoresPorNombre(campoBusqueda);
        }

        return ProfesorServicio.obtenerProfesores();
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
        actualizarInformacion();
    }

    @FXML
    private void clicRegistrarProfesor(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioProfesor");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Registro de profesor");
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
    private void clicListadoProfesores(ActionEvent event) {
        cb_filtros.getSelectionModel().select(FILTRO_TODOS);
        txt_busqueda.setText("");
        filtroBusqueda = "";
        cargarInformacionProfesores();
    }

    @FXML
    private void clicRegistrarEstudiante(ActionEvent event) {
        cambiarVentana("FXMLFormularioEstudiante",
                "Registro de estudiante");
    }

    @FXML
    private void clicListadoEstudiantes(ActionEvent event) {
        cambiarVentana("FXMLListadoEstudiantes",
                "Listado de estudiantes");
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

            Stage escenario = (Stage) tv_profesores.getScene().getWindow();
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