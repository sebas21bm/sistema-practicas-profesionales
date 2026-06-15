/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.modelo.pojo;

/**
 *
 * @author sebas
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
