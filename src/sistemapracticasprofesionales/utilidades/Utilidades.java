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
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase de utilidades que se van a reutilizar en distintas partes
 * del programa.
 */
public class Utilidades {
    
    /**
     * Método reuitilizable para cargar las vistas FXML desde cualquier punto. 
     * Esto permite evitar problemas con la ruta o la extensión, al estar manejado
     * siempre desde el mismo lugar.
     * 
     * @param nombreVista nombre de la vista FXML a cargar.
     * @return FXMLLoader que puede ser usado desde las clases {@code Controller}
     * para cargar las nuevas vistas y acceder al controlador de estas.
     */
    public static FXMLLoader cargarFXML(String nombreVista) {
        return new FXMLLoader(SistemaPracticasProfesionales.class.
                getResource("vista/" + nombreVista + ".fxml"));
    }
    
    /**
     * Método reutilizable para mostrar alertas simples de distintos simples
     * en distintos puntos del programa unicamente llamando al método e indicando
     * el contenido de la alerta. 
     * 
     * @param titulo titulo que tendrá la ventana emergente de la alerta.
     * @param contenido contenido que se va a mostrar al usuario.
     * @param tipo tipo de la alerta que se va a usar de acuerdo al caso en
     *              donde haya sido llamado.
     */
    public static void mostrarAlertaSimple(String titulo, String contenido, 
            Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    /**
     * Método reutilizable para mostrar alertas de confirmación en distintos
     * puntos del programa en donde la confirmación sea necesaria.
     * 
     * @param titulo titulo que tendrá la ventana emergente de la alerta.
     * @param contenido contenido que se va a mostrar al usuario.
     * @return valor boolean que indica la selección del usuario. 
     *          Retorna {@code true} si la respuesta fue OK.
     *          Retorna {@code false} si la respuesta no fue OK.
     */
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return (respuesta.get() == ButtonType.OK);
    }
    
    /**
     * Método reutilizable para cifrar la contraseña. Esto es necesario porque 
     * se busca seguridad en la base de datos y en el inicio de sesión.
     * 
     * @param contrasenia contraseña como cadena de caracteres.
     * @return arreglo de bytes que representa la contraseña cifrada.
     * @throws NoSuchAlgorithmException en caso de que no pueda ejecutar el 
     *          algoritmo de cifrado.
     */
     public static byte[] cifrarContrasenia(String contrasenia) 
             throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(contrasenia.getBytes(StandardCharsets.UTF_8));
    }
}
