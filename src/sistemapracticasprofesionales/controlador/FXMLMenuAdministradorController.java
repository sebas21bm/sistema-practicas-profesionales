/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLMenuAdministradorController implements Initializable {

    @FXML
    private Label lb_nombre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicRegistrarCoordinador(ActionEvent event) {
        cambiarVentana("FXMLFormularioCoordinador",
                "Registro de coordinador");
    }

    @FXML
    private void clicVerListadoCoordinadores(ActionEvent event) {
        cambiarVentana("FXMLListadoCoordinadores",
                "Listado de coordinadores");
    }

    @FXML
    private void clicRegistrarProfesor(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioProfesor");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage stageFormulario = new Stage();
            stageFormulario.setScene(escena);
            stageFormulario.setTitle("Registro de profesor");
            stageFormulario.setResizable(false);
            stageFormulario.centerOnScreen();
            stageFormulario.initModality(Modality.APPLICATION_MODAL);
            stageFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVerListadoProfesores(ActionEvent event) {
        cambiarVentana("FXMLListadoProfesores",
                "Listado de profesores");
    }

    @FXML
    private void clicRegistrarEstudiante(ActionEvent event) {
        try {
            FXMLLoader cargador =
                    Utilidades.cargarFXML("FXMLFormularioEstudiante");
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage stageFormulario = new Stage();
            stageFormulario.setScene(escena);
            stageFormulario.setTitle("Registro de estudiante");
            stageFormulario.setResizable(false);
            stageFormulario.centerOnScreen();
            stageFormulario.initModality(Modality.APPLICATION_MODAL);
            stageFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVerListadoEstudiantes(ActionEvent event) {
        cambiarVentana("FXMLListadoEstudiantes",
                "Listado de estudiantes");
    }

    @FXML
    private void clicCerrarSesión(ActionEvent event) {
        cambiarVentana("FXMLInicioSesion", "Inicio de sesión");
    }
    
    private void cambiarVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader cargador = Utilidades.cargarFXML(nombreVista);
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);

            Stage escenario =
                    (Stage) lb_nombre.getScene().getWindow();
            escenario.setScene(escena);
            escenario.setTitle(titulo);
            escenario.centerOnScreen();
            escenario.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
