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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemapracticasprofesionales.modelo.pojo.Profesor;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLListadoProfesoresController implements Initializable {

    @FXML
    private TableView<Profesor> tv_profesores;
    @FXML
    private TableColumn<?, ?> col_nombre;
    @FXML
    private TableColumn<?, ?> col_telefono;
    @FXML
    private TableColumn<?, ?> col_correo;
    @FXML
    private TableColumn<?, ?> col_seccion;
    @FXML
    private TextField txt_nombreBusqueda;
    @FXML
    private TableColumn<?, ?> col_apellidoPaterno;
    @FXML
    private TableColumn<?, ?> col_apellidoMaterno;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @FXML
    private void clicVerTodos(ActionEvent event) {
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
    }

    
    //Navegacion

    @FXML
    private void clicRegistrarCoordinador(ActionEvent event) {
    }

    @FXML
    private void clicListadoCoordinadores(ActionEvent event) {
    }

    @FXML
    private void clilcRegistrarProfesor(ActionEvent event) {
    }

    @FXML
    private void clilcListadoCoordinadores(ActionEvent event) {
    }

    @FXML
    private void clilcRegistrarEstudiante(ActionEvent event) {
    }

    @FXML
    private void clilcListadoEstudiantes(ActionEvent event) {
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clilcRegresar(ActionEvent event) {
    }
    
}
