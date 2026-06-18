package sistemapracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemapracticasprofesionales.excepciones.UsuarioNoEncontradoException;
import sistemapracticasprofesionales.excepciones.UsuarioInactivoException;
import sistemapracticasprofesionales.modelo.dao.AutenticacionDAO;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.modelo.pojo.Usuario;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Controlador para la vista del inicio de sesión.
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private PasswordField psw_contrasenia;
    @FXML
    private TextField txt_usuario;
    @FXML
    private Label lb_errorUsuario;
    @FXML
    private Label lb_errorContrasenia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String usuario = txt_usuario.getText();
        String contrasenia = psw_contrasenia.getText();
        
        if (!validarCampos(usuario, contrasenia)){
            return;
        }
        validarCredenciales(usuario, contrasenia);
        txt_usuario.clear();
        psw_contrasenia.clear();
        
    }
    
    private boolean validarCampos(String usuario, String contrasenia) {
        lb_errorUsuario.setText("");
        lb_errorContrasenia.setText("");
        boolean valido = true;
        
        if (usuario.trim().isEmpty()) {
            lb_errorUsuario.setText("Falta insertar usuario");
            valido = false;
        }

        if (contrasenia.trim().isEmpty()) {
            lb_errorContrasenia.setText("Falta insertar contraseña");
            valido = false;
        }

        return valido;
    }
    
    private void validarCredenciales(String usuario, String contrasenia) {
        try {
            Usuario usuarioLogin = AutenticacionDAO.validarSesionUsuario(usuario, contrasenia);
            
            
            Utilidades.mostrarAlertaSimple("Bienvenido(a)",
                    "Bienvenido al sistema: " + usuarioLogin.getNombreReal(),
                    Alert.AlertType.INFORMATION);
            
            Sesion.setUsuarioActual(usuarioLogin);
            irPantallaPrincipal();
            
        } catch (SQLException | NoSuchAlgorithmException | IOException ex) {
            Utilidades.mostrarAlertaSimple("ERROR", 
                    ex.getMessage(), 
                    Alert.AlertType.ERROR);
        } catch (UsuarioInactivoException ex) {
            Utilidades.mostrarAlertaSimple(
                "Usuario inactivo",
                ex.getMessage(),
                Alert.AlertType.WARNING);
        } catch (UsuarioNoEncontradoException ex) {
            Utilidades.mostrarAlertaSimple("Error", 
                    ex.getMessage(), 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void irPantallaPrincipal() throws IOException{
        String rutaMenu = cargarEscenaSegunRol(Sesion.
                getUsuarioActual().getRolUsuario());
        FXMLLoader cargador = Utilidades.cargarFXML(rutaMenu);
        Parent vista = cargador.load();
        Scene escena = new Scene(vista);
        
        Stage escenario = (Stage) txt_usuario.getScene().getWindow();
        escenario.setScene(escena);
        escenario.setTitle("Menu principal");
        escenario.centerOnScreen();
        escenario.show();
    }
    
    private String cargarEscenaSegunRol(Rol rolUsuario) {
        if (rolUsuario == null) {
            return null;
        }

        switch (rolUsuario) {
            case Estudiante:
                return "FXMLInicioEstudiante";
            case Profesor:
                return "FXMLInicioProfesor";
            case Coordinador:
                return "FXMLInicioCoordinador";
            case Administrador:
                return "FXMLInicioAdministrador";
            default:
                return null;
        }
    }
    
}
