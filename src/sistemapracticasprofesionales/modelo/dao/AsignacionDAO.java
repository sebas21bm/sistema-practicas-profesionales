package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Constantes;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: DAO encargado de acceder a los datos de las asignaciones de
 * proyectos de prácticas profesionales.
 */
public class AsignacionDAO {
    
    public static ArrayList<Estudiante> obtenerEstudiantesSinAsignacion()
            throws SQLException, NullPointerException {
        
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT id_estudiante, matricula, nombre, "
                    + "paterno, materno, correo, telefono, activo "
                    + "FROM vista_estudiantes_sin_asignacion;";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentenciaBD.executeQuery();
            
            while (resultado.next()) {
                estudiantes.add(serializarEstudiante(resultado));
            }
        }
        
        return estudiantes;
    }
    
    public static ArrayList<ProyectoPracticas> obtenerProyectosDisponibles()
            throws SQLException, NullPointerException {
        
        ArrayList<ProyectoPracticas> proyectos = new ArrayList<>();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT num_proyecto, nombre, descripcion, "
                    + "cupo, fecha_finalizacion, id_responsable, disponible "
                    + "FROM vista_proyectos_disponibles;";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentenciaBD.executeQuery();
            
            while (resultado.next()) {
                proyectos.add(serializarProyectoPracticas(resultado));
            }
        }
        
        return proyectos;
    }
    
    public static RespuestaOperacion asignarProyecto(
            int numeroProyecto, int idEstudiante)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "{CALL asignar_proyecto(?, ?, ?)}";
            CallableStatement sentenciaBD = conexionBD.prepareCall(consulta);
            
            sentenciaBD.setInt(1, numeroProyecto);
            sentenciaBD.setInt(2, idEstudiante);
            sentenciaBD.setString(
                    3, Sesion.getUsuarioActual().getNombreUsuario());
            sentenciaBD.execute();
            
            respuesta.setError(false);
            respuesta.setMensaje("Proyecto asignado correctamente.");
        }
        
        return respuesta;
    }
    
    public static Asignacion obtenerDetallesAsignacion(
            int numeroProyecto, int idEstudiante)
            throws SQLException, NullPointerException {
        
        Asignacion asignacion = null;
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT num_proyecto, id_estudiante, "
                    + "id_personal_academico, nombre_proyecto, "
                    + "descripcion_proyecto, fecha_finalizacion, "
                    + "organizacion_vinculada, nombre_responsable, "
                    + "correo_responsable, nombre_estudiante, matricula, "
                    + "correo_estudiante, nombre_coordinador "
                    + "FROM vista_detalles_asignacion "
                    + "WHERE num_proyecto = ? AND id_estudiante = ?;";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            
            sentenciaBD.setInt(1, numeroProyecto);
            sentenciaBD.setInt(2, idEstudiante);
            
            ResultSet resultado = sentenciaBD.executeQuery();
            
            if (resultado.next()) {
                asignacion = serializarDetallesAsignacion(resultado);
            }
        }
        
        return asignacion;
    }
    
    private static Estudiante serializarEstudiante(ResultSet resultado)
            throws SQLException {
        
        Estudiante estudiante = new Estudiante();
        
        estudiante.setIdEstudiante(resultado.getInt("id_estudiante"));
        estudiante.setMatricula(resultado.getString("matricula"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setApellidoPaterno(resultado.getString("paterno"));
        estudiante.setApellidoMaterno(resultado.getString("materno"));
        estudiante.setCorreo(resultado.getString("correo"));
        estudiante.setTelefono(resultado.getString("telefono"));
        estudiante.setActivo(resultado.getBoolean("activo"));
        
        return estudiante;
    }
    
    private static ProyectoPracticas serializarProyectoPracticas(
            ResultSet resultado) throws SQLException {
        
        ProyectoPracticas proyectoPracticas = new ProyectoPracticas();
        
        proyectoPracticas.setNumProyecto(resultado.getInt("num_proyecto"));
        proyectoPracticas.setNombre(resultado.getString("nombre"));
        proyectoPracticas.setDescripcion(resultado.getString("descripcion"));
        proyectoPracticas.setCupo(resultado.getInt("cupo"));
        proyectoPracticas.setFechaFinalizacion(
                resultado.getDate("fecha_finalizacion"));
        proyectoPracticas.setIdResponsable(resultado.getInt("id_responsable"));
        proyectoPracticas.setDisponible(resultado.getBoolean("disponible"));
        
        return proyectoPracticas;
    }
    
    private static Asignacion serializarDetallesAsignacion(
        ResultSet resultado) throws SQLException {
    
        Asignacion asignacion = new Asignacion();

        asignacion.setNumProyecto(resultado.getInt("num_proyecto"));
        asignacion.setIdEstudiante(resultado.getInt("id_estudiante"));
        asignacion.setIdPersonalAcademico(
                resultado.getInt("id_personal_academico"));
        asignacion.setNombreProyecto(resultado.getString("nombre_proyecto"));
        asignacion.setDescripcionProyecto(
                resultado.getString("descripcion_proyecto"));
        asignacion.setFechaFinalizacion(
                resultado.getDate("fecha_finalizacion"));
        asignacion.setOrganizacionVinculada(
                resultado.getString("organizacion_vinculada"));
        asignacion.setNombreResponsable(
                resultado.getString("nombre_responsable"));
        asignacion.setCorreoResponsable(
                resultado.getString("correo_responsable"));
        asignacion.setNombreEstudiante(
                resultado.getString("nombre_estudiante"));
        asignacion.setMatricula(resultado.getString("matricula"));
        asignacion.setCorreoEstudiante(
                resultado.getString("correo_estudiante"));
        asignacion.setNombreCoordinador(
                resultado.getString("nombre_coordinador"));

        return asignacion;
    }
    
    public static ArrayList<Asignacion> obtenerAsignacionesPeriodoActual()
        throws SQLException, NullPointerException {

        ArrayList<Asignacion> asignaciones = new ArrayList<>();

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT num_proyecto, id_estudiante, estado, "
                    + "nombre_proyecto, nombre_estudiante "
                    + "FROM vista_listado_asignaciones_periodo_actual;";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentenciaBD.executeQuery();

            while (resultado.next()) {
                asignaciones.add(serializarAsignacionListado(resultado));
            }
        }

        return asignaciones;
    }
    
    private static Asignacion serializarAsignacionListado(
        ResultSet resultado) throws SQLException {
    
        Asignacion asignacion = new Asignacion();

        asignacion.setNumProyecto(resultado.getInt("num_proyecto"));
        asignacion.setIdEstudiante(resultado.getInt("id_estudiante"));
        asignacion.setEstado(resultado.getString("estado"));
        asignacion.setNombreProyecto(resultado.getString("nombre_proyecto"));
        asignacion.setNombreEstudiante(
                resultado.getString("nombre_estudiante"));

        return asignacion;
    }
        
}