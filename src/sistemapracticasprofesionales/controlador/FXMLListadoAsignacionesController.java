package sistemapracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.servicio.AsignacionServicio;
import sistemapracticasprofesionales.servicio.OficioAsignacionServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: Clase controladora para la vista del listado de asignaciones.
 */
public class FXMLListadoAsignacionesController implements Initializable {

    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private ComboBox<String> cb_periodoEscolar;
    @FXML
    private TableView<Asignacion> tv_asignaciones;
    @FXML
    private TableColumn<Asignacion, String> col_estudiante;
    @FXML
    private TableColumn<Asignacion, String> col_proyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarPeriodoEscolar();
        configurarBusqueda();
        cargarAsignacionesPeriodoActual();
    }
    
    private void configurarTabla() {
        col_estudiante.setCellValueFactory(
                new PropertyValueFactory<>("nombreEstudiante"));
        col_proyecto.setCellValueFactory(
                new PropertyValueFactory<>("nombreProyecto"));
    }
    
    private void configurarPeriodoEscolar() {
        cb_periodoEscolar.setDisable(true);
        cb_periodoEscolar.setPromptText("Periodo actual");
    }
    
    private void configurarBusqueda() {
        if (txt_nombreBusqueda != null) {
            txt_nombreBusqueda.setDisable(true);
            txt_nombreBusqueda.setPromptText("Búsqueda no disponible");
        }
    }
    
    private void cargarAsignacionesPeriodoActual() {
        try {
            tv_asignaciones.setItems(FXCollections.observableArrayList(
                    AsignacionServicio
                            .recuperarAsignacionesPeriodoActual()));
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar asignaciones",
                    "No se pueden mostrar las asignaciones registradas. "
                    + "Hubo un error al recuperar los registros. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al recuperar asignaciones",
                    "No fue posible recuperar la información de la sesión.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicGenerarReporteIndicadores(ActionEvent event) {
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
    private void clicAsignarProyecto(ActionEvent event) {
        cambiarVentana("FXMLAsignacionProyecto",
                "Asignación de proyectos");
    }

    @FXML
    private void clicGenerarOficioDeAsignacion(ActionEvent event) {
        cargarAsignacionesPeriodoActual();
    }

    @FXML
    private void clicRegistrarOrganizacionVinculada(ActionEvent event) {
        cambiarVentana("FXMLRegistroOrganizacionVinculada",
                "Registro de organización vinculada");
    }

    @FXML
    private void clicVerListadoOrganizacionesVinculadas(
            ActionEvent event) {
        cambiarVentana("FXMLListadoOrganizacionesVinculadas",
                "Listado de organizaciones vinculadas");
    }

    @FXML
    private void clicVerListadoEstudiantes(ActionEvent event) {
    }
    
    @FXML
    private void clicVerDetalles(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
        Sesion.cerrarSesion();
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        cargarAsignacionesPeriodoActual();
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
        if (txt_nombreBusqueda != null) {
            txt_nombreBusqueda.clear();
        }
        
        cargarAsignacionesPeriodoActual();
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cambiarVentana("FXMLInicioCoordinador", "Menú principal");
    }

    @FXML
    private void clicGenerarOficio(ActionEvent event) {
        Asignacion asignacionSeleccionada =
                tv_asignaciones.getSelectionModel().getSelectedItem();
        
        if (asignacionSeleccionada == null) {
            Utilidades.mostrarAlertaSimple(
                    "Asignación no seleccionada",
                    "Debe seleccionar una asignación para generar "
                    + "el oficio.",
                    Alert.AlertType.WARNING);
            return;
        }
        
        generarOficioAsignacion(asignacionSeleccionada);
    }

    @FXML
    private void clicCancelarAsignacion(ActionEvent event) {
    }
    
    private void generarOficioAsignacion(
            Asignacion asignacionSeleccionada) {
        try {
            Asignacion detallesAsignacion =
                    AsignacionServicio.recuperarDetallesAsignacion(
                            asignacionSeleccionada.getNumProyecto(),
                            asignacionSeleccionada.getIdEstudiante());
            
            if (detallesAsignacion == null) {
                mostrarErrorRecuperarInformacion();
                return;
            }
            
            File archivoDestino =
                    seleccionarUbicacionOficio(detallesAsignacion);
            
            if (archivoDestino == null) {
                return;
            }
            
            OficioAsignacionServicio.generarOficioAsignacion(
                    detallesAsignacion, archivoDestino);
            
            Utilidades.mostrarAlertaSimple(
                    "Oficio descargado",
                    "El oficio de asignación se ha descargado "
                    + "en tu computadora.",
                    Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            mostrarErrorRecuperarInformacion();
        } catch (IllegalArgumentException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al generar oficio",
                    "No se pudo crear el oficio de asignación. "
                    + "Ocurrió un problema al llenar el formato. "
                    + "Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (IOException | SecurityException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al descargar oficio",
                    "No se logró descargar el oficio de asignación. "
                    + "Ocurrió un problema con la descarga.",
                    Alert.AlertType.ERROR);
        }
    }
    
    private File seleccionarUbicacionOficio(Asignacion asignacion) {
        FileChooser selectorArchivo = new FileChooser();
        
        selectorArchivo.setTitle("Guardar oficio de asignación");
        selectorArchivo.setInitialFileName(
                crearNombreArchivoSugerido(asignacion));
        selectorArchivo.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        
        return selectorArchivo.showSaveDialog(
                tv_asignaciones.getScene().getWindow());
    }
    
    private String crearNombreArchivoSugerido(Asignacion asignacion) {
        String matricula = asignacion.getMatricula();
        
        if (matricula == null || matricula.trim().isEmpty()) {
            matricula = "estudiante";
        }
        
        return "oficio_asignacion_" + matricula + ".pdf";
    }
    
    private void mostrarErrorRecuperarInformacion() {
        Utilidades.mostrarAlertaSimple(
                "Error al recuperar información",
                "No se pudo recuperar toda la información de los "
                + "elementos que contiene el oficio de asignación. "
                + "Hubo un problema con sus registros. "
                + "Intente nuevamente.",
                Alert.AlertType.ERROR);
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario = (Stage) tv_asignaciones.getScene()
                    .getWindow();
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