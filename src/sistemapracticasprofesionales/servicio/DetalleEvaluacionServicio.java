package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.DetalleEvaluacionDAO;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio para consultar
 *              detalles de evaluación de documentos.
 */
public class DetalleEvaluacionServicio {

    private static final String ESTADO_APROBADO = "Aprobado";
    private static final String ESTADO_RECHAZADO = "Rechazado";
    private static final int LONGITUD_MAXIMA_OBSERVACIONES = 255;
    
    public static ArrayList<DetalleEvaluacion>
            obtenerDocumentosInicialesExpediente(int idExpediente)
            throws SQLException, NullPointerException {
        validarExpediente(idExpediente);

        return DetalleEvaluacionDAO.obtenerDocumentosInicialesExpediente(
                idExpediente);
    }

    public static ArrayList<DetalleEvaluacion>
            obtenerReportesEvaluacionesExpediente(int idExpediente)
            throws SQLException, NullPointerException {
        validarExpediente(idExpediente);

        return DetalleEvaluacionDAO.obtenerReportesEvaluacionesExpediente(
                idExpediente);
    }

    public static DetalleEvaluacion obtenerDetalleEvaluacionPorId(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {
        validarDetalleEvaluacion(idDetallesEvaluacion);

        return DetalleEvaluacionDAO.obtenerDetalleEvaluacionPorId(
                idDetallesEvaluacion);
    }

    public static DetalleEvaluacion obtenerArchivoDocumento(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {
        validarDetalleEvaluacion(idDetallesEvaluacion);

        return DetalleEvaluacionDAO.obtenerArchivoDocumento(
                idDetallesEvaluacion);
    }

    private static void validarExpediente(int idExpediente) {
        if (idExpediente <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó el expediente.");
        }
    }

    private static void validarDetalleEvaluacion(
            int idDetallesEvaluacion) {
        if (idDetallesEvaluacion <= 0) {
            throw new IllegalArgumentException(
                    "No se identificó el documento seleccionado.");
        }
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
                    "No se identificó el documento seleccionado.");
        }

        if (!detalleEvaluacion.esDocumentoInicial()) {
            agregarError(errores,
                    "El documento seleccionado no corresponde "
                    + "a un documento inicial.");
        }

        if (estaVacio(detalleEvaluacion.getEstado())) {
            agregarError(errores,
                    "Debe seleccionar un estado.");
        } else if (!ESTADO_APROBADO.equals(detalleEvaluacion.getEstado())
                && !ESTADO_RECHAZADO.equals(detalleEvaluacion.getEstado())) {
            agregarError(errores,
                    "El estado solo puede ser Aprobado o Rechazado.");
        }

        detalleEvaluacion.setObservaciones(
                limpiarTexto(detalleEvaluacion.getObservaciones()));

        if (ESTADO_RECHAZADO.equals(detalleEvaluacion.getEstado())
                && estaVacio(detalleEvaluacion.getObservaciones())) {
            agregarError(errores,
                    "Debe ingresar comentarios al rechazar "
                    + "el documento.");
        }

        if (detalleEvaluacion.getObservaciones().length()
                > LONGITUD_MAXIMA_OBSERVACIONES) {
            agregarError(errores,
                    "Los comentarios no pueden superar los 255 caracteres.");
        }

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
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