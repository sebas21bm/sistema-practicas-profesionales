package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.FormatoDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: DAO encargado de acceder a los formatos de documentos
 *              asociados a una experiencia educativa.
 */
public class FormatoDocumentoDAO {

    public static RespuestaOperacion subirFormatoDocumento(
            FormatoDocumento formatoDocumento)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL subir_formato_documento(?, ?, ?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setInt(
                        1,
                        formatoDocumento.getIdExperienciaEducativa());
                sentencia.setInt(2, formatoDocumento.getIdDocumento());
                sentencia.setString(
                        3,
                        formatoDocumento.getNombreOriginal());
                sentencia.setBytes(4, formatoDocumento.getArchivo());
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "El formato se ha subido correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<FormatoDocumento> obtenerFormatosDocumento(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        ArrayList<FormatoDocumento> formatosDocumento = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_experiencia_educativa, "
                        + "id_documento, tipo_documento, "
                        + "id_formato_documento, idarchivo, "
                        + "nombre_original "
                        + "FROM vista_formatos_documentos "
                        + "WHERE id_experiencia_educativa = ? "
                        + "ORDER BY tipo_documento;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExperienciaEducativa);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    FormatoDocumento formatoDocumento =
                            serializarFormatoDocumento(resultado);
                    formatosDocumento.add(formatoDocumento);
                }

                return formatosDocumento;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static FormatoDocumento obtenerFormatoDocumentoPorId(
            int idExperienciaEducativa, int idDocumento)
            throws SQLException, NullPointerException {
        FormatoDocumento formatoDocumento = null;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_experiencia_educativa, "
                        + "id_documento, tipo_documento, "
                        + "id_formato_documento, idarchivo, "
                        + "nombre_original "
                        + "FROM vista_formatos_documentos "
                        + "WHERE id_experiencia_educativa = ? "
                        + "AND id_documento = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExperienciaEducativa);
                sentencia.setInt(2, idDocumento);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    formatoDocumento =
                            serializarFormatoDocumento(resultado);
                }

                return formatoDocumento;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static FormatoDocumento serializarFormatoDocumento(
            ResultSet resultado) throws SQLException, NullPointerException {
        FormatoDocumento formatoDocumento = new FormatoDocumento();
        int idFormatoDocumento;
        int idArchivo;

        formatoDocumento.setIdExperienciaEducativa(
                resultado.getInt("id_experiencia_educativa"));
        formatoDocumento.setIdDocumento(resultado.getInt("id_documento"));
        formatoDocumento.setTipoDocumento(
                resultado.getString("tipo_documento"));

        idFormatoDocumento =
                resultado.getInt("id_formato_documento");
        if (!resultado.wasNull()) {
            formatoDocumento.setIdFormatoDocumento(idFormatoDocumento);
        }

        idArchivo = resultado.getInt("idarchivo");
        if (resultado.wasNull()) {
            formatoDocumento.setIdArchivo(null);
        } else {
            formatoDocumento.setIdArchivo(idArchivo);
        }

        formatoDocumento.setNombreOriginal(
                resultado.getString("nombre_original"));

        return formatoDocumento;
    }
}