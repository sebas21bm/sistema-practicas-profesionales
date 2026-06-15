package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.FormatoDocumentoDAO;
import sistemapracticasprofesionales.modelo.pojo.FormatoDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio de formatos
 *              de documentos.
 */
public class FormatoDocumentoServicio {

    private static final String EXTENSION_PDF = ".pdf";
    private static final String EXTENSION_DOCX = ".docx";

    public static RespuestaOperacion subirFormatoDocumento(
            FormatoDocumento formatoDocumento)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta =
                validarFormatoDocumento(formatoDocumento);

        if (respuesta.getError()) {
            return respuesta;
        }

        return FormatoDocumentoDAO.subirFormatoDocumento(formatoDocumento);
    }

    public static ArrayList<FormatoDocumento> obtenerFormatosDocumento()
            throws SQLException, NullPointerException {
        return FormatoDocumentoDAO.obtenerFormatosDocumento();
    }

    public static FormatoDocumento obtenerFormatoDocumentoPorId(
            int idDocumento) throws SQLException, NullPointerException {
        return FormatoDocumentoDAO.obtenerFormatoDocumentoPorId(idDocumento);
    }

    private static RespuestaOperacion validarFormatoDocumento(
            FormatoDocumento formatoDocumento) {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();

        if (formatoDocumento == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se recibió la información del formato.");
            return respuesta;
        }

        limpiarDatosFormatoDocumento(formatoDocumento);
        validarCamposObligatorios(formatoDocumento, errores);
        validarFormatoArchivo(formatoDocumento, errores);

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void validarCamposObligatorios(
            FormatoDocumento formatoDocumento, StringBuilder errores) {
        if (formatoDocumento.getIdDocumento() <= 0) {
            agregarError(errores,
                    "Debe seleccionar un tipo de documento.");
        }

        if (estaVacio(formatoDocumento.getNombreOriginal())) {
            agregarError(errores,
                    "Debe seleccionar un archivo para subir.");
        }

        if (formatoDocumento.getArchivo() == null
                || formatoDocumento.getArchivo().length == 0) {
            agregarError(errores,
                    "El archivo seleccionado no contiene información.");
        }
    }

    private static void validarFormatoArchivo(
            FormatoDocumento formatoDocumento, StringBuilder errores) {
        String nombreArchivo = formatoDocumento.getNombreOriginal();

        if (!estaVacio(nombreArchivo)
                && !tieneExtensionPermitida(nombreArchivo)) {
            agregarError(errores,
                    "El archivo debe tener extensión .pdf o .docx.");
        }
    }

    private static boolean tieneExtensionPermitida(String nombreArchivo) {
        String nombreMinusculas = nombreArchivo.toLowerCase();

        return nombreMinusculas.endsWith(EXTENSION_PDF)
                || nombreMinusculas.endsWith(EXTENSION_DOCX);
    }

    private static void limpiarDatosFormatoDocumento(
            FormatoDocumento formatoDocumento) {
        formatoDocumento.setNombreOriginal(
                limpiarTexto(formatoDocumento.getNombreOriginal()));
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