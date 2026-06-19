package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Constantes;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: DAO encargado de acceder a los datos de los proyectos de
 * prácticas profesionales.
 */
public class ProyectoPracticasDAO {
    
    public static RespuestaOperacion registrarProyectoConResponsableNuevo(
            ProyectoPracticas proyectoPracticas,
            ResponsableProyecto responsableProyecto)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "{CALL registrar_proyecto_responsable_nuevo("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement sentenciaBD =
                    conexionBD.prepareCall(consulta);
            
            sentenciaBD.setString(1, proyectoPracticas.
                    getNombre());
            sentenciaBD.setString(2, proyectoPracticas.
                    getDescripcion());
            sentenciaBD.setInt(3, proyectoPracticas.getCupo());
            sentenciaBD.setDate(4, new Date(proyectoPracticas
                    .getFechaFinalizacion().getTime()));
            sentenciaBD.setString(5, responsableProyecto.
                    getNombre());
            sentenciaBD.setString(6, responsableProyecto.
                    getPaterno());
            sentenciaBD.setString(7, responsableProyecto.
                    getMaterno());
            sentenciaBD.setString(8, responsableProyecto.
                    getTelefono());
            sentenciaBD.setString(9, responsableProyecto.
                    getCorreo());
            sentenciaBD.setString(10, responsableProyecto.
                    getPuesto());
            sentenciaBD.setInt(11, responsableProyecto
                    .getNumOrganizacionVinculada());
            
            sentenciaBD.executeUpdate();
            
            respuesta.setError(false);
            respuesta.setMensaje("Proyecto registrado correctamente");
        }
        
        return respuesta;
    }
    
    public static RespuestaOperacion registrarProyectoConResponsableExistente(
            ProyectoPracticas proyectoPracticas)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "INSERT INTO proyecto_practicas "
                    + "(nombre, descripcion, cupo, fecha_finalizacion, "
                    + "id_responsable, disponible) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            
            sentenciaBD.setString(1, proyectoPracticas.
                    getNombre());
            sentenciaBD.setString(2, proyectoPracticas.
                    getDescripcion());
            sentenciaBD.setInt(3, proyectoPracticas.
                    getCupo());
            sentenciaBD.setDate(4, new Date(proyectoPracticas
                    .getFechaFinalizacion().getTime()));
            sentenciaBD.setInt(5, proyectoPracticas.
                    getIdResponsable());
            sentenciaBD.setBoolean(6, proyectoPracticas.
                    isDisponible());
            
            sentenciaBD.executeUpdate();
            
            respuesta.setError(false);
            respuesta.setMensaje("Proyecto registrado correctamente");
        }
        
        return respuesta;
    }
    
    public static ArrayList<ResponsableProyecto>
            obtenerResponsablesPorOrganizacion(
            int numeroOrganizacion) throws SQLException, NullPointerException {
        
        ArrayList<ResponsableProyecto> responsables = new ArrayList<>();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT id_responsable, nombre, paterno, "
                    + "materno, telefono, correo, puesto, "
                    + "num_organizacion_vinculada "
                    + "FROM vista_responsables_organizacion "
                    + "WHERE num_organizacion_vinculada = ?;";
            PreparedStatement sentenciaBD =
                    conexionBD.prepareStatement(consulta);
            
            sentenciaBD.setInt(1, numeroOrganizacion);
            
            ResultSet resultado = sentenciaBD.executeQuery();
            
            while (resultado.next()) {
                responsables.add(serializarResponsableProyecto(resultado));
            }
        }
        
        return responsables;
    }
    
    private static ResponsableProyecto serializarResponsableProyecto(
            ResultSet resultado) throws SQLException {
        
        ResponsableProyecto responsableProyecto = new ResponsableProyecto();
        
        responsableProyecto.setIdResponsable(
                resultado.getInt("id_responsable"));
        responsableProyecto.setNombre(resultado.getString(
                "nombre"));
        responsableProyecto.setPaterno(resultado.getString(
                "paterno"));
        responsableProyecto.setMaterno(resultado.getString(
                "materno"));
        responsableProyecto.setTelefono(resultado.getString(
                "telefono"));
        responsableProyecto.setCorreo(resultado.getString(
                "correo"));
        responsableProyecto.setPuesto(resultado.getString(
                "puesto"));
        responsableProyecto.setNumOrganizacionVinculada(
                resultado.getInt(
                        "num_organizacion_vinculada"));
        
        return responsableProyecto;
    }
    
    public static ArrayList<ProyectoPracticas> obtenerProyectos()
        throws SQLException, NullPointerException {
    
    ArrayList<ProyectoPracticas> proyectos = new ArrayList<>();
    
    try (Connection conexionBD = ConexionBD.crearParaRol(
            Sesion.getUsuarioActual().getRolUsuario())) {
        
        if (conexionBD == null) {
            throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
        }
        
        String consulta = "SELECT num_proyecto, nombre, estudiante, "
                + "organizacion_vinculada, fecha_finalizacion "
                + "FROM vista_listado_proyectos;";
        PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
        ResultSet resultado = sentenciaBD.executeQuery();
        
        while (resultado.next()) {
            proyectos.add(serializarProyectoListado(resultado));
        }
    }
    
    return proyectos;
}

    private static ProyectoPracticas serializarProyectoListado(
            ResultSet resultado) throws SQLException {

        ProyectoPracticas proyectoPracticas = new ProyectoPracticas();

        proyectoPracticas.setNumProyecto(resultado.getInt(
                "num_proyecto"));
        proyectoPracticas.setNombre(resultado.getString(
                "nombre"));
        proyectoPracticas.setNombreEstudiante(resultado.getString(
                "estudiante"));
        proyectoPracticas.setNombreOrganizacionVinculada(
                resultado.getString(
                        "organizacion_vinculada"));
        proyectoPracticas.setFechaFinalizacion(
                resultado.getDate(
                        "fecha_finalizacion"));

        return proyectoPracticas;
    }
    
    public static boolean existeResponsableIgual(
            ResponsableProyecto responsableProyecto)
            throws SQLException, NullPointerException {

        boolean existe = false;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT COUNT(*) AS total "
                    + "FROM responsable "
                    + "WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(paterno)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(IFNULL(materno, ''))) = "
                    + "LOWER(TRIM(?)) "
                    + "AND telefono = ? "
                    + "AND LOWER(TRIM(correo)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(puesto)) = LOWER(TRIM(?)) "
                    + "AND num_organizacion_vinculada = ?;";

            PreparedStatement sentenciaBD = conexionBD.prepareStatement(
                    consulta);
            sentenciaBD.setString(1, responsableProyecto.
                    getNombre());
            sentenciaBD.setString(2, responsableProyecto.
                    getPaterno());
            sentenciaBD.setString(3, responsableProyecto.
                    getMaterno());
            sentenciaBD.setString(4, responsableProyecto.
                    getTelefono());
            sentenciaBD.setString(5, responsableProyecto.
                    getCorreo());
            sentenciaBD.setString(6, responsableProyecto.
                    getPuesto());
            sentenciaBD.setInt(7,
                    responsableProyecto.getNumOrganizacionVinculada());

            ResultSet resultado = sentenciaBD.executeQuery();

            if (resultado.next()) {
                existe = resultado.getInt("total") > 0;
            }
        }

        return existe;
    }
    
    public static boolean existeProyectoIgual(
            ProyectoPracticas proyectoPracticas)
            throws SQLException, NullPointerException {

        boolean existe = false;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT COUNT(*) AS total "
                    + "FROM proyecto_practicas "
                    + "WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(descripcion)) = LOWER(TRIM(?)) "
                    + "AND cupo = ? "
                    + "AND fecha_finalizacion = ? "
                    + "AND id_responsable = ?;";

            PreparedStatement sentenciaBD = conexionBD.prepareStatement(
                    consulta);
            sentenciaBD.setString(1, proyectoPracticas.
                    getNombre());
            sentenciaBD.setString(2, proyectoPracticas.
                    getDescripcion());
            sentenciaBD.setInt(3, proyectoPracticas.getCupo());
            sentenciaBD.setDate(4, new Date(proyectoPracticas
                    .getFechaFinalizacion().getTime()));
            sentenciaBD.setInt(5, proyectoPracticas.
                    getIdResponsable());

            ResultSet resultado = sentenciaBD.executeQuery();

            if (resultado.next()) {
                existe = resultado.getInt("total") > 0;
            }
        }

        return existe;
    }
    
    public static boolean existeProyectoIgualEnOrganizacion(
            ProyectoPracticas proyectoPracticas, int numeroOrganizacionVinculada)
            throws SQLException, NullPointerException {

        boolean existe = false;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT COUNT(*) AS total "
                    + "FROM proyecto_practicas pp "
                    + "INNER JOIN responsable r "
                    + "ON pp.id_responsable = r.id_responsable "
                    + "WHERE LOWER(TRIM(pp.nombre)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(pp.descripcion)) = LOWER(TRIM(?)) "
                    + "AND pp.cupo = ? "
                    + "AND pp.fecha_finalizacion = ? "
                    + "AND r.num_organizacion_vinculada = ?;";

            PreparedStatement sentenciaBD = conexionBD.prepareStatement(
                    consulta);
            sentenciaBD.setString(1, proyectoPracticas.
                    getNombre());
            sentenciaBD.setString(2, proyectoPracticas.
                    getDescripcion());
            sentenciaBD.setInt(3, proyectoPracticas.getCupo());
            sentenciaBD.setDate(4, new Date(proyectoPracticas
                    .getFechaFinalizacion().getTime()));
            sentenciaBD.setInt(5, numeroOrganizacionVinculada);

            ResultSet resultado = sentenciaBD.executeQuery();

            if (resultado.next()) {
                existe = resultado.getInt("total") > 0;
            }
        }

        return existe;
    }
}