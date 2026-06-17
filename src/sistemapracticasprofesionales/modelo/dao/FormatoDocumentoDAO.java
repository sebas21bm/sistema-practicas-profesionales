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
import sistemapracticasprofesionales.modelo.pojo.Sesion;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: DAO encargado de acceder a los formatos de documentos.
 */
public class FormatoDocumentoDAO {

    public static RespuestaOperacion subirFormatoDocumento(
            FormatoDocumento formatoDocumento)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL subir_formato_documento(?, ?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setInt(1, formatoDocumento.getIdDocumento());
                sentencia.setString(2, formatoDocumento.getNombreOriginal());
                sentencia.setBytes(3, formatoDocumento.getArchivo());
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "El formato se ha subido correctamente.");
                return respuesta;
            }
        }

        throw new SQLException("No fue posible guardar "
                + "el formato en el sistema. Intente nuevamente");
    }

    public static ArrayList<FormatoDocumento> obtenerFormatosDocumento()
            throws SQLException, NullPointerException {
        ArrayList<FormatoDocumento> formatosDocumento = new ArrayList<>();

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_documento, tipo_documento, valor, "
                        + "idarchivo, nombre_original, fecha_subida "
                        + "FROM vista_formatos_documentos;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    FormatoDocumento formatoDocumento =
                            serializarFormatoDocumento(resultado);
                    formatosDocumento.add(formatoDocumento);
                }

                return formatosDocumento;
            }
        }

        throw new SQLException("No es posible mostrar los documentos. "
                + "Error al recuperar la información. Intente de nuevo");
    }

    public static FormatoDocumento obtenerFormatoDocumentoPorId(
            int idDocumento) throws SQLException, NullPointerException {
        FormatoDocumento formatoDocumento = null;

        try (Connection conexionBD = ConexionBD.crearParaRol(
                Sesion.getUsuarioActual().getRolUsuario())) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT id_documento, tipo_documento, valor, "
                        + "idarchivo, nombre_original, fecha_subida "
                        + "FROM vista_formatos_documentos "
                        + "WHERE id_documento = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idDocumento);
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
        int idArchivo;

        formatoDocumento.setIdDocumento(resultado.getInt("id_documento"));
        formatoDocumento.setTipoDocumento(
                resultado.getString("tipo_documento"));
        formatoDocumento.setValor(resultado.getDouble("valor"));

        idArchivo = resultado.getInt("idarchivo");
        if (resultado.wasNull()) {
            formatoDocumento.setIdArchivo(null);
        } else {
            formatoDocumento.setIdArchivo(idArchivo);
        }

        formatoDocumento.setNombreOriginal(
                resultado.getString("nombre_original"));
        formatoDocumento.setFechaSubida(resultado.getString("fecha_subida"));

        return formatoDocumento;
    }
}