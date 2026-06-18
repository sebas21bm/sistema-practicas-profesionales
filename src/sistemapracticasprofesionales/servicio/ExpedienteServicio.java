package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemapracticasprofesionales.modelo.pojo.ExpedienteEstudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar reglas para consulta y cálculo
 *              de expedientes.
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

    public static ExpedienteEstudiante obtenerExpedienteEstudianteProfesorPorId(
            int idExpediente, int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        validarExpediente(idExpediente);
        validarExperienciaEducativa(idExperienciaEducativa);

        return ExpedienteDAO.obtenerExpedienteEstudianteProfesorPorId(
                idExpediente, idExperienciaEducativa);
    }

    public static RespuestaOperacion calcularCalificacionExpediente(
            int idExpediente, int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta;
        ExpedienteEstudiante expediente;

        validarExpediente(idExpediente);
        validarExperienciaEducativa(idExperienciaEducativa);

        expediente = ExpedienteDAO.obtenerExpedienteEstudianteProfesorPorId(
                idExpediente,
                idExperienciaEducativa);

        respuesta = validarExpedienteParaCalculo(expediente);

        if (respuesta.getError()) {
            return respuesta;
        }

        return ExpedienteDAO.calcularCalificacionExpediente(
                idExpediente);
    }

    private static RespuestaOperacion validarExpedienteParaCalculo(
            ExpedienteEstudiante expediente) {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        if (expediente == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se encontró el expediente seleccionado.");
            return respuesta;
        }

        if (!expediente.isCompleto()) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se puede calcular la calificación final "
                    + "porque el expediente aún no está completo. "
                    + "Todos los documentos deben estar aprobados.");
            return respuesta;
        }

        respuesta.setError(false);
        respuesta.setMensaje("");
        return respuesta;
    }

    private static void validarExperienciaEducativa(
            int idExperienciaEducativa) {
        if (idExperienciaEducativa <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la experiencia educativa.");
        }
    }

    private static void validarExpediente(int idExpediente) {
        if (idExpediente <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó el expediente.");
        }
    }

    private static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }
}