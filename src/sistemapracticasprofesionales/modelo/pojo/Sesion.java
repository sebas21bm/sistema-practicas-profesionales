package sistemapracticasprofesionales.modelo.pojo;

/*
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase para almacenar al usuario que ha iniciado sesión como
 *              variable global para poder ser reutilizado para las conexiones
 *              con la BD y datos acerca del usuario.
 */
public class Sesion {

    private static Usuario usuarioActual;
    private static ExpedienteEstudiante expedienteSeleccionado;


    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuarioActual) {
        Sesion.usuarioActual = usuarioActual;
    }

    public static int getIdExperienciaEducativa() {
        if (usuarioActual == null) {
            return 0;
        }

        return usuarioActual.getIdExperienciaEducativa();
    }

    public static void setIdExperienciaEducativa(
            int idExperienciaEducativa) {
        if (usuarioActual != null) {
            usuarioActual.setIdExperienciaEducativa(
                    idExperienciaEducativa);
        }
    }
    
    public static ExpedienteEstudiante getExpedienteSeleccionado() {
        return expedienteSeleccionado;
    }

    public static void setExpedienteSeleccionado(
            ExpedienteEstudiante expedienteSeleccionado) {
        Sesion.expedienteSeleccionado = expedienteSeleccionado;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        expedienteSeleccionado = null;
    }

}