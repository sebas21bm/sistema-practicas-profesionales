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

    private static final String FILTRO_MATRICULA = "Matrícula";
    private static final String FILTRO_NOMBRE = "Nombre";

    public static ArrayList<ExpedienteEstudiante>
            obtenerExpedientesEstudiantesProfesor(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        validarExperienciaEducativa(idExperienciaEducativa);

        return ExpedienteDAO.obtenerExpedientesEstudiantesProfesor(
                idExperienciaEducativa);
    }

    public static ArrayList<ExpedienteEstudiante>
            buscarExpedientesEstudiantesProfesor(
            int idExperienciaEducativa, String filtro, String criterio)
            throws SQLException, NullPointerException {
        validarExperienciaEducativa(idExperienciaEducativa);
        filtro = limpiarTexto(filtro);
        criterio = limpiarTexto(criterio);

        if (criterio.isEmpty()) {
            return obtenerExpedientesEstudiantesProfesor(
                    idExperienciaEducativa);
        }

        if (!FILTRO_NOMBRE.equals(filtro)
                && !FILTRO_MATRICULA.equals(filtro)) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un filtro de búsqueda válido.");
        }

        return ExpedienteDAO.buscarExpedientesEstudiantesProfesor(
                idExperienciaEducativa, filtro, criterio);
    }

    private static void validarExperienciaEducativa(
            int idExperienciaEducativa) {
        if (idExperienciaEducativa <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la experiencia educativa.");
        }
    }

    private static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
