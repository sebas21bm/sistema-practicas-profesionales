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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.OrganizacionVinculadaServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista del listado de
 * organizaciones vinculadas
 */
public class FXMLListadoOrganizacionesVinculadasController implements Initializable {

    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private TableView<OrganizacionVinculada> tv_organizacionesVinculadas;
    @FXML
    private TableColumn<OrganizacionVinculada, String> col_nombreOrganizacion;
    @FXML
    private TableColumn<OrganizacionVinculada, String> col_telefono;
    @FXML
    private TableColumn<OrganizacionVinculada, String> col_correo;
    @FXML
    private TableColumn<OrganizacionVinculada, String> col_tipo;
    
    private ObservableList<OrganizacionVinculada> organizaciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaOrganizacionesVinculadas();
        cargarRegistrosOrganizacionesVinculadas();
    }
    
    private void configurarTablaOrganizacionesVinculadas() {
        col_nombreOrganizacion.setCellValueFactory(
            new PropertyValueFactory<>("nombre"));
        col_telefono.setCellValueFactory(
            new PropertyValueFactory<>("telefono"));
        col_correo.setCellValueFactory(
            new PropertyValueFactory<>("correo"));
        col_tipo.setCellValueFactory(
            new PropertyValueFactory<>("tipo"));
    }
    
    private void cargarRegistrosOrganizacionesVinculadas() {
        try {
            organizaciones = FXCollections.observableArrayList();
            List<OrganizacionVinculada> organizacionesBD = 
                OrganizacionVinculadaServicio.
                        recuperarListadoOrganizacionesVinculadas();
            organizaciones.addAll(organizacionesBD);
            tv_organizacionesVinculadas.setItems(organizaciones);
            
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al consultar",
                    "No se pueden mostrar las organizaciones. "
                    + "Error al recuperar sus registros. "
                    + "Intente nuevamente",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al cargar organizaciones",
                    "Lo sentimos, la información de las organizaciones "
                    + "no puede ser cargada en este momento.",
                    Alert.AlertType.ERROR);
        }
        
    }
    
    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
        cargarRegistrosOrganizacionesVinculadas();
        
    }

    @FXML
    private void clicVerDetalles(ActionEvent event) {
        
        OrganizacionVinculada organizacionSeleccionada = 
                tv_organizacionesVinculadas.getSelectionModel().getSelectedItem();
        
        if (organizacionSeleccionada == null) {
            Utilidades.mostrarAlertaSimple(
                    "Organización no seleccionada",
                    "Debes seleccionar una organización vinculada "
                            + "para ver sus detalles.",
                    Alert.AlertType.WARNING
            );
            return;
        }
        
        try {
            OrganizacionVinculada organizacionCompleta = 
                    OrganizacionVinculadaServicio.
                    recuperarOrganizacionCompleta(
                    organizacionSeleccionada.
                                    getNumOrganizacionVinculada());
            if (organizacionCompleta == null) {
                Utilidades.mostrarAlertaSimple("Error", 
                    "No se puede ver la información de la organización. "
                    + "Error al recuperar el registro de la "
                            + "OrganizacionVinculada. "
                    + "Intente nuevamente", Alert.AlertType.ERROR);
            } else {
                cargarVistaDetalles(organizacionCompleta);
            }
        } catch (SQLException | NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar información",
                    "No se puede ver la información de la organización. "
                    + "Error al recuperar el registro de la organización "
                            + "vinculada. "
                    + "Intente nuevamente",
                    Alert.AlertType.ERROR
            );
        }
    }
    
    private void cargarVistaDetalles(OrganizacionVinculada organizacionVinculada) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(
                    "FXMLInfoOrganizacionVinculada");
            Parent vista = cargador.load();
            FXMLInfoOrganizacionVinculadaController controlador = 
                    cargador.getController();
            controlador.inicializarInformacionOrganizacion(organizacionVinculada);
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) txt_nombreBusqueda.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Información de organizacion vinculada");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //Navegacion
    
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
    private void clicRegistrarOrganizacionVinculada(ActionEvent event) {
        cambiarVentana("FXMLRegistroOrganizacionVinculada",
                "Registro de organización vinculada");
    }


    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");

    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador",
                "Menu principal");
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

            Stage escenario = (Stage) txt_nombreBusqueda.getScene().getWindow();
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
