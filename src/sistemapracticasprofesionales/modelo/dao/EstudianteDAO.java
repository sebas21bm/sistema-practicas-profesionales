package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: DAO encargado de acceder a los datos de estudiantes.
 */
public class EstudianteDAO {

    public static RespuestaOperacion registrarEstudiante(Estudiante estudiante)
            throws SQLException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD = ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta =
                    "{CALL registrar_estudiante(?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setString(1, estudiante.getMatricula());
            sentencia.setString(2, estudiante.getNombre());
            sentencia.setString(3, estudiante.getApellidoPaterno());
            sentencia.setString(4, estudiante.getApellidoMaterno());
            sentencia.setString(5, estudiante.getCorreo());
            sentencia.setString(6, estudiante.getTelefono());
            sentencia.setString(7, estudiante.getIdUsuario());
            sentencia.setString(8, estudiante.getContrasenia());
            sentencia.execute();

            respuesta.setError(false);
            respuesta.setMensaje("El estudiante se ha registrado correctamente.");
        }

        return respuesta;
    }

    public static ArrayList<Estudiante> obtenerEstudiantes() throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        try (Connection conexionBD = ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT id_estudiante, matricula, nombre, paterno, "
                    + "materno, correo, telefono, activo "
                    + "FROM vista_estudiantes;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                estudiantes.add(serializarEstudiante(resultado));
            }
        }

        return estudiantes;
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
}