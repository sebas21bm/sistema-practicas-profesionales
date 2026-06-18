package sistemapracticasprofesionales.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.utilidades.Constantes;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: DAO encargado de consultar los detalles de evaluación de
 *              documentos pertenecientes a un expediente.
 */
public class DetalleEvaluacionDAO {

    public static ArrayList<DetalleEvaluacion>
            obtenerDocumentosInicialesExpediente(int idExpediente)
            throws SQLException, NullPointerException {
        return obtenerDetallesExpedientePorClasificacion(
                idExpediente,
                "Documento inicial");
    }

    public static ArrayList<DetalleEvaluacion>
            obtenerReportesEvaluacionesExpediente(int idExpediente)
            throws SQLException, NullPointerException {
        return obtenerDetallesExpedientePorClasificacion(
                idExpediente,
                "Reportes y evaluaciones");
    }

    public static DetalleEvaluacion obtenerDetalleEvaluacionPorId(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {
        DetalleEvaluacion detalleEvaluacion = null;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        obtenerConsultaBase()
                        + "WHERE id_detalles_evaluacion = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idDetallesEvaluacion);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    detalleEvaluacion =
                            serializarDetalleEvaluacion(resultado);
                }

                return detalleEvaluacion;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    public static DetalleEvaluacion obtenerArchivoDocumento(
            int idDetallesEvaluacion)
            throws SQLException, NullPointerException {
        DetalleEvaluacion detalleEvaluacion = null;

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "SELECT de.id_detalles_evaluacion, "
                        + "a.idarchivo, a.nombre_original, a.archivo "
                        + "FROM detalles_evaluacion de "
                        + "JOIN archivo a ON de.idarchivo = a.idarchivo "
                        + "WHERE de.id_detalles_evaluacion = ?;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idDetallesEvaluacion);
                ResultSet resultado = sentencia.executeQuery();

                if (resultado.next()) {
                    detalleEvaluacion = new DetalleEvaluacion();
                    detalleEvaluacion.setIdDetallesEvaluacion(
                            resultado.getInt("id_detalles_evaluacion"));
                    detalleEvaluacion.setIdArchivo(
                            resultado.getInt("idarchivo"));
                    detalleEvaluacion.setNombreOriginal(
                            resultado.getString("nombre_original"));
                    detalleEvaluacion.setArchivo(
                            resultado.getBytes("archivo"));
                }

                return detalleEvaluacion;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static ArrayList<DetalleEvaluacion>
            obtenerDetallesExpedientePorClasificacion(
            int idExpediente, String clasificacion)
            throws SQLException, NullPointerException {
        ArrayList<DetalleEvaluacion> detallesEvaluacion =
                new ArrayList<>();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        obtenerConsultaBase()
                        + "WHERE id_expediente = ? "
                        + "AND clasificacion = ? "
                        + "ORDER BY id_documento, numero_entrega;";
                PreparedStatement sentencia =
                        conexionBD.prepareStatement(consulta);

                sentencia.setInt(1, idExpediente);
                sentencia.setString(2, clasificacion);

                ResultSet resultado = sentencia.executeQuery();

                while (resultado.next()) {
                    detallesEvaluacion.add(
                            serializarDetalleEvaluacion(resultado));
                }

                return detallesEvaluacion;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }
            
    public static RespuestaOperacion validarDocumentoInicial(
            DetalleEvaluacion detalleEvaluacion)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();

        try (Connection conexionBD =
                ConexionBD.crearParaRol(Rol.Profesor)) {
            if (conexionBD != null) {
                String consulta =
                        "{CALL validar_documento_expediente(?, ?, ?)}";
                CallableStatement sentencia =
                        conexionBD.prepareCall(consulta);

                sentencia.setInt(
                        1,
                        detalleEvaluacion.getIdDetallesEvaluacion());
                sentencia.setString(2, detalleEvaluacion.getEstado());
                sentencia.setString(
                        3,
                        detalleEvaluacion.getObservaciones());
                sentencia.execute();

                respuesta.setError(false);
                respuesta.setMensaje(
                        "La validación se ha registrado correctamente.");
                return respuesta;
            }
        }

        throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
    }

    private static String obtenerConsultaBase() {
        return "SELECT id_detalles_evaluacion, id_expediente, "
                + "id_entrega_documento, id_experiencia_educativa, "
                + "id_documento, matricula, nombre_estudiante, "
                + "tipo_documento, clasificacion, numero_entrega, "
                + "fecha_entrega, fecha_subida, estado, calificacion, "
                + "valor, porcentaje_obtenido, observaciones, "
                + "idarchivo, nombre_original "
                + "FROM vista_expediente_estudiante_profesor ";
    }

    private static DetalleEvaluacion serializarDetalleEvaluacion(
            ResultSet resultado) throws SQLException {
        DetalleEvaluacion detalleEvaluacion = new DetalleEvaluacion();
        Date fechaEntrega;
        Timestamp fechaSubida;
        double calificacion;
        int idArchivo;

        detalleEvaluacion.setIdDetallesEvaluacion(
                resultado.getInt("id_detalles_evaluacion"));
        detalleEvaluacion.setIdExpediente(
                resultado.getInt("id_expediente"));
        detalleEvaluacion.setIdEntregaDocumento(
                resultado.getInt("id_entrega_documento"));
        detalleEvaluacion.setIdExperienciaEducativa(
                resultado.getInt("id_experiencia_educativa"));
        detalleEvaluacion.setIdDocumento(
                resultado.getInt("id_documento"));
        detalleEvaluacion.setMatricula(
                resultado.getString("matricula"));
        detalleEvaluacion.setNombreEstudiante(
                resultado.getString("nombre_estudiante"));
        detalleEvaluacion.setTipoDocumento(
                resultado.getString("tipo_documento"));
        detalleEvaluacion.setClasificacion(
                resultado.getString("clasificacion"));
        detalleEvaluacion.setNumeroEntrega(
                resultado.getInt("numero_entrega"));
        detalleEvaluacion.setEstado(
                resultado.getString("estado"));
        detalleEvaluacion.setValor(
                resultado.getDouble("valor"));
        detalleEvaluacion.setPorcentajeObtenido(
                resultado.getDouble("porcentaje_obtenido"));
        detalleEvaluacion.setObservaciones(
                resultado.getString("observaciones"));
        detalleEvaluacion.setNombreOriginal(
                resultado.getString("nombre_original"));

        calificacion = resultado.getDouble("calificacion");
        if (resultado.wasNull()) {
            detalleEvaluacion.setCalificacion(null);
        } else {
            detalleEvaluacion.setCalificacion(calificacion);
        }

        fechaEntrega = resultado.getDate("fecha_entrega");
        if (fechaEntrega != null) {
            detalleEvaluacion.setFechaEntrega(fechaEntrega.toLocalDate());
        }

        fechaSubida = resultado.getTimestamp("fecha_subida");
        if (fechaSubida != null) {
            detalleEvaluacion.setFechaSubida(
                    fechaSubida.toLocalDateTime());
        }

        idArchivo = resultado.getInt("idarchivo");
        if (resultado.wasNull()) {
            detalleEvaluacion.setIdArchivo(null);
        } else {
            detalleEvaluacion.setIdArchivo(idArchivo);
        }

        return detalleEvaluacion;
    }
}