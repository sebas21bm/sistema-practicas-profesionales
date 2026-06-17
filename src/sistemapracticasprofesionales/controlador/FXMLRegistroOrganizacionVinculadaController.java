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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.servicio.OrganizacionVinculadaServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;
import sistemapracticasprofesionales.modelo.pojo.Sesion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Clase controladora para la vista del registro de
 * organizaciones vinculadas
 */
public class FXMLRegistroOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField txt_nombreOrganizacionVinculada;
    @FXML
    private TextField txt_correoElectronico;
    @FXML
    private ComboBox<String> cb_tipoOrganizacion;
    @FXML
    private TextField txt_calle;
    @FXML
    private TextField txt_colonia;
    @FXML
    private TextField txt_codigoPostal;
    @FXML
    private TextField txt_telefono;
    
    private final ObservableList<String> tipoOrganizacion = 
            FXCollections.observableArrayList("Pública", "Privada");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb_tipoOrganizacion.setItems(tipoOrganizacion);
    }    
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador",
                "Menu principal");
    }
    
    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (!estanCamposObligatoriosCompletos()){
            Utilidades.mostrarAlertaSimple("Datos faltantes", 
                    "No es posible continuar con el registro. "
                    + "Debes ingresar datos en todos los campos. "
                    + "Ingrese los datos nuevamente para poder continuar", 
                    Alert.AlertType.WARNING);
            return;
        } 
        confirmarRegistro();
    }
    
    private boolean estanCamposObligatoriosCompletos() {
        if (estaVacio(txt_nombreOrganizacionVinculada)) {
            return false;
        }

        if (estaVacio(txt_correoElectronico)) {
            return false;
        }

        if (estaVacio(txt_telefono)) {
            return false;
        }

        if (estaVacio(txt_calle)) {
            return false;
        }

        if (estaVacio(txt_colonia)) {
            return false;
        }

        if (estaVacio(txt_codigoPostal)) {
            return false;
        }

        if (cb_tipoOrganizacion == null || 
                cb_tipoOrganizacion.getValue() == null) {
            return false;
        }

        return true;
    }
    
    private boolean estaVacio(TextField campo) {
        return campo == null || campo.getText() == null 
                || campo.getText().trim().isEmpty();
    }
    
    private void confirmarRegistro(){
        boolean respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar "
                + "registro de Organizacion Vinculada", 
                "¿Seguro que desea registrar esta organización "
                        + "vinculada en el sistema?");
        
        if (!respuesta) {
            return;
        }
        
        guardarOrganizacionVinculada();
        
    }
    
    private void guardarOrganizacionVinculada(){
        try {
            OrganizacionVinculada organizacionVinculada = 
                    obtenerOrganizacionVinculada();
            RespuestaOperacion respuesta = OrganizacionVinculadaServicio.
                    registrarOrganizacionVinculada(organizacionVinculada);
            
            if (!respuesta.getError()){
                Utilidades.mostrarAlertaSimple("Organizacion vinculada"
                        + "registrada correctamente", respuesta.getMensaje(), 
                        Alert.AlertType.INFORMATION);
                limpiarCampos();
            } else {
                Utilidades.mostrarAlertaSimple("Datos invalidos", 
                    "No es posible continuar con el registro de la "
                    + "organización.\nLos sigueintes datos ingresados fueron inválidos: "
                    + respuesta.getMensaje() + "\nIntente nuevamente", 
                    Alert.AlertType.WARNING);
            }
            
        } catch (SQLException ex) {
           Utilidades.mostrarAlertaSimple(
                   "Error", "No es posible registrar la organización. "
                        + "Error al guardar el registro. Intente nuevamente.", 
                   Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
           Utilidades.mostrarAlertaSimple(
                   "Error", 
                   "No fue posible obtener la informacion de la organizacion", 
                   Alert.AlertType.ERROR);
        }
        
    }
    
    private OrganizacionVinculada obtenerOrganizacionVinculada(){
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        
        organizacionVinculada.setNombre(txt_nombreOrganizacionVinculada.
                getText().trim());
        organizacionVinculada.setCalle(txt_calle.getText().trim());
        organizacionVinculada.setColonia(txt_colonia.getText().trim());
        organizacionVinculada.setCodigoPostal(txt_codigoPostal.
                getText().trim());
        organizacionVinculada.setCorreo(txt_correoElectronico.getText().trim());
        organizacionVinculada.setTelefono(txt_telefono.getText().trim());
        organizacionVinculada.setTipo(cb_tipoOrganizacion.getValue().trim());
        
        return organizacionVinculada;
    }
    
    private void limpiarCampos(){
        txt_nombreOrganizacionVinculada.clear();
        txt_calle.clear();
        txt_colonia.clear();
        txt_codigoPostal.clear();
        txt_correoElectronico.clear();
        txt_telefono.clear();
        cb_tipoOrganizacion.setValue(null);
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
    private void clicVerListadoOrganizacionesVinculadas(ActionEvent event) {
        cambiarVentana("FXMLListadoOrganizacionesVinculadas",
                "Listado de organizaciones vinculadas");
    }
    
    @FXML
    private void clicVerAsignaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Listado de asignaciones");
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }
      
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario =
                    (Stage) txt_nombreOrganizacionVinculada.getScene().getWindow();
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
