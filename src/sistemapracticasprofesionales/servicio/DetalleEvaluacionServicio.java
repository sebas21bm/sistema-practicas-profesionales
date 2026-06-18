package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.DetalleEvaluacionDAO;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio de los
 *              detalles de evaluación.
 */
public class DetalleEvaluacionServicio {

    public static ArrayList<DetalleEvaluacion> obtenerDocumentosIniciales(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        validarExperienciaEducativa(idExperienciaEducativa);

        return DetalleEvaluacionDAO.obtenerDocumentosIniciales(
                idExperienciaEducativa);
    }

    public static DetalleEvaluacion obtenerArchivoDocumento(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {
        validarDetalleEvaluacion(idDetallesEvaluacion);

        return DetalleEvaluacionDAO.obtenerArchivoDocumento(
                idDetallesEvaluacion);
    }

    public static RespuestaOperacion validarDocumentoInicial(
            DetalleEvaluacion detalleEvaluacion)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta =
                validarDatosDocumentoInicial(detalleEvaluacion);

        if (respuesta.getError()) {
            return respuesta;
        }

        return DetalleEvaluacionDAO.validarDocumentoInicial(
                detalleEvaluacion);
    }

    private static RespuestaOperacion validarDatosDocumentoInicial(
            DetalleEvaluacion detalleEvaluacion) {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();

        if (detalleEvaluacion == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se recibió la información del documento.");
            return respuesta;
        }

        if (detalleEvaluacion.getIdDetallesEvaluacion() <= 0) {
            agregarError(errores,
                    "Debe seleccionar un documento.");
        }

        if (estaVacio(detalleEvaluacion.getEstado())) {
            agregarError(errores,
                    "Debe seleccionar un estado.");
        } else if (!detalleEvaluacion.getEstado().equals("Aprobado")
                && !detalleEvaluacion.getEstado().equals("Rechazado")) {
            agregarError(errores,
                    "El estado solo puede ser Aprobado o Rechazado.");
        }

        if ("Rechazado".equals(detalleEvaluacion.getEstado())
                && estaVacio(detalleEvaluacion.getObservaciones())) {
            agregarError(errores,
                    "Debe ingresar comentarios al rechazar.");
        }

        limpiarDatos(detalleEvaluacion);

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void limpiarDatos(
            DetalleEvaluacion detalleEvaluacion) {
        detalleEvaluacion.setObservaciones(
                limpiarTexto(detalleEvaluacion.getObservaciones()));
    }

    private static void validarExperienciaEducativa(
            int idExperienciaEducativa) {
        if (idExperienciaEducativa <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó la experiencia educativa.");
        }
    }

    private static void validarDetalleEvaluacion(
            int idDetallesEvaluacion) {
        if (idDetallesEvaluacion <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó el documento.");
        }
    }

    private static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }

        errores.append("- ").append(mensaje);
    }
}