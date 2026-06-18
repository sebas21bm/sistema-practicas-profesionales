/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.modelo.dao;

import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.*;
import sistemapracticasprofesionales.utilidades.Constantes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: DAO encargado de acceder a los datos de 
 * las Organizaciones vinculadas.
 */
public class OrganizacionVinculadaDAO {
    
    public static RespuestaOperacion registrarOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "INSERT INTO organizacion_vinculada "
                    + "(nombre, calle, colonia, codigo_postal, telefono,"
                    + "correo, tipo) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            sentenciaBD.setString(1, organizacionVinculada.getNombre());
            sentenciaBD.setString(2, organizacionVinculada.getCalle());
            sentenciaBD.setString(3, organizacionVinculada.getColonia());
            sentenciaBD.setString(4, organizacionVinculada.getCodigoPostal());
            sentenciaBD.setString(5, organizacionVinculada.getTelefono());
            sentenciaBD.setString(6, organizacionVinculada.getCorreo());
            sentenciaBD.setString(7, organizacionVinculada.getTipo());
            
            sentenciaBD.executeUpdate();
            
            respuesta.setError(false);
            respuesta.setMensaje("Organización vinculada registrada "
                        + "correctamente");
        }
        
        return respuesta;
    }
    
    public static boolean existeOrganizacionVinculadaIgual(
            OrganizacionVinculada organizacionVinculada)
            throws SQLException, NullPointerException {

        boolean existe = false;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT COUNT(*) AS total "
                    + "FROM organizacion_vinculada "
                    + "WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(calle)) = LOWER(TRIM(?)) "
                    + "AND LOWER(TRIM(colonia)) = LOWER(TRIM(?)) "
                    + "AND codigo_postal = ? "
                    + "AND telefono = ? "
                    + "AND LOWER(TRIM(correo)) = LOWER(TRIM(?)) "
                    + "AND tipo = ?;";

            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            sentenciaBD.setString(1, organizacionVinculada.getNombre());
            sentenciaBD.setString(2, organizacionVinculada.getCalle());
            sentenciaBD.setString(3, organizacionVinculada.getColonia());
            sentenciaBD.setString(4, organizacionVinculada.getCodigoPostal());
            sentenciaBD.setString(5, organizacionVinculada.getTelefono());
            sentenciaBD.setString(6, organizacionVinculada.getCorreo());
            sentenciaBD.setString(7, organizacionVinculada.getTipo());

            ResultSet resultado = sentenciaBD.executeQuery();

            if (resultado.next()) {
                existe = resultado.getInt("total") > 0;
            }
        }

        return existe;
    }
    
    public static ArrayList<OrganizacionVinculada> obtenerOrganizaciones() 
        throws SQLException, NullPointerException {
        
        ArrayList<OrganizacionVinculada> organizaciones = new ArrayList<>();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT num_organizacion_vinculada, nombre, "
                    + "telefono, correo, tipo FROM "
                    + "vista_organizaciones_activas;";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentenciaBD.executeQuery();
            
            while (resultado.next()){
                organizaciones.add(serializarOrganizacionVinculadaListado(resultado));
            }
        }
        return organizaciones;
    }
    
    public static OrganizacionVinculada obtenerOrganizacionCompleta(
            int numeroOrganizacion) throws SQLException, NullPointerException {
        
        OrganizacionVinculada organizacionVinculada = null;
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "SELECT num_organizacion_vinculada, nombre,"
                    + "calle, colonia, codigo_postal, telefono, correo,"
                    + "tipo, estado FROM organizacion_vinculada "
                    + "WHERE num_organizacion_vinculada = ?";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            sentenciaBD.setInt(1, numeroOrganizacion);
            ResultSet resultado = sentenciaBD.executeQuery();
            if (resultado.next()) {
                organizacionVinculada = serializarOrganizacionCompleta(resultado);
                cargarDatosCompletos(conexionBD, organizacionVinculada);
            }
            
        }
        return organizacionVinculada;
    }
    
    private static void cargarDatosCompletos(Connection conexionBD,
            OrganizacionVinculada organizacionVinculada) 
            throws SQLException, NullPointerException{
        
        if (organizacionVinculada == null) {
            throw new NullPointerException("No se recibió la organización vinculada");
        }
        
        int numeroOrganizacion = organizacionVinculada.getNumOrganizacionVinculada();
    
        List<ResponsableProyecto> responsables =
                obtenerResponsablesPorOrganizacion(conexionBD, numeroOrganizacion);

        List<ProyectoPracticas> proyectos =
                obtenerProyectosPorOrganizacion(conexionBD, numeroOrganizacion);

        organizacionVinculada.setResponsables(responsables);
        organizacionVinculada.setProyectos(proyectos);
        
    }
    
    private static List<ResponsableProyecto> obtenerResponsablesPorOrganizacion(
        Connection conexionBD,
        int numeroOrganizacion) throws SQLException {
    
        List<ResponsableProyecto> responsables = new ArrayList<>();

        String consulta = "SELECT id_responsable, nombre, paterno, materno, "
                + "telefono, correo, puesto, num_organizacion_vinculada "
                + "FROM vista_responsables_organizacion "
                + "WHERE num_organizacion_vinculada = ?";

        PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
        sentenciaBD.setInt(1, numeroOrganizacion);

        ResultSet resultado = sentenciaBD.executeQuery();

        while (resultado.next()) {
            responsables.add(serializarResponsableProyecto(resultado));
        }

        return responsables;
    }
    
    private static ResponsableProyecto serializarResponsableProyecto(
        ResultSet resultado) throws SQLException {
    
        ResponsableProyecto responsable = new ResponsableProyecto();

        responsable.setIdResponsable(resultado.getInt("id_responsable"));
        responsable.setNombre(resultado.getString("nombre"));
        responsable.setPaterno(resultado.getString("paterno"));
        responsable.setMaterno(resultado.getString("materno"));
        responsable.setTelefono(resultado.getString("telefono"));
        responsable.setCorreo(resultado.getString("correo"));
        responsable.setPuesto(resultado.getString("puesto"));
        responsable.setNumOrganizacionVinculada(
                resultado.getInt("num_organizacion_vinculada"));

        return responsable;
    }
    
    private static List<ProyectoPracticas> obtenerProyectosPorOrganizacion(
        Connection conexionBD,
        int numeroOrganizacion) throws SQLException {
    
        List<ProyectoPracticas> proyectos = new ArrayList<>();

        String consulta = "SELECT num_proyecto, nombre, num_organizacion_vinculada "
                + "FROM vista_proyectos_organizacion "
                + "WHERE num_organizacion_vinculada = ?";

        PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
        sentenciaBD.setInt(1, numeroOrganizacion);

        ResultSet resultado = sentenciaBD.executeQuery();

        while (resultado.next()) {
            proyectos.add(serializarProyectoPracticasListado(resultado));
        }

        return proyectos;
    }
    
    private static ProyectoPracticas serializarProyectoPracticasListado(
        ResultSet resultado) throws SQLException {
    
        ProyectoPracticas proyecto = new ProyectoPracticas();

        proyecto.setNumProyecto(resultado.getInt("num_proyecto"));
        proyecto.setNombre(resultado.getString("nombre"));

        return proyecto;
    }

    private static OrganizacionVinculada serializarOrganizacionCompleta
        (ResultSet resultado) throws SQLException, NullPointerException {
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        
        organizacionVinculada.setNumOrganizacionVinculada(
resultado.getInt("num_organizacion_vinculada"));
        organizacionVinculada.setNombre(resultado.
                getString("nombre"));
        organizacionVinculada.setCalle(resultado.
                getString("calle"));
        organizacionVinculada.setColonia(resultado.
                getString("colonia"));
        organizacionVinculada.setCodigoPostal(resultado.
                getString("codigo_postal"));
        organizacionVinculada.setTelefono(resultado.getString("telefono"));
        organizacionVinculada.setCorreo(resultado.getString("correo"));
        organizacionVinculada.setTipo(resultado.getString("tipo"));
        organizacionVinculada.setEstado(resultado.getBoolean("estado"));
        
        return organizacionVinculada;
    }
        
    private static OrganizacionVinculada serializarOrganizacionVinculadaListado(
            ResultSet resultado) throws SQLException, NullPointerException {
        
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        
        organizacionVinculada.setNumOrganizacionVinculada(
resultado.getInt("num_organizacion_vinculada"));
        organizacionVinculada.setNombre(resultado.getString("nombre"));
        organizacionVinculada.setTelefono(resultado.getString("telefono"));
        organizacionVinculada.setCorreo(resultado.getString("correo"));
        organizacionVinculada.setTipo(resultado.getString("tipo"));
        
        return organizacionVinculada;
    }
    
    public static RespuestaOperacion actualizarOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada) 
            throws SQLException, NullPointerException{
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        
        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            String consulta = "UPDATE organizacion_vinculada "
                    + "SET nombre = ?, calle = ?, colonia = ? , "
                    + "codigo_postal = ?, telefono = ?, correo = ?, "
                    + "tipo = ?, estado = ? "
                    + "WHERE num_organizacion_vinculada = ?;";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            sentenciaBD.setString(1, organizacionVinculada.getNombre());
            sentenciaBD.setString(2, organizacionVinculada.getCalle());
            sentenciaBD.setString(3, organizacionVinculada.getColonia());
            sentenciaBD.setString(4, organizacionVinculada.getCodigoPostal());
            sentenciaBD.setString(5, organizacionVinculada.getTelefono());
            sentenciaBD.setString(6, organizacionVinculada.getCorreo());
            sentenciaBD.setString(7, organizacionVinculada.getTipo());
            sentenciaBD.setBoolean(8, organizacionVinculada.getEstado());
            sentenciaBD.setInt(9, organizacionVinculada.getNumOrganizacionVinculada());
            
            int filasAfectadas = sentenciaBD.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró la organización vinculada a actualizar.");
            }
            
            respuesta.setError(false);
            respuesta.setMensaje("Cambios guardados con éxito");
        }
        
        return respuesta;
    }
    
}
