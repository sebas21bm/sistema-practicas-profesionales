package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.FormatoDocumento;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para FormatoDocumentoServicio.
 */
public class FormatoDocumentoServicioTest {

    @Test
    public void subirFormatoConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                FormatoDocumentoServicio.subirFormatoDocumento(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del formato.",
                respuesta.getMensaje());
    }
    
    @Test
    public void subirFormatoConCamposFaltantesRegresaErrores()
            throws SQLException {
        FormatoDocumento formato = new FormatoDocumento();

        formato.setNombreOriginal("formato.pdf");
        formato.setArchivo(new byte[] {1, 2, 3});

        RespuestaOperacion respuesta =
                FormatoDocumentoServicio.subirFormatoDocumento(formato);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "No se identificó la experiencia educativa."));
        assertTrue(respuesta.getMensaje().contains(
                "Debe seleccionar un tipo de documento."));
    }

    @Test
    public void subirFormatoConExtensionNoPermitidaRegresaError()
            throws SQLException {
        FormatoDocumento formato = crearFormatoBase();
        formato.setNombreOriginal("formato.txt");

        RespuestaOperacion respuesta =
                FormatoDocumentoServicio.subirFormatoDocumento(formato);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El archivo debe tener extensión .pdf o .docx."));
    }

    @Test(expected = IllegalArgumentException.class)
    public void obtenerFormatosConExperienciaInvalidaLanzaExcepcion()
            throws SQLException {
        FormatoDocumentoServicio.obtenerFormatosDocumento(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void obtenerFormatoPorIdConDocumentoInvalidoLanzaExcepcion()
            throws SQLException {
        FormatoDocumentoServicio.obtenerFormatoDocumentoPorId(1, 0);
    }

    private FormatoDocumento crearFormatoBase() {
        FormatoDocumento formato = new FormatoDocumento();
        formato.setIdExperienciaEducativa(1);
        formato.setIdDocumento(1);
        formato.setNombreOriginal("formato.pdf");
        formato.setArchivo(new byte[] {1, 2, 3});
        return formato;
    }
}