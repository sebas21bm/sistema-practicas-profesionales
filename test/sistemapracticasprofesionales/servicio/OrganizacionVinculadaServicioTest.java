package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para OrganizacionVinculadaServicio.
 */
public class OrganizacionVinculadaServicioTest {

    @Test
    public void registrarOrganizacionConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                OrganizacionVinculadaServicio.registrarOrganizacionVinculada(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información de la organización vinculada",
                respuesta.getMensaje());
    }

    @Test
    public void registrarOrganizacionConCamposVaciosRegresaErrores()
            throws SQLException {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();

        RespuestaOperacion respuesta =
                OrganizacionVinculadaServicio.registrarOrganizacionVinculada(organizacion);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El nombre de la OrganizacionVinculada no puede ir vacío"));
        assertTrue(respuesta.getMensaje().contains(
                "El registro de la calle es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El registro de la colonia es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El registro del código postal es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El registro del teléfono es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El registro del correo electrónico es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El tipo de organización es obligatorio"));
    }

    @Test
    public void registrarOrganizacionConCodigoPostalInvalidoRegresaError()
            throws SQLException {
        OrganizacionVinculada organizacion = crearOrganizacionBase();
        organizacion.setCodigoPostal("9100A");

        RespuestaOperacion respuesta =
                OrganizacionVinculadaServicio.registrarOrganizacionVinculada(organizacion);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "debe contener exactamente 5 dígitos."));
    }

    @Test
    public void registrarOrganizacionConTipoInvalidoRegresaError()
            throws SQLException {
        OrganizacionVinculada organizacion = crearOrganizacionBase();
        organizacion.setTipo("Mixta");

        RespuestaOperacion respuesta =
                OrganizacionVinculadaServicio.registrarOrganizacionVinculada(organizacion);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "debe ser Pública o Privada."));
    }

    private OrganizacionVinculada crearOrganizacionBase() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNombre("Empresa de Software");
        organizacion.setCalle("Av. Universidad 100");
        organizacion.setColonia("Centro");
        organizacion.setCodigoPostal("91000");
        organizacion.setTelefono("2281234567");
        organizacion.setCorreo("contacto@empresa.com");
        organizacion.setTipo("Privada");
        return organizacion;
    }
}