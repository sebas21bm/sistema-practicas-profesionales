package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.OrganizacionVinculadaServicio;
import sistemapracticasprofesionales.servicio.ProyectoPracticasServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase controladora para la vista del registro de proyecto.
 */
public class FXMLRegistroProyectoController implements Initializable {

    @FXML
    private TextField txt_nombreProyecto;
    @FXML
    private TextField txt_descripcion;
    @FXML
    private DatePicker dp_fechaFinalizacion;
    @FXML
    private TextField txt_cupo;
    @FXML
    private ComboBox<OrganizacionVinculada> cb_organizacionVinculada;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_apellidoPaterno;
    @FXML
    private TextField txt_apellidoMaterno;
    @FXML
    private TextField txt_telefono;
    @FXML
    private TextField txt_correo;
    @FXML
    private TextField txt_puesto;
    @FXML
    private RadioButton rbtn_nuevoResponsable;
    @FXML
    private ToggleGroup tg_seleccionResponsable;
    @FXML
    private RadioButton rbtn_responsableExistente;
    @FXML
    private ComboBox<ResponsableProyecto> cb_responsableExistente;
    
    private ObservableList<OrganizacionVinculada> organizacionesVinculadas;
    private ObservableList<ResponsableProyecto> responsablesProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarVistaInicial();
        cargarOrganizacionesVinculadas();
        configurarEventos();
    }
    
    private void configurarVistaInicial() {
        rbtn_nuevoResponsable.setSelected(true);
        cb_responsableExistente.setDisable(true);
    }
    
    private void configurarEventos() {
        rbtn_nuevoResponsable.setOnAction(event -> actualizarModoResponsable());
        rbtn_responsableExistente.setOnAction(
                event -> actualizarModoResponsable());
        
        cb_organizacionVinculada.valueProperty().addListener(
                (observable, valorAnterior, valorNuevo) -> {
                    if (rbtn_responsableExistente.isSelected()) {
                        cargarResponsablesPorOrganizacion();
                    }
                });
    }
    
    private void actualizarModoResponsable() {
        boolean responsableNuevo = rbtn_nuevoResponsable.isSelected();
        
        txt_nombre.setDisable(!responsableNuevo);
        txt_apellidoPaterno.setDisable(!responsableNuevo);
        txt_apellidoMaterno.setDisable(!responsableNuevo);
        txt_telefono.setDisable(!responsableNuevo);
        txt_correo.setDisable(!responsableNuevo);
        txt_puesto.setDisable(!responsableNuevo);
        cb_responsableExistente.setDisable(responsableNuevo);
        
        if (responsableNuevo) {
            cb_responsableExistente.setValue(null);
        } else {
            limpiarCamposResponsableNuevo();
            cargarResponsablesPorOrganizacion();
        }
    }
    
    private void cargarOrganizacionesVinculadas() {
        try {
            organizacionesVinculadas = FXCollections.observableArrayList(
                    OrganizacionVinculadaServicio
                            .recuperarListadoOrganizacionesVinculadas());
            cb_organizacionVinculada.setItems(organizacionesVinculadas);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar organizaciones",
                    "No fue posible mostrar las organizaciones registradas. "
                    + "Hubo un problema al recuperar los registros. "
                    + "Intente nuevamente o registre una nueva organización.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información de las "
                    + "organizaciones vinculadas.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void cargarResponsablesPorOrganizacion() {
        OrganizacionVinculada organizacionSeleccionada =
                cb_organizacionVinculada.getValue();
        
        cb_responsableExistente.setValue(null);
        
        if (organizacionSeleccionada == null) {
            cb_responsableExistente.setItems(
                    FXCollections.observableArrayList());
            return;
        }
        
        try {
            responsablesProyecto = FXCollections.observableArrayList(
                    ProyectoPracticasServicio
                            .recuperarResponsablesPorOrganizacion(
                                    organizacionSeleccionada
                                            .getNumOrganizacionVinculada()));
            cb_responsableExistente.setItems(responsablesProyecto);
            
            if (responsablesProyecto.isEmpty()) {
                Utilidades.mostrarAlertaSimple(
                        "Sin responsables registrados",
                        "La organización seleccionada no tiene responsables "
                        + "registrados. Debes capturar un responsable nuevo.",
                        Alert.AlertType.INFORMATION);
                
                rbtn_nuevoResponsable.setSelected(true);
                actualizarModoResponsable();
            }
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar responsables",
                    "No fue posible mostrar los responsables de la "
                    + "organización seleccionada. Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información de los responsables.",
                    Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador", "Menu principal");
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (!estanCamposObligatoriosCompletos()) {
            Utilidades.mostrarAlertaSimple(
                    "Datos faltantes",
                    "No es posible continuar con el registro. Debes ingresar "
                    + "datos en todos los campos, con la excepción de apellido "
                    + "materno. Ingrese los datos nuevamente para poder "
                    + "continuar.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        confirmarRegistro();
    }
    
    @FXML
    private void clicNuevaOrganizacionVinculada(ActionEvent event) {
        cambiarVentanaRegistroOrganizacion();
    }
    
    private boolean estanCamposObligatoriosCompletos() {
        if (estaVacio(txt_nombreProyecto) || estaVacio(txt_descripcion)
                || estaVacio(txt_cupo)) {
            return false;
        }
        
        if (dp_fechaFinalizacion.getValue() == null) {
            return false;
        }
        
        if (cb_organizacionVinculada.getValue() == null) {
            return false;
        }
        
        if (rbtn_nuevoResponsable.isSelected()) {
            return estanCamposResponsableNuevoCompletos();
        }
        
        return cb_responsableExistente.getValue() != null;
    }
    
    private boolean estanCamposResponsableNuevoCompletos() {
        return !estaVacio(txt_nombre) && !estaVacio(txt_apellidoPaterno)
                && !estaVacio(txt_telefono) && !estaVacio(txt_correo)
                && !estaVacio(txt_puesto);
    }
    
    private boolean estaVacio(TextField campoTexto) {
        return campoTexto == null || campoTexto.getText() == null
                || campoTexto.getText().trim().isEmpty();
    }
    
    private void confirmarRegistro() {
        boolean respuesta = Utilidades.mostrarAlertaConfirmacion(
                "Confirmar registro de proyecto",
                "¿Seguro que desea registrar este proyecto?");
        
        if (respuesta) {
            guardarProyecto();
        }
    }
    
    private void guardarProyecto() {
        try {
            RespuestaOperacion respuesta;
            
            if (rbtn_nuevoResponsable.isSelected()) {
                ProyectoPracticas proyectoPracticas = obtenerProyectoPracticas();
                ResponsableProyecto responsableProyecto =
                        obtenerResponsableProyectoNuevo();
                
                respuesta = ProyectoPracticasServicio
                        .registrarProyectoConResponsableNuevo(
                                proyectoPracticas, responsableProyecto);
            } else {
                ProyectoPracticas proyectoPracticas =
                        obtenerProyectoConResponsableExistente();
                
                respuesta = ProyectoPracticasServicio
                        .registrarProyectoConResponsableExistente(
                                proyectoPracticas);
            }
            
            mostrarResultadoRegistro(respuesta);
        } catch (NumberFormatException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Cupo inválido",
                    "El cupo debe ser un número entero mayor o igual a 1.",
                    Alert.AlertType.WARNING);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No es posible registrar el Proyecto. Error al guardar "
                    + "el registro. Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información del proyecto.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarResultadoRegistro(RespuestaOperacion respuesta) {
        if (!respuesta.getError()) {
            Utilidades.mostrarAlertaSimple(
                    "Proyecto registrado correctamente",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION);
            limpiarCampos();
        } else {
            Utilidades.mostrarAlertaSimple(
                    "Datos inválidos",
                    "No es posible continuar con el registro del proyecto. "
                    + "Los datos ingresados son inválidos:\n"
                    + respuesta.getMensaje()
                    + "\nIntente nuevamente.",
                    Alert.AlertType.WARNING);
        }
    }
    
    private ProyectoPracticas obtenerProyectoPracticas() {
        ProyectoPracticas proyectoPracticas = new ProyectoPracticas();
        OrganizacionVinculada organizacionSeleccionada =
                cb_organizacionVinculada.getValue();
        
        proyectoPracticas.setNombre(txt_nombreProyecto.getText());
        proyectoPracticas.setDescripcion(txt_descripcion.getText());
        proyectoPracticas.setCupo(Integer.parseInt(txt_cupo.getText().trim()));
        proyectoPracticas.setFechaFinalizacion(obtenerFechaFinalizacion());
        proyectoPracticas.setOrganizacionVinculada(organizacionSeleccionada);
        proyectoPracticas.setDisponible(true);
        
        return proyectoPracticas;
    }
    
    private ProyectoPracticas obtenerProyectoConResponsableExistente() {
        ProyectoPracticas proyectoPracticas = obtenerProyectoPracticas();
        ResponsableProyecto responsableSeleccionado =
                cb_responsableExistente.getValue();
        
        proyectoPracticas.setIdResponsable(
                responsableSeleccionado.getIdResponsable());
        proyectoPracticas.setResponsableProyecto(responsableSeleccionado);
        
        return proyectoPracticas;
    }
    
    private ResponsableProyecto obtenerResponsableProyectoNuevo() {
        ResponsableProyecto responsableProyecto = new ResponsableProyecto();
        OrganizacionVinculada organizacionSeleccionada =
                cb_organizacionVinculada.getValue();
        
        responsableProyecto.setNombre(txt_nombre.getText());
        responsableProyecto.setPaterno(txt_apellidoPaterno.getText());
        responsableProyecto.setMaterno(txt_apellidoMaterno.getText());
        responsableProyecto.setTelefono(txt_telefono.getText());
        responsableProyecto.setCorreo(txt_correo.getText());
        responsableProyecto.setPuesto(txt_puesto.getText());
        responsableProyecto.setNumOrganizacionVinculada(
                organizacionSeleccionada.getNumOrganizacionVinculada());
        
        return responsableProyecto;
    }
    
    private Date obtenerFechaFinalizacion() {
        LocalDate fechaFinalizacion = dp_fechaFinalizacion.getValue();
        
        return Date.from(fechaFinalizacion.atStartOfDay(
                ZoneId.systemDefault()).toInstant());
    }
    
    private void limpiarCampos() {
        txt_nombreProyecto.clear();
        txt_descripcion.clear();
        txt_cupo.clear();
        dp_fechaFinalizacion.setValue(null);
        cb_organizacionVinculada.setValue(null);
        cb_responsableExistente.setValue(null);
        limpiarCamposResponsableNuevo();
        rbtn_nuevoResponsable.setSelected(true);
        actualizarModoResponsable();
    }
    
    private void limpiarCamposResponsableNuevo() {
        txt_nombre.clear();
        txt_apellidoPaterno.clear();
        txt_apellidoMaterno.clear();
        txt_telefono.clear();
        txt_correo.clear();
        txt_puesto.clear();
    }
    
    private void cambiarVentanaRegistroOrganizacion() {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(
                    "FXMLRegistroOrganizacionVinculada");
            Parent vista = cargador.load();
            
            FXMLRegistroOrganizacionVinculadaController controlador =
                    cargador.getController();
            controlador.inicializarInformacionRegreso(
                    "FXMLRegistroProyecto", "Registro de proyecto");
            
            Scene escena = new Scene(vista);
            Stage escenario = (Stage) txt_cupo.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle("Registro de organización vinculada");
            escenario.setResizable(false);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVerListadoProyecto(ActionEvent event) {
        cambiarVentana("FXMLListadoProyectos", "Listado de proyectos");
    }
    
    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }


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
    private void clicVerAsignaciones(ActionEvent event) {
        cambiarVentana("FXMLListadoAsignaciones",
                "Listado de asignaciones");
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) txt_cupo.getScene().getWindow();
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