package sistemapracticasprofesionales.modelo.pojo;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase para almacenar al usuario que ha iniciado sesión como
 * variable global para poder ser reutilizado para las conexiones con la BD
 * y datos acerca del usuario.
 */
public class Sesion {
    
    private static Usuario usuarioActual;

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuarioActual) {
        Sesion.usuarioActual = usuarioActual;
    }
    
    public static void cerrarSesion() {
        usuarioActual = null;
    }
    
}
