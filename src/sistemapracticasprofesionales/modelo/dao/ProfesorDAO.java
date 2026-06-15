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
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL registrar_profesor(?, ?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setString(1, profesor.getNumeroEmpleado());
                sentencia.setString(2, profesor.getNombre());
                sentencia.setString(3, profesor.getApellidoPaterno());
                sentencia.setString(4, profesor.getApellidoMaterno());
                sentencia.setString(5, profesor.getTelefono());
                sentencia.setString(6, profesor.getCorreo());
                sentencia.setString(7, profesor.getIdUsuario());
                sentencia.setString(8, profesor.getContrasenia());
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "El profesor se ha registrado correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<Profesor> obtenerProfesores()
            throws SQLException, NullPointerException {
        ArrayList<Profesor> profesores = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_personal_academico, num_empleado, "
                        + "nombre, paterno, materno, telefono, correo, "
                        + "activo FROM vista_profesores;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    Profesor profesor = serializarProfesor(resultado);
                    profesores.add(profesor);
                }

                return profesores;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static boolean verificarNumeroEmpleadoExistente(
            String numeroEmpleado) throws SQLException, NullPointerException {
        int encontrados = 0;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT COUNT(*) FROM personal_academico pa "
                        + "JOIN personal_academico_has_puesto php "
                        + "ON pa.id_personal_academico = "
                        + "php.id_personal_academico "
                        + "JOIN puesto p ON php.id_puesto = p.id_puesto "
                        + "WHERE pa.num_empleado = ? "
                        + "AND p.puesto = 'Profesor';";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setString(1, numeroEmpleado);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    encontrados = resultado.getInt(1);
                }

                return encontrados > 0;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static boolean verificarCorreoProfesorExistente(String correo)
            throws SQLException, NullPointerException {
        int encontrados = 0;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Administrador)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT COUNT(*) FROM personal_academico pa "
                        + "JOIN personal_academico_has_puesto php "
                        + "ON pa.id_personal_academico = "
                        + "php.id_personal_academico "
                        + "JOIN puesto p ON php.id_puesto = p.id_puesto "
                        + "WHERE LOWER(pa.correo) = LOWER(?) "
                        + "AND p.puesto = 'Profesor';";
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
    
    public static ArrayList<Profesor> buscarProfesoresPorNumeroEmpleado(
        String numeroEmpleado) throws SQLException, NullPointerException {
    ArrayList<Profesor> profesores = new ArrayList<>();

    try (Connection conexionBD =
            ConexionBD.crearParaRol(Rol.Administrador)) {
        if (conexionBD != null) {
            String consulta =
                    "SELECT id_personal_academico, num_empleado, nombre, "
                    + "paterno, materno, telefono, correo, activo "
                    + "FROM vista_profesores "
                    + "WHERE num_empleado LIKE ?;";
            PreparedStatement sentencia =
                    conexionBD.prepareStatement(consulta);

            sentencia.setString(1, numeroEmpleado);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                profesores.add(serializarProfesor(resultado));
            }

            return profesores;
        }
    }

    throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
}

public static ArrayList<Profesor> buscarProfesoresPorNombre(String nombre)
        throws SQLException, NullPointerException {
    ArrayList<Profesor> profesores = new ArrayList<>();

    try (Connection conexionBD =
            ConexionBD.crearParaRol(Rol.Administrador)) {
        if (conexionBD != null) {
            String consulta =
                    "SELECT id_personal_academico, num_empleado, nombre, "
                    + "paterno, materno, telefono, correo, activo "
                    + "FROM vista_profesores "
                    + "WHERE LOWER(CONCAT(nombre, ' ', paterno, ' ', "
                    + "IFNULL(materno, ''))) LIKE LOWER(?);";
            PreparedStatement sentencia =
                    conexionBD.prepareStatement(consulta);

            sentencia.setString(1, "%" + nombre + "%");
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                profesores.add(serializarProfesor(resultado));
            }

            return profesores;
        }
    }

    throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
}

    private static Profesor serializarProfesor(ResultSet resultado)
            throws SQLException, NullPointerException {
        Profesor profesor = new Profesor();

        profesor.setIdPersonalAcademico(
                resultado.getInt("id_personal_academico"));
        profesor.setNumeroEmpleado(resultado.getString("num_empleado"));
        profesor.setNombre(resultado.getString("nombre"));
        profesor.setApellidoPaterno(resultado.getString("paterno"));
        profesor.setApellidoMaterno(
                resultado.getString("materno") != null
                ? resultado.getString("materno") : "");
        profesor.setTelefono(resultado.getString("telefono"));
        profesor.setCorreo(resultado.getString("correo"));
        profesor.setActivo(resultado.getBoolean("activo"));

        return profesor;
    }
}