package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
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
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.ProyectoPracticasServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del listado de proyectos.
 */
public class FXMLListadoProyectosController implements Initializable {

    @FXML
    private TableView<ProyectoPracticas> tv_proyectos;
    @FXML
    private TableColumn<ProyectoPracticas, String> col_nombre;
    @FXML
    private TableColumn<ProyectoPracticas, String> col_estudiante;
    @FXML
    private TableColumn<ProyectoPracticas, String> col_organizacionVinculada;
    @FXML
    private TableColumn<ProyectoPracticas, Date> col_fechaFinalizacion;
    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private ComboBox<?> cb_periodoEscolar;
    
    private ObservableList<ProyectoPracticas> proyectos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProyectos();
    }
    
    private void configurarTabla() {
        col_nombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));
        col_estudiante.setCellValueFactory(
                new PropertyValueFactory<>("nombreEstudiante"));
        col_organizacionVinculada.setCellValueFactory(
                new PropertyValueFactory<>("nombreOrganizacionVinculada"));
        col_fechaFinalizacion.setCellValueFactory(
                new PropertyValueFactory<>("fechaFinalizacion"));
    }
    
    private void cargarProyectos() {
        try {
            proyectos = FXCollections.observableArrayList(
                    ProyectoPracticasServicio.recuperarListadoProyectos());
            tv_proyectos.setItems(proyectos);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar proyectos",
                    "No fue posible mostrar los proyectos registrados. "
                    + "Hubo un problema al recuperar los registros. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información de los proyectos.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
        txt_nombreBusqueda.clear();
        cb_periodoEscolar.setValue(null);
        cargarProyectos();
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }

    @FXML
    private void clicRegistrarProyecto(ActionEvent event) {
        cambiarVentana("FXMLRegistroProyecto", "Registro de proyecto");
    }

    @FXML
    private void clicAsignarProyecto(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto", "Asignacion de proyectos");
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
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador", "Menu principal");
    }

    @FXML
    private void clicVerAsignaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Listado de asignaciones");
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) tv_proyectos.getScene().getWindow();
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