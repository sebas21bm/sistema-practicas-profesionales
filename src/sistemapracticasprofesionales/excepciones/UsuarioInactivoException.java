package sistemapracticasprofesionales.excepciones;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Excepción personalizada que se lanza cuando el estado del usuario
 * es inactivo, lo que significa que no puede iniciar en el sistema.
 * 
 */
public class UsuarioInactivoException extends Exception {

    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
}
