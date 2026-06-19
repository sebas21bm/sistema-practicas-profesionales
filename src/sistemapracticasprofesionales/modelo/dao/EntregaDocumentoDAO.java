package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.EntregaDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: DAO encargado de acceder a las entregas programadas de
 *              documentos.
 */
public class EntregaDocumentoDAO {

    public static RespuestaOperacion asignarFechaEntrega(
            EntregaDocumento entregaDocumento)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL asignar_fecha_entrega_documento(?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setInt(1,entregaDocumento.getIdEntregaDocumento());
                sentencia.setDate(2,
                        Date.valueOf(entregaDocumento.getFechaEntrega()));
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "La fecha de entrega se asignó correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static ArrayList<EntregaDocumento> obtenerEntregasDocumento(
            int idExperienciaEducativa)
            throws SQLException, NullPointerException {
        ArrayList<EntregaDocumento> entregasDocumento = new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT ed.id_entrega_documento, "
                        + "ed.id_experiencia_educativa, ed.id_documento, "
                        + "d.tipo_documento, d.valor, "
                        + "ed.numero_entrega, ed.fecha_entrega "
                        + "FROM entrega_documento ed "
                        + "JOIN documento d "
                        + "ON ed.id_documento = d.id_documento "
                        + "WHERE ed.id_experiencia_educativa = ? "
                        + "AND ed.activo = 1 "
                        + "ORDER BY d.clasificacion, "
                        + "d.id_documento, ed.numero_entrega;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExperienciaEducativa);
                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    EntregaDocumento entregaDocumento =
                            serializarEntregaDocumento(resultado);
                    entregasDocumento.add(entregaDocumento);
                }

                return entregasDocumento;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static EntregaDocumento serializarEntregaDocumento(
            ResultSet resultado) throws SQLException {
        EntregaDocumento entregaDocumento = new EntregaDocumento();
        Date fechaEntrega;

        entregaDocumento.setIdEntregaDocumento(
                resultado.getInt("id_entrega_documento"));
        entregaDocumento.setIdExperienciaEducativa(
                resultado.getInt("id_experiencia_educativa"));
        entregaDocumento.setIdDocumento(resultado.getInt("id_documento"));
        entregaDocumento.setTipoDocumento(
                resultado.getString("tipo_documento"));
        entregaDocumento.setValor(resultado.getDouble("valor"));
        entregaDocumento.setNumeroEntrega(
                resultado.getInt("numero_entrega"));

        fechaEntrega = resultado.getDate("fecha_entrega");
        if (fechaEntrega != null) {
            entregaDocumento.setFechaEntrega(
                    fechaEntrega.toLocalDate());
        }

        return entregaDocumento;
    }
}