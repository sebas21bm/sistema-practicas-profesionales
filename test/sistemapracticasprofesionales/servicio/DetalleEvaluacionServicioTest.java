package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.DetalleEvaluacion;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para DetalleEvaluacionServicio.
 */
public class DetalleEvaluacionServicioTest {

    @Test
    public void fueEntregadoConRetrasoCuandoSubidaEsPosteriorRegresaTrue() {
        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setFechaEntrega(LocalDate.now());
        detalle.setFechaSubida(LocalDateTime.now().plusDays(1));

        assertTrue(DetalleEvaluacionServicio.fueEntregadoConRetraso(detalle));
    }

    @Test
    public void fueEntregadoConRetrasoConDatosIncompletosRegresaFalse() {
        assertFalse(DetalleEvaluacionServicio.fueEntregadoConRetraso(null));
    }

    @Test
    public void validarDocumentoInicialNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.validarDocumentoInicial(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del documento.",
                respuesta.getMensaje());
    }

    @Test
    public void validarDocumentoInicialRechazadoSinObservacionesRegresaError()
            throws SQLException {
        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setIdDetallesEvaluacion(1);
        detalle.setClasificacion("Documento inicial");
        detalle.setEstado("Rechazado");
        detalle.setObservaciones("  ");

        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.validarDocumentoInicial(detalle);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                    "Los comentarios son obligatorios al seleccionar el estado "
                            + "“Rechazado”, escríbalos"));
    }

    @Test
    public void evaluarDocumentoSinArchivoRegresaError()
            throws SQLException {
        DetalleEvaluacion detalle = crearDetalleReporteBase();
        detalle.setIdArchivo(null);

        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.evaluarDocumentoExpediente(detalle);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "No se puede evaluar un documento sin archivo."));
    }

    @Test
    public void evaluarDocumentoConCalificacionFueraDeRangoRegresaError()
            throws SQLException {
        DetalleEvaluacion detalle = crearDetalleReporteBase();
        detalle.setCalificacion(11.0);

        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.evaluarDocumentoExpediente(detalle);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "La calificación debe estar entre 0 y 10."));
    }

    @Test
    public void subirEvaluacionProfesorConArchivoNoPermitidoRegresaError()
            throws SQLException {
        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setIdDetallesEvaluacion(1);
        detalle.setTipoDocumento("Primera evaluación del profesor");
        detalle.setNombreOriginal("evaluacion.txt");
        detalle.setArchivo(new byte[] {1, 2, 3});
        detalle.setCalificacion(8.0);
        detalle.setObservaciones("Buen desempeño");

        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.subirEvaluacionProfesor(detalle);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El archivo debe tener formato PDF o DOCX."));
    }

    @Test
    public void subirDocumentoEstudianteConExtensionNoPermitidaRegresaError()
            throws SQLException {
        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setIdDetallesEvaluacion(1);
        detalle.setNombreOriginal("documento.txt");
        detalle.setArchivo(new byte[] {1, 2, 3});

        RespuestaOperacion respuesta =
                DetalleEvaluacionServicio.subirDocumentoEstudiante(detalle);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El archivo debe tener formato PDF o DOCX."));
    }

    @Test(expected = IllegalArgumentException.class)
    public void obtenerDetalleConIdentificadorInvalidoLanzaExcepcion()
            throws SQLException {
        DetalleEvaluacionServicio.obtenerDetalleEvaluacionPorId(0);
    }

    private DetalleEvaluacion crearDetalleReporteBase() {
        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setIdDetallesEvaluacion(1);
        detalle.setClasificacion("Reporte");
        detalle.setTipoDocumento("Reporte mensual");
        detalle.setIdArchivo(10);
        detalle.setEstado("Aprobado");
        detalle.setCalificacion(8.0);
        detalle.setValor(5.0);
        detalle.setObservaciones("Correcto");
        detalle.setFechaEntrega(LocalDate.now());
        detalle.setFechaSubida(LocalDateTime.now());
        return detalle;
    }
}