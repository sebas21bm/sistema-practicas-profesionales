package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.AsignacionServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista del la asignación de proyectos.
 */
public class FXMLAsignacionProyectoController implements Initializable {

    @FXML
    private TableView<Estudiante> tv_estudiantesSinAsignacion;
    @FXML
    private TableColumn<Estudiante, String> col_matricula;
    @FXML
    private TableColumn<Estudiante, String> col_nombre;
    @FXML
    private TableColumn<Estudiante, String> col_paterno;
    @FXML
    private TableColumn<Estudiante, String> col_materno;
    @FXML
    private TableView<ProyectoPracticas> tv_proyectosDisponibles;
    @FXML
    private TableColumn<ProyectoPracticas, String> col_proyecto;
    @FXML
    private TableColumn<ProyectoPracticas, Integer> col_cupos;
    
    private ObservableList<Estudiante> estudiantesSinAsignacion;
    private ObservableList<ProyectoPracticas> proyectosDisponibles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        cargarInformacion();
    }
    
    private void configurarTablas() {
        col_matricula.setCellValueFactory(
                new PropertyValueFactory<>("matricula"));
        col_nombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));
        col_paterno.setCellValueFactory(
                new PropertyValueFactory<>("apellidoPaterno"));
        col_materno.setCellValueFactory(
                new PropertyValueFactory<>("apellidoMaterno"));
        col_proyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));
        col_cupos.setCellValueFactory(
                new PropertyValueFactory<>("cupo"));
    }
    
    private void cargarInformacion() {
        try {
            estudiantesSinAsignacion = FXCollections.observableArrayList(
                    AsignacionServicio
                            .recuperarEstudiantesSinAsignacion());
            proyectosDisponibles = FXCollections.observableArrayList(
                    AsignacionServicio.recuperarProyectosDisponibles());
            
            tv_estudiantesSinAsignacion.setItems(estudiantesSinAsignacion);
            tv_proyectosDisponibles.setItems(proyectosDisponibles);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se puede asignar un proyecto. Hubo un error al "
                    + "recuperar el listado de estudiantes y proyectos. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información para la asignación.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tv_estudiantesSinAsignacion
                .getSelectionModel().getSelectedItem();
        ProyectoPracticas proyectoSeleccionado = tv_proyectosDisponibles
                .getSelectionModel().getSelectedItem();
        
        if (estudianteSeleccionado == null || proyectoSeleccionado == null) {
            Utilidades.mostrarAlertaSimple(
                    "Selección incompleta",
                    "Debes seleccionar un estudiante y un proyecto para "
                    + "realizar la asignación.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        confirmarAsignacion(estudianteSeleccionado, proyectoSeleccionado);
    }
    
    private void confirmarAsignacion(
            Estudiante estudiante, ProyectoPracticas proyectoPracticas) {
        
        boolean respuesta = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar asignación",
                "¿Seguro que desea asignar el proyecto seleccionado al "
                + "estudiante seleccionado?");
        
        if (respuesta) {
            asignarProyecto(estudiante, proyectoPracticas);
        }
    }
    
    private void asignarProyecto(
            Estudiante estudiante, ProyectoPracticas proyectoPracticas) {
        
        try {
            RespuestaOperacion respuesta = AsignacionServicio.asignarProyecto(
                    estudiante, proyectoPracticas);
            
            if (respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "No fue posible asignar el proyecto",
                        respuesta.getMensaje(),
                        Alert.AlertType.ERROR);
                cargarInformacion();
                return;
            }
            
            Asignacion asignacion = AsignacionServicio
                    .recuperarDetallesAsignacion(
                            proyectoPracticas.getNumProyecto(),
                            estudiante.getIdEstudiante());
            
            cargarDetallesAsignacion(asignacion);
            cargarInformacion();
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No se guardó la asignación. Hubo un problema al "
                    + "registrarla. Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información de la asignación.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDetallesAsignacion(Asignacion asignacion) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(
                    "FXMLDetallesAsignacion");
            Parent vista = cargador.load();
            
            FXMLDetallesAsignacionController controlador =
                    cargador.getController();
            controlador.inicializarInformacion(asignacion);
            
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) tv_estudiantesSinAsignacion
                    .getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Detalles de asignación");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicRegistrarProyecto(ActionEvent event) {
        cambiarVentana("FXMLRegistroProyecto", "Registro de proyecto");
    }

    @FXML
    private void clicVerListadoProyecto(ActionEvent event) {
        cambiarVentana("FXMLListadoProyectos", "Listado de proyectos");
    }

    @FXML
    private void clicGenerarOficioDeAsignacion(ActionEvent event) {
    }

    @FXML
    private void clicRegistrarOrganizacionVinculada(ActionEvent event) {
        cambiarVentana("FXMLRegistroOrganizacionVinculada",
                "Registro de organización vinculada");
    }

    @FXML
    private void clicVerListadoOrganizacionesVinculadas(ActionEvent event) {
        cambiarVentana("FXMLListadoOrganizacionesVinculadas",
                "Listado de organizaciones vinculadas");
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador", "Menu principal");
    }
    
    private void clicAsignarProyecto(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto", "Asignacion de proyectos");
    }
    
    private void clicVerListadoEstudiantes(ActionEvent event) {
        cambiarVentana("FXMLListadoEstudiantes", "Listado de estudiantes");
    }
    
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) tv_estudiantesSinAsignacion
                    .getScene().getWindow();
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