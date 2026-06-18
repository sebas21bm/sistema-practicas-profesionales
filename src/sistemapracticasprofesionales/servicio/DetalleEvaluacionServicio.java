package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.DetalleEvaluacionDAO;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Sesion;
/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio para consultar
 *              y evaluar detalles de documentos.
 */
public class DetalleEvaluacionServicio {

    private static final String ESTADO_APROBADO = "Aprobado";
    private static final String ESTADO_RECHAZADO = "Rechazado";
    private static final double CALIFICACION_MINIMA = 0.0;
    private static final double CALIFICACION_MAXIMA = 10.0;
    private static final int LONGITUD_MAXIMA_OBSERVACIONES = 255;
    private static final int TAMANIO_MAXIMO_ARCHIVO = 10 * 1024 * 1024;

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

    public static RespuestaOperacion evaluarDocumentoExpediente(
            DetalleEvaluacion detalleEvaluacion)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta =
                validarDatosEvaluacionDocumento(detalleEvaluacion);

        if (respuesta.getError()) {
            return respuesta;
        }

        calcularPorcentajeEvaluacion(detalleEvaluacion);

        return DetalleEvaluacionDAO.evaluarDocumentoExpediente(
                detalleEvaluacion);
    }

    public static RespuestaOperacion subirEvaluacionProfesor(
            DetalleEvaluacion detalleEvaluacion)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta =
                validarDatosEvaluacionProfesor(detalleEvaluacion);

        if (respuesta.getError()) {
            return respuesta;
        }

        calcularPorcentajeEvaluacionProfesor(detalleEvaluacion);

        return DetalleEvaluacionDAO.subirEvaluacionProfesor(
                detalleEvaluacion);
    }

    public static boolean fueEntregadoConRetraso(
            DetalleEvaluacion detalleEvaluacion) {
        if (detalleEvaluacion == null
                || detalleEvaluacion.getFechaEntrega() == null
                || detalleEvaluacion.getFechaSubida() == null) {
            return false;
        }

        return detalleEvaluacion.getFechaSubida()
                .toLocalDate()
                .isAfter(detalleEvaluacion.getFechaEntrega());
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

        validarEstadoEvaluacion(detalleEvaluacion, errores);

        detalleEvaluacion.setObservaciones(
                limpiarTexto(detalleEvaluacion.getObservaciones()));

        if (ESTADO_RECHAZADO.equals(detalleEvaluacion.getEstado())
                && estaVacio(detalleEvaluacion.getObservaciones())) {
            agregarError(errores,
                    "Debe ingresar comentarios al rechazar "
                    + "el documento.");
        }

        validarLongitudObservaciones(detalleEvaluacion, errores);

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static RespuestaOperacion validarDatosEvaluacionDocumento(
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

        if (detalleEvaluacion.esDocumentoInicial()) {
            agregarError(errores,
                    "El documento seleccionado corresponde "
                    + "a un documento inicial.");
        }

        if (detalleEvaluacion.esEvaluacionProfesor()) {
            agregarError(errores,
                    "La evaluación del profesor debe registrarse "
                    + "desde su pantalla correspondiente.");
        }

        if (!detalleEvaluacion.tieneArchivo()) {
            agregarError(errores,
                    "No se puede evaluar un documento sin archivo.");
        }

        validarEstadoEvaluacion(detalleEvaluacion, errores);

        if (!fueEntregadoConRetraso(detalleEvaluacion)) {
            validarCalificacion(detalleEvaluacion, errores);
        }

        detalleEvaluacion.setObservaciones(
                limpiarTexto(detalleEvaluacion.getObservaciones()));

        if (ESTADO_RECHAZADO.equals(detalleEvaluacion.getEstado())
                && estaVacio(detalleEvaluacion.getObservaciones())) {
            agregarError(errores,
                    "Debe ingresar comentarios al rechazar "
                    + "el documento.");
        }

        validarLongitudObservaciones(detalleEvaluacion, errores);

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static RespuestaOperacion validarDatosEvaluacionProfesor(
            DetalleEvaluacion detalleEvaluacion) {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();

        if (detalleEvaluacion == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se recibió la información de la evaluación.");
            return respuesta;
        }

        if (detalleEvaluacion.getIdDetallesEvaluacion() <= 0) {
            agregarError(errores,
                    "No se identificó la evaluación seleccionada.");
        }

        if (!detalleEvaluacion.esEvaluacionProfesor()) {
            agregarError(errores,
                    "El documento seleccionado no corresponde "
                    + "a una evaluación del profesor.");
        }

        validarArchivoEvaluacionProfesor(detalleEvaluacion, errores);
        validarCalificacion(detalleEvaluacion, errores);

        detalleEvaluacion.setObservaciones(
                limpiarTexto(detalleEvaluacion.getObservaciones()));

        validarLongitudObservaciones(detalleEvaluacion, errores);

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void validarArchivoEvaluacionProfesor(
            DetalleEvaluacion detalleEvaluacion,
            StringBuilder errores) {
        if (estaVacio(detalleEvaluacion.getNombreOriginal())
                || detalleEvaluacion.getArchivo() == null
                || detalleEvaluacion.getArchivo().length == 0) {
            agregarError(errores,
                    "Debe seleccionar un archivo válido.");
            return;
        }

        if (!esExtensionPermitida(detalleEvaluacion.getNombreOriginal())) {
            agregarError(errores,
                    "El archivo debe tener formato PDF o DOCX.");
        }

        if (detalleEvaluacion.getArchivo().length
                > TAMANIO_MAXIMO_ARCHIVO) {
            agregarError(errores,
                    "El archivo no puede superar los 10 MB.");
        }
    }

    private static void validarEstadoEvaluacion(
            DetalleEvaluacion detalleEvaluacion,
            StringBuilder errores) {
        if (estaVacio(detalleEvaluacion.getEstado())) {
            agregarError(errores,
                    "Debe seleccionar un estado.");
        } else if (!ESTADO_APROBADO.equals(detalleEvaluacion.getEstado())
                && !ESTADO_RECHAZADO.equals(detalleEvaluacion.getEstado())) {
            agregarError(errores,
                    "El estado solo puede ser Aprobado o Rechazado.");
        }
    }

    private static void validarCalificacion(
            DetalleEvaluacion detalleEvaluacion,
            StringBuilder errores) {
        if (detalleEvaluacion.getCalificacion() == null) {
            agregarError(errores,
                    "Debe ingresar una calificación.");
        } else if (detalleEvaluacion.getCalificacion()
                < CALIFICACION_MINIMA
                || detalleEvaluacion.getCalificacion()
                > CALIFICACION_MAXIMA) {
            agregarError(errores,
                    "La calificación debe estar entre 0 y 10.");
        }
    }

    private static void validarLongitudObservaciones(
            DetalleEvaluacion detalleEvaluacion,
            StringBuilder errores) {
        if (detalleEvaluacion.getObservaciones().length()
                > LONGITUD_MAXIMA_OBSERVACIONES) {
            agregarError(errores,
                    "Las observaciones no pueden superar "
                    + "los 255 caracteres.");
        }
    }

    private static void calcularPorcentajeEvaluacion(
            DetalleEvaluacion detalleEvaluacion) {
        if (fueEntregadoConRetraso(detalleEvaluacion)) {
            detalleEvaluacion.setCalificacion(0.0);
            detalleEvaluacion.setPorcentajeObtenido(0.0);
            return;
        }

        double porcentajeObtenido =
                (detalleEvaluacion.getCalificacion()
                / CALIFICACION_MAXIMA)
                * detalleEvaluacion.getValor();

        detalleEvaluacion.setPorcentajeObtenido(
                redondearDosDecimales(porcentajeObtenido));
    }

    private static void calcularPorcentajeEvaluacionProfesor(
            DetalleEvaluacion detalleEvaluacion) {
        double porcentajeObtenido =
                (detalleEvaluacion.getCalificacion()
                / CALIFICACION_MAXIMA)
                * detalleEvaluacion.getValor();

        detalleEvaluacion.setPorcentajeObtenido(
                redondearDosDecimales(porcentajeObtenido));
    }

    private static boolean esExtensionPermitida(String nombreArchivo) {
        String nombreSeguro;

        if (nombreArchivo == null) {
            return false;
        }

        nombreSeguro = nombreArchivo.toLowerCase();

        return nombreSeguro.endsWith(".pdf")
                || nombreSeguro.endsWith(".docx");
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

    private static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private static double redondearDosDecimales(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }

        errores.append("- ").append(mensaje);
    }
    
    public static DetalleEvaluacion obtenerDetalleEvaluacionEstudiantePorId(
        int idDetallesEvaluacion)
            throws SQLException, NullPointerException {

        validarDetalleEvaluacion(idDetallesEvaluacion);

        return DetalleEvaluacionDAO.obtenerDetalleEvaluacionEstudiantePorId(
                idDetallesEvaluacion,
                Sesion.getUsuarioActual().getNombreUsuario());
    }

    public static DetalleEvaluacion obtenerArchivoDocumentoEstudiante(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {

        validarDetalleEvaluacion(idDetallesEvaluacion);

        return DetalleEvaluacionDAO.obtenerArchivoDocumentoEstudiante(
                idDetallesEvaluacion,
                Sesion.getUsuarioActual().getNombreUsuario());
    }

    public static RespuestaOperacion subirDocumentoEstudiante(
            DetalleEvaluacion detalleEvaluacion)
            throws SQLException, NullPointerException {

        RespuestaOperacion respuesta =
                validarDocumentoEstudiante(detalleEvaluacion);

        if (respuesta.getError()) {
            return respuesta;
        }

        asignarEstadoEntregaEstudiante(detalleEvaluacion);

        return DetalleEvaluacionDAO.subirDocumentoEstudiante(
                detalleEvaluacion);
    }
    
    private static RespuestaOperacion validarDocumentoEstudiante(
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

        if (estaVacio(detalleEvaluacion.getNombreOriginal())
                || detalleEvaluacion.getArchivo() == null) {
            agregarError(errores,
                    "Debes seleccionar un archivo para subir.");
        }

        if (!estaVacio(detalleEvaluacion.getNombreOriginal())
                && !esExtensionPermitida(
                        detalleEvaluacion.getNombreOriginal())) {
            agregarError(errores,
                    "El archivo debe tener formato PDF o DOCX.");
        }

        if (detalleEvaluacion.getArchivo() != null
                && (detalleEvaluacion.getArchivo().length <= 0
                || detalleEvaluacion.getArchivo().length
                > TAMANIO_MAXIMO_ARCHIVO)) {
            agregarError(errores,
                    "El archivo no puede estar vacío ni superar los 10 MB.");
        }

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void asignarEstadoEntregaEstudiante(
        DetalleEvaluacion detalleEvaluacion) {

        if (detalleEvaluacion.getFechaEntrega() != null
                && java.time.LocalDate.now().isAfter(
                        detalleEvaluacion.getFechaEntrega())) {
            detalleEvaluacion.setEstado("Entregado con retraso");
        } else {
            detalleEvaluacion.setEstado("Entregado");
        }
    }
    
}