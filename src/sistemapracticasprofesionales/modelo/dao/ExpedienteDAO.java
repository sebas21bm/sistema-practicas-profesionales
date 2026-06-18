package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: DAO encargado de consultar y actualizar expedientes de
 *              estudiantes.
 */
public class ExpedienteDAO {

    public static ArrayList<ExpedienteEstudiante>
            obtenerExpedientesEstudiantesProfesor(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        ArrayList<ExpedienteEstudiante> expedientes =
                new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta = obtenerConsultaBase()
                        + "WHERE e.id_experiencia_educativa = ? "
                        + "ORDER BY es.matricula;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExperienciaEducativa);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    expedientes.add(
                            serializarExpedienteEstudiante(resultado));
                }

                return expedientes;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<ExpedienteEstudiante>
            buscarExpedientesEstudiantesProfesor(
            int idExperienciaEducativa, String filtro, String criterio)
            throws SQLException, NullPointerException {
        ArrayList<ExpedienteEstudiante> expedientes =
                new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta = obtenerConsultaBusqueda(filtro);
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExperienciaEducativa);
                sentencia.setString(2, "%" + criterio + "%");

                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    expedientes.add(
                            serializarExpedienteEstudiante(resultado));
                }

                return expedientes;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ExpedienteEstudiante obtenerExpedienteEstudianteProfesorPorId(
            int idExpediente, int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        ExpedienteEstudiante expediente = null;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta = obtenerConsultaBase()
                        + "WHERE e.id_expediente = ? "
                        + "AND e.id_experiencia_educativa = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExpediente);
                sentencia.setInt(2, idExperienciaEducativa);

                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    expediente =
                            serializarExpedienteEstudiante(resultado);
                }

                return expediente;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static RespuestaOperacion calcularCalificacionExpediente(
            int idExpediente)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL calcular_calificacion_expediente(?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setInt(1, idExpediente);
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "La calificación final se calculó "
                        + "correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static String obtenerConsultaBase() {
        return "SELECT e.id_expediente, e.completo, "
                + "e.num_proyecto, e.id_estudiante, "
                + "e.id_experiencia_educativa, es.matricula, "
                + "CONCAT(es.nombre, ' ', es.paterno, ' ', "
                + "IFNULL(es.materno, '')) AS nombre_estudiante, "
                + "pp.nombre AS nombre_proyecto, "
                + "i.calificacion "
                + "FROM expediente e "
                + "JOIN estudiante es "
                + "ON e.id_estudiante = es.id_estudiante "
                + "LEFT JOIN proyecto_practicas pp "
                + "ON e.num_proyecto = pp.num_proyecto "
                + "LEFT JOIN inscripcion i "
                + "ON e.id_estudiante = i.id_estudiante "
                + "AND e.id_experiencia_educativa = "
                + "i.id_experiencia_educativa ";
    }

    private static String obtenerConsultaBusqueda(String filtro) {
        String condicionBusqueda;

        if ("Nombre".equals(filtro)) {
            condicionBusqueda =
                    "AND CONCAT(es.nombre, ' ', es.paterno, ' ', "
                    + "IFNULL(es.materno, '')) LIKE ? ";
        } else {
            condicionBusqueda = "AND es.matricula LIKE ? ";
        }

        return obtenerConsultaBase()
                + "WHERE e.id_experiencia_educativa = ? "
                + condicionBusqueda
                + "ORDER BY es.matricula;";
    }

    private static ExpedienteEstudiante serializarExpedienteEstudiante(
            ResultSet resultado) throws SQLException {
        ExpedienteEstudiante expediente = new ExpedienteEstudiante();
        double calificacion;

        expediente.setIdExpediente(resultado.getInt("id_expediente"));
        expediente.setCompleto(resultado.getBoolean("completo"));
        expediente.setNumProyecto(resultado.getInt("num_proyecto"));
        expediente.setIdEstudiante(resultado.getInt("id_estudiante"));
        expediente.setIdExperienciaEducativa(
                resultado.getInt("id_experiencia_educativa"));
        expediente.setMatricula(resultado.getString("matricula"));
        expediente.setNombreEstudiante(
                resultado.getString("nombre_estudiante"));
        expediente.setNombreProyecto(
                resultado.getString("nombre_proyecto"));

        calificacion = resultado.getDouble("calificacion");
        if (resultado.wasNull()) {
            expediente.setCalificacion(null);
        } else {
            expediente.setCalificacion(calificacion);
        }

        return expediente;
    }
    
    public static ExpedienteEstudiante obtenerExpedientePropio(
        String idUsuario) throws SQLException, NullPointerException {

        ExpedienteEstudiante expedienteEstudiante = null;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {

            if (conexionBD == null) {
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }

            String consulta = "SELECT id_expediente, completo, num_proyecto, "
                    + "id_estudiante, id_experiencia_educativa, matricula, "
                    + "nombre_estudiante, nombre_proyecto, calificacion "
                    + "FROM vista_expediente_propio_estudiante "
                    + "WHERE id_usuario = ?;";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);

            sentenciaBD.setString(1, idUsuario);

            ResultSet resultado = sentenciaBD.executeQuery();

            if (resultado.next()) {
                expedienteEstudiante =
                        serializarExpedienteEstudiante(resultado);
            }
        }

        return expedienteEstudiante;
    }
}