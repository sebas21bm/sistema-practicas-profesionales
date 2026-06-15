package sistemapracticasprofesionales.excepciones;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Excepción personalizada que se lanza cuando no se encuentra
 * ningún registro que coincida con el usuario y contraseña ingresado en el
 * inicio de sesión.
 */
public class UsuarioNoEncontradoException extends RuntimeException {
    
    public UsuarioNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
