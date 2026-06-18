package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.AsignacionDAO;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: Servicio encargado de validar y solicitar las operaciones de
 * asignación de proyectos de prácticas profesionales.
 */
public class AsignacionServicio {
    
    public static ArrayList<Estudiante> recuperarEstudiantesSinAsignacion()
            throws SQLException, NullPointerException {
        
        return AsignacionDAO.obtenerEstudiantesSinAsignacion();
    }
    
    public static ArrayList<ProyectoPracticas> recuperarProyectosDisponibles()
            throws SQLException, NullPointerException {
        
        return AsignacionDAO.obtenerProyectosDisponibles();
    }
    
    public static RespuestaOperacion asignarProyecto(
            Estudiante estudiante, ProyectoPracticas proyectoPracticas)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        if (estudiante == null) {
            respuesta.setError(true);
            respuesta.setMensaje("Debes seleccionar un estudiante.");
            return respuesta;
        }
        
        if (proyectoPracticas == null) {
            respuesta.setError(true);
            respuesta.setMensaje("Debes seleccionar un proyecto.");
            return respuesta;
        }
        
        if (proyectoPracticas.getCupo() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("No se pudo seleccionar el proyecto. "
                    + "El proyecto se quedó sin cupos. Seleccione otro proyecto");
            return respuesta;
        }
        
        return AsignacionDAO.asignarProyecto(
                proyectoPracticas.getNumProyecto(),
                estudiante.getIdEstudiante());
    }
    
    public static ArrayList<Asignacion> recuperarAsignacionesPeriodoActual()
        throws SQLException, NullPointerException {
    
    return AsignacionDAO.obtenerAsignacionesPeriodoActual();
}

    public static Asignacion recuperarDetallesAsignacion(
            int numeroProyecto, int idEstudiante)
            throws SQLException, NullPointerException {

        if (numeroProyecto <= 0 || idEstudiante <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la asignación seleccionada.");
        }

        return AsignacionDAO.obtenerDetallesAsignacion(
                numeroProyecto, idEstudiante);
    }
}