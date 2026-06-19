package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para ProyectoPracticasServicio.
 */
public class ProyectoPracticasServicioTest {

    @Test
    public void registrarProyectoConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                ProyectoPracticasServicio.registrarProyectoConResponsableNuevo(
                        null, crearResponsableBase());

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del proyecto.",
                respuesta.getMensaje());
    }

    @Test
    public void registrarProyectoConCamposInvalidosRegresaErrores()
            throws SQLException {
        ProyectoPracticas proyecto = new ProyectoPracticas();

        RespuestaOperacion respuesta =
                ProyectoPracticasServicio.registrarProyectoConResponsableNuevo(
                        proyecto, crearResponsableBase());

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El nombre del proyecto es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "La descripción del proyecto es obligatoria"));
        assertTrue(respuesta.getMensaje().contains(
                "El cupo es obligatorio y debe ser mínimo 1."));
        assertTrue(respuesta.getMensaje().contains(
                "La fecha de finalización es obligatoria."));
        assertTrue(respuesta.getMensaje().contains(
                "Debes seleccionar una organización vinculada."));
    }

    @Test
    public void registrarProyectoConResponsableNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                ProyectoPracticasServicio.registrarProyectoConResponsableNuevo(
                        crearProyectoBase(), null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del responsable.",
                respuesta.getMensaje());
    }

    @Test
    public void registrarProyectoConResponsableInvalidoRegresaErrores()
            throws SQLException {
        ResponsableProyecto responsable = crearResponsableBase();
        responsable.setNombre("Ana 123");
        responsable.setTelefono("0000000000");
        responsable.setNumOrganizacionVinculada(0);

        RespuestaOperacion respuesta =
                ProyectoPracticasServicio.registrarProyectoConResponsableNuevo(
                        crearProyectoBase(), responsable);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El nombre del responsable es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El teléfono del responsable es obligatorio"));
        assertTrue(respuesta.getMensaje().contains(
                "El responsable debe estar asociado"));
    }

    @Test
    public void registrarProyectoConResponsableExistenteNoSeleccionadoRegresaError()
            throws SQLException {
        ProyectoPracticas proyecto = crearProyectoBase();
        proyecto.setIdResponsable(0);

        RespuestaOperacion respuesta =
                ProyectoPracticasServicio.registrarProyectoConResponsableExistente(
                        proyecto);

        assertTrue(respuesta.getError());
        assertEquals("- Debes seleccionar un responsable existente.",
                respuesta.getMensaje());
    }

    private ProyectoPracticas crearProyectoBase() {
        ProyectoPracticas proyecto = new ProyectoPracticas();
        proyecto.setNombre("Sistema de control documental");
        proyecto.setDescripcion("Desarrollo de módulo para gestionar documentos");
        proyecto.setCupo(3);
        proyecto.setFechaFinalizacion(new Date());
        proyecto.setOrganizacionVinculada(crearOrganizacionBase());
        return proyecto;
    }

    private OrganizacionVinculada crearOrganizacionBase() {
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setNumOrganizacionVinculada(1);
        organizacion.setNombre("Organización Vinculada");
        return organizacion;
    }

    private ResponsableProyecto crearResponsableBase() {
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setNombre("Ana");
        responsable.setPaterno("López");
        responsable.setMaterno("Mora");
        responsable.setTelefono("2281234567");
        responsable.setCorreo("ana@empresa.com");
        responsable.setPuesto("Gerente de proyectos");
        responsable.setNumOrganizacionVinculada(1);
        return responsable;
    }
}