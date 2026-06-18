package sistemapracticasprofesionales.servicio;

import java.io.File;
import java.io.IOException;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.utilidades.ExportadorOficioAsignacion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: Servicio encargado de validar y solicitar la generación del
 * oficio de asignación.
 */
public class OficioAsignacionServicio {
    
    public static void generarOficioAsignacion(
            Asignacion asignacion, File archivoDestino)
            throws IOException, IllegalArgumentException {
        
        validarDatosOficio(asignacion, archivoDestino);
        ExportadorOficioAsignacion.generarOficio(
                asignacion, archivoDestino);
    }
    
    private static void validarDatosOficio(
            Asignacion asignacion, File archivoDestino) {
        
        if (asignacion == null) {
            throw new IllegalArgumentException(
                    "No se recibió la información de la asignación.");
        }
        
        if (archivoDestino == null) {
            throw new IllegalArgumentException(
                    "No se recibió la ubicación de descarga.");
        }
        
        validarTexto(asignacion.getNombreEstudiante(),
                "No se encontró el nombre del estudiante.");
        validarTexto(asignacion.getMatricula(),
                "No se encontró la matrícula del estudiante.");
        validarTexto(asignacion.getCorreoEstudiante(),
                "No se encontró el correo del estudiante.");
        validarTexto(asignacion.getNombreProyecto(),
                "No se encontró el nombre del proyecto.");
        validarTexto(asignacion.getDescripcionProyecto(),
                "No se encontró la descripción del proyecto.");
        validarTexto(asignacion.getOrganizacionVinculada(),
                "No se encontró la organización vinculada.");
        validarTexto(asignacion.getNombreResponsable(),
                "No se encontró el responsable del proyecto.");
        validarTexto(asignacion.getCorreoResponsable(),
                "No se encontró el correo del responsable.");
        validarTexto(asignacion.getNombreCoordinador(),
                "No se encontró el nombre del coordinador.");
    }
    
    private static void validarTexto(String texto, String mensaje) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}