package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.Profesor;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: DAO encargado de acceder a los datos de profesores.
 */
public class ProfesorDAO {

    public static RespuestaOperacion registrarProfesor(Profesor profesor)
            throws SQLException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD = 
                ConexionBD.crearParaRol(Rol.Administrador)){
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = 
                    "{CALL registrar_profesor(?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setInt(1, profesor.getNumeroEmpleado());
            sentencia.setString(2, profesor.getNombre());
            sentencia.setString(3, profesor.getApellidoPaterno());
            sentencia.setString(4, profesor.getApellidoMaterno());
            sentencia.setString(5, profesor.getTelefono());
            sentencia.setString(6, profesor.getCorreo());
            sentencia.setString(7, profesor.getIdUsuario());
            sentencia.setString(8, profesor.getContrasenia());
            sentencia.execute();

            respuesta.setError(false);
            respuesta.setMensaje("El profesor se ha registrado correctamente.");
        }

        return respuesta;
    }

    public static ArrayList<Profesor> obtenerProfesores() throws SQLException {
        ArrayList<Profesor> profesores = new ArrayList<>();

        try (Connection conexionBD = 
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT id_personal_academico, num_empleado, "
                    + "nombre, paterno, materno, telefono, correo, activo "
                    + "FROM vista_profesores;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                profesores.add(serializarProfesor(resultado));
            }
        }

        return profesores;
    }

    private static Profesor serializarProfesor(ResultSet resultado)
            throws SQLException {
        Profesor profesor = new Profesor();
        profesor.setIdPersonalAcademico(resultado.getInt("id_personal_academico"));
        profesor.setNumeroEmpleado(resultado.getInt("num_empleado"));
        profesor.setNombre(resultado.getString("nombre"));
        profesor.setApellidoPaterno(resultado.getString("paterno"));
        profesor.setApellidoMaterno(resultado.getString("materno"));
        profesor.setTelefono(resultado.getString("telefono"));
        profesor.setCorreo(resultado.getString("correo"));
        profesor.setActivo(resultado.getBoolean("activo"));

        return profesor;
    }
}