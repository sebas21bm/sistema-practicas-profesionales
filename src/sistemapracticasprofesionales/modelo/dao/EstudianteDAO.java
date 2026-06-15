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

    public static RespuestaOperacion registrarEstudiante(
            Estudiante estudiante) throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL registrar_estudiante(?, ?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

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
                respuesta.setMensaje(
                        "El estudiante se ha registrado correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<Estudiante> obtenerEstudiantes()
            throws SQLException, NullPointerException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_estudiante, matricula, nombre, paterno, "
                        + "materno, correo, telefono, activo "
                        + "FROM vista_estudiantes;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    Estudiante estudiante = serializarEstudiante(resultado);
                    estudiantes.add(estudiante);
                }

                return estudiantes;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static boolean verificarMatriculaExistente(String matricula)
            throws SQLException, NullPointerException {
        int encontrados = 0;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT COUNT(*) FROM estudiante "
                        + "WHERE matricula = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, matricula);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    encontrados = resultado.getInt(1);
                }

                return encontrados > 0;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static boolean verificarCorreoEstudianteExistente(String correo)
            throws SQLException, NullPointerException {
        int encontrados = 0;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT COUNT(*) FROM estudiante "
                        + "WHERE LOWER(correo) = LOWER(?);";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, correo);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    encontrados = resultado.getInt(1);
                }

                return encontrados > 0;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static boolean verificarUsuarioExistente(String idUsuario)
            throws SQLException, NullPointerException {
        int encontrados = 0;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT COUNT(*) FROM cuenta "
                        + "WHERE id_usuario = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, idUsuario);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    encontrados = resultado.getInt(1);
                }

                return encontrados > 0;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static Estudiante serializarEstudiante(ResultSet resultado)
            throws SQLException, NullPointerException {
        Estudiante estudiante = new Estudiante();

        estudiante.setIdEstudiante(resultado.getInt("id_estudiante"));
        estudiante.setMatricula(resultado.getString("matricula"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setApellidoPaterno(resultado.getString("paterno"));
        estudiante.setApellidoMaterno(
                resultado.getString("materno") != null
                ? resultado.getString("materno") : "");
        estudiante.setCorreo(resultado.getString("correo"));
        estudiante.setTelefono(resultado.getString("telefono"));
        estudiante.setActivo(resultado.getBoolean("activo"));

        return estudiante;
    }
    
    public static ArrayList<Estudiante> buscarEstudiantesPorMatricula(
            String matricula) throws SQLException, NullPointerException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_estudiante, matricula, nombre, paterno, "
                        + "materno, correo, telefono, activo "
                        + "FROM vista_estudiantes "
                        + "WHERE matricula LIKE ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, "%" + matricula + "%");
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    estudiantes.add(serializarEstudiante(resultado));
                }

                return estudiantes;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<Estudiante> buscarEstudiantesPorNombre(String nombre)
            throws SQLException, NullPointerException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_estudiante, matricula, nombre, paterno, "
                        + "materno, correo, telefono, activo "
                        + "FROM vista_estudiantes "
                        + "WHERE LOWER(CONCAT(nombre, ' ', paterno, ' ', "
                        + "IFNULL(materno, ''))) LIKE LOWER(?);";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, "%" + nombre + "%");
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    estudiantes.add(serializarEstudiante(resultado));
                }

                return estudiantes;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }
}