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
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.OrganizacionVinculadaServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador para la vista de actualización de la organización
 * vinculada.
 */
public class FXMLActualizarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField txt_nombreOrganizacionVinculada;
    @FXML
    private TextField txt_correoElectronico;
    @FXML
    private TextField txt_telefono;
    @FXML
    private ComboBox<String> cb_tipoOrganizacion;
    @FXML
    private ComboBox<String> cb_estadoOrganizacion;
    @FXML
    private TextField txt_calle;
    @FXML
    private TextField txt_colonia;
    @FXML
    private TextField txt_codigoPostal;
    
    private OrganizacionVinculada organizacionVinculada;

    private final ObservableList<String> tipoOrganizacion =
            FXCollections.observableArrayList("Pública", "Privada");

    private final ObservableList<String> estadoOrganizacion =
            FXCollections.observableArrayList("Activa", "Inactiva");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb_tipoOrganizacion.setItems(tipoOrganizacion);
        cb_estadoOrganizacion.setItems(estadoOrganizacion);
    }  
    
    public void inicializarInformacionOrganizacion(
            OrganizacionVinculada organizacionVinculada) {
        if (organizacionVinculada == null) {
            return;
        }

        this.organizacionVinculada = organizacionVinculada;

        txt_nombreOrganizacionVinculada.setText(
                organizacionVinculada.getNombre()
        );

        txt_correoElectronico.setText(
                organizacionVinculada.getCorreo()
        );

        txt_telefono.setText(
                organizacionVinculada.getTelefono()
        );

        cb_tipoOrganizacion.setValue(
                organizacionVinculada.getTipo()
        );

        cb_estadoOrganizacion.setValue(
                organizacionVinculada.getEstado() ? "Activa" : "Inactiva"
        );

        txt_calle.setText(
                organizacionVinculada.getCalle()
        );

        txt_colonia.setText(
                organizacionVinculada.getColonia()
        );

        txt_codigoPostal.setText(
                organizacionVinculada.getCodigoPostal()
        );
        
    }

    @FXML
    private void clicGuardarCambios(ActionEvent event) {
        if (!estanCamposObligatoriosCompletos()){
            Utilidades.mostrarAlertaSimple("Datos faltantes", 
                    "No es posible continuar con la actualización. "
                    + "Debes ingresar datos en todos los campos. "
                    + "Ingrese los datos nuevamente para poder continuar", 
                    Alert.AlertType.WARNING);
            return;
        } 
        confirmarActualizacion();
        
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
        
        if (cb_estadoOrganizacion == null ||
                cb_estadoOrganizacion.getValue() == null) {
            return false;
        }

        return true;
    }
    
    private boolean estaVacio(TextField campo) {
        return campo == null || campo.getText() == null 
                || campo.getText().trim().isEmpty();
    }
    
    private void confirmarActualizacion(){
        boolean respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar "
                + "actualización de Organizacion Vinculada", 
                "¿Seguro que desea actualizar esta organización vinculada "
                        + "en el sistema?");
        
        if (!respuesta) {
            return;
        }
        
        actualizarOrganizacionVinculada();
        
    }
    
    private void actualizarOrganizacionVinculada() {
        try {
            OrganizacionVinculada organizacionVinculada = 
                    obtenerOrganizacionVinculada();
            RespuestaOperacion respuesta = OrganizacionVinculadaServicio.
                    actualizarOrganizacionVinculada(organizacionVinculada);
            
            if (!respuesta.getError()){
                Utilidades.mostrarAlertaSimple("Cambios guardados", 
                    respuesta.getMensaje(), 
                        Alert.AlertType.INFORMATION);
                
                OrganizacionVinculada organizacionCompleta =
                    OrganizacionVinculadaServicio.recuperarOrganizacionCompleta(
                            organizacionVinculada.
                                    getNumOrganizacionVinculada()
                    );
                cargarVistaDetalles(organizacionVinculada);
                
                
            } else {
                Utilidades.mostrarAlertaSimple("Datos inválidos", 
                    "No es posible continuar con la actualización de la "
                            + "organización.\n"
                    + "Los datos ingresados son inválidos:\n"
                    + respuesta.getMensaje() + "\nIntente nuevamente", 
                    Alert.AlertType.WARNING);
            }
            
        } catch (SQLException ex) {
           Utilidades.mostrarAlertaSimple(
                "Error", 
                   "No es posible actualizar los datos de la "
                    + "organización. Error al acceder al registro. "
                    + "Intente nuevamente", 
                   Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
           Utilidades.mostrarAlertaSimple(
                   "Error", 
                   "No fue posible obtener la información de la "
                           + "organización", 
                   Alert.AlertType.ERROR);
        }
        
    }
    
    private void cargarVistaDetalles(OrganizacionVinculada organizacionVinculada) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(
                    "FXMLInfoOrganizacionVinculada");
            Parent vista = cargador.load();
            FXMLInfoOrganizacionVinculadaController controlador = 
                    cargador.getController();
            
            OrganizacionVinculada organizacionCompleta = 
                    OrganizacionVinculadaServicio.recuperarOrganizacionCompleta(
            organizacionVinculada.getNumOrganizacionVinculada());
            
            controlador.inicializarInformacionOrganizacion(
                    organizacionCompleta);
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) txt_calle.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Información de organizacion vinculada");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException | NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar información",
                    "No se puede ver la información de la organización. "
                    + "Error al recuperar el registro de la "
                    + "organización vinculada. "
                    + "Intente nuevamente",
                    Alert.AlertType.ERROR
            );
        }
    }
    
    private OrganizacionVinculada obtenerOrganizacionVinculada(){
        OrganizacionVinculada organizacionCampos = new OrganizacionVinculada();
        
        if (this.organizacionVinculada == null) {
            throw new NullPointerException("No se recibió la "
                    + "organización vinculada a actualizar.");
        }
        
        organizacionCampos.setNumOrganizacionVinculada(
                organizacionVinculada.
                        getNumOrganizacionVinculada()
        );
        organizacionCampos.setNombre(txt_nombreOrganizacionVinculada.
                getText().trim());
        organizacionCampos.setCalle(txt_calle.getText().trim());
        organizacionCampos.setColonia(txt_colonia.getText().trim());
        organizacionCampos.setCodigoPostal(txt_codigoPostal.
                getText().trim());
        organizacionCampos.setCorreo(txt_correoElectronico.getText().trim());
        organizacionCampos.setTelefono(txt_telefono.getText().trim());
        organizacionCampos.setTipo(cb_tipoOrganizacion.getValue().trim());
        organizacionCampos.setEstado(
            "Activa".equals(cb_estadoOrganizacion.getValue())
        );
        
        return organizacionCampos;
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
    private void clicCancelar(ActionEvent event) {
        cambiarVentana("FXMLListadoOrganizacionesVinculadas", 
                "Listado de organizaciones vinculadas");
    }

    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) txt_calle.getScene().getWindow();
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
