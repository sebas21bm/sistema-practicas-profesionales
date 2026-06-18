package sistemapracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: DAO encargado de consultar expedientes de estudiantes.
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
                String consulta =
                        "SELECT e.id_expediente, e.completo, "
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
                        + "i.id_experiencia_educativa "
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
}