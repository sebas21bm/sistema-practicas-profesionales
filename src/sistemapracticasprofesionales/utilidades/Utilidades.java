/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.utilidades;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sistemapracticasprofesionales.SistemaPracticasProfesionales;

/**
 *
 * @author sebas
 */
public class Utilidades {
    
    public static FXMLLoader cargarFXML(String nombreVista) {
        return new FXMLLoader(SistemaPracticasProfesionales.class.
                getResource("vista/" + nombreVista + ".fxml"));
    }
    
    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return (respuesta.get() == ButtonType.OK);
    }
    
        
     public static byte[] cifrarContrasenia(String contrasenia) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(contrasenia.getBytes(StandardCharsets.UTF_8));
    }
}
