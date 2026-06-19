package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.EntregaDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor:  Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para EntregaDocumentoServicio.
 */
public class EntregaDocumentoServicioTest {

    @Test
    public void asignarFechaConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                EntregaDocumentoServicio.asignarFechaEntrega(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información de la entrega.",
                respuesta.getMensaje());
    }

    @Test
    public void asignarFechaSinDocumentoNiFechaRegresaErrores()
            throws SQLException {
        EntregaDocumento entrega = new EntregaDocumento();

        RespuestaOperacion respuesta =
                EntregaDocumentoServicio.asignarFechaEntrega(entrega);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "Debe seleccionar un documento."));
        assertTrue(respuesta.getMensaje().contains(
                "Debe seleccionar una fecha de entrega."));
    }

    @Test
    public void asignarFechaAnteriorAlDiaActualRegresaError()
            throws SQLException {
        EntregaDocumento entrega = new EntregaDocumento();
        entrega.setIdEntregaDocumento(1);
        entrega.setFechaEntrega(LocalDate.now().minusDays(1));

        RespuestaOperacion respuesta =
                EntregaDocumentoServicio.asignarFechaEntrega(entrega);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "La fecha de entrega no puede ser anterior"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void obtenerEntregasConExperienciaInvalidaLanzaExcepcion()
            throws SQLException {
        EntregaDocumentoServicio.obtenerEntregasDocumento(0);
    }
}