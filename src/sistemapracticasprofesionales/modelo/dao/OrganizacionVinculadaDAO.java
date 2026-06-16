/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.modelo.dao;

import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Constantes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    
}
