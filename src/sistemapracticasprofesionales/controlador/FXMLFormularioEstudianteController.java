/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLFormularioEstudianteController implements Initializable {

    @FXML
    private TextField txt_matricula;
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
    private ComboBox<?> cb_seccion;
    @FXML
    private ComboBox<?> cb_estado;
    @FXML
    private TextField txt_usuario;
    @FXML
    private TextField txt_contrasenia;
    @FXML
    private Label lb_error;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicGuardar(ActionEvent event) {
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
    }
    
}
