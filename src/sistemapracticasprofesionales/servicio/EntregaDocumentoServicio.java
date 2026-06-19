package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.EntregaDocumentoDAO;
import sistemapracticasprofesionales.modelo.pojo.EntregaDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar las entregas programadas de
 *              documentos.
 */
public class EntregaDocumentoServicio {

    public static RespuestaOperacion asignarFechaEntrega(
            EntregaDocumento entregaDocumento)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta =
                validarEntregaDocumento(entregaDocumento);

        if (respuesta.getError()) {
            return respuesta;
        }

        return EntregaDocumentoDAO.asignarFechaEntrega(entregaDocumento);
    }

    public static ArrayList<EntregaDocumento> obtenerEntregasDocumento(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        validarExperienciaEducativa(idExperienciaEducativa);

        return EntregaDocumentoDAO.obtenerEntregasDocumento(
                idExperienciaEducativa);
    }

    private static RespuestaOperacion validarEntregaDocumento(
            EntregaDocumento entregaDocumento) {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();

        if (entregaDocumento == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se recibió la información de la entrega.");
            return respuesta;
        }

        if (entregaDocumento.getIdEntregaDocumento() <= 0) {
            agregarError(errores,
                    "Debe seleccionar un documento.");
        }

        if (entregaDocumento.getFechaEntrega() == null) {
            agregarError(errores,
                    "Debe seleccionar una fecha de entrega.");
        }

        if (entregaDocumento.getFechaEntrega() != null
                && entregaDocumento.getFechaEntrega().isBefore(
                        java.time.LocalDate.now())) {
            agregarError(errores,
                    "La fecha de entrega no puede ser anterior "
                    + "a la fecha actual.");
        }

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void validarExperienciaEducativa(
            int idExperienciaEducativa) {
        if (idExperienciaEducativa <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la experiencia educativa.");
        }
    }

    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }

        errores.append("- ").append(mensaje);
    }
}