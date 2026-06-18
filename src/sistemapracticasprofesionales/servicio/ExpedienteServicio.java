package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar reglas para consulta de
 *              expedientes.
 */
public class ExpedienteServicio {

    public static ArrayList<ExpedienteEstudiante>
            obtenerExpedientesEstudiantesProfesor(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        validarExperienciaEducativa(idExperienciaEducativa);

        return ExpedienteDAO.obtenerExpedientesEstudiantesProfesor(
                idExperienciaEducativa);
    }

    private static void validarExperienciaEducativa(
            int idExperienciaEducativa) {
        if (idExperienciaEducativa <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la experiencia educativa.");
        }
    }
}
