package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador para la ventana de informacion de la organización
 * vinculada.
 */
public class FXMLInfoOrganizacionVinculadaController implements Initializable {
    
    @FXML
    private Label lb_nombreOrganizacion;
    @FXML
    private Label lb_estado;
    @FXML
    private Label lb_ubicacion;
    @FXML
    private Label lb_telefono;
    @FXML
    private Label lb_correo;
    @FXML
    private Label lb_tipo;
    @FXML
    private TableView<ProyectoPracticas> tv_proyectos;
    @FXML
    private TableColumn<ProyectoPracticas, String> col_nombreProyecto;
    @FXML
    private TableView<ResponsableProyecto> tv_responsablesProyecto;
    @FXML
    private TableColumn<ResponsableProyecto, String> col_correo;
    @FXML
    private TableColumn<ResponsableProyecto, String> 
            col_nombreCompletoResponsable;
    
    private OrganizacionVinculada organizacionDetalles;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnasTablas();
    }
    
    private void configurarColumnasTablas() {
        col_nombreProyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        col_nombreCompletoResponsable.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto"));

        col_correo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));
    }

    public void inicializarInformacionOrganizacion(
            OrganizacionVinculada organizacionVinculada) {
        
        if (organizacionVinculada == null) {
            return;
        }

        this.organizacionDetalles = organizacionVinculada;

        lb_nombreOrganizacion.setText(organizacionVinculada.getNombre());
        lb_estado.setText(organizacionVinculada.getEstado() 
                ? "Activa" : "Inactiva");
        lb_ubicacion.setText(organizacionVinculada.getUbicacionCompleta());
        lb_telefono.setText(organizacionVinculada.getTelefono());
        lb_correo.setText(organizacionVinculada.getCorreo());
        lb_tipo.setText(organizacionVinculada.getTipo());

        tv_proyectos.setItems(FXCollections.observableArrayList(
                organizacionVinculada.getProyectos()));

        tv_responsablesProyecto.setItems(FXCollections.observableArrayList(
                organizacionVinculada.getResponsables()));
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
        cargarVistaActualizacion();
    }
    
    private void cargarVistaActualizacion() {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(
                    "FXMLActualizarOrganizacionVinculada");
            Parent vista = cargador.load();
            FXMLActualizarOrganizacionVinculadaController controlador = 
                    cargador.getController();
            controlador.inicializarInformacionOrganizacion(
                    organizacionDetalles);
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) lb_correo.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Actualizar organizacion vinculada");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicRegistrarProyecto(ActionEvent event) {
        cambiarVentana("FXMLRegistroProyecto", 
                "Registro de proyecto");
    }

    @FXML
    private void clicVerListadoProyecto(ActionEvent event) {
        cambiarVentana("FXMLListadoProyectos", 
                "Listado de proyectos");
    }

    @FXML
    private void clicAsignarProyecto(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto", 
                "Asignacion de proyectos");
    }

    @FXML
    private void clicVerAsignaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Listado de asignaciones");
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
        cambiarVentana("FXMLListadoOrganizacionesVinculadas", 
                "Listado de organizaciones vinculadas");
    }
    
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) lb_correo.getScene().getWindow();
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
