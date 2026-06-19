package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para AsignacionServicio.
 */
public class AsignacionServicioTest {

    @Test
    public void asignarProyectoSinEstudianteRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                AsignacionServicio.asignarProyecto(null, crearProyectoConCupo());

        assertTrue(respuesta.getError());
        assertEquals("Debes seleccionar un estudiante.", respuesta.getMensaje());
    }

    @Test
    public void asignarProyectoSinProyectoRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                AsignacionServicio.asignarProyecto(new Estudiante(), null);

        assertTrue(respuesta.getError());
        assertEquals("Debes seleccionar un proyecto.", respuesta.getMensaje());
    }

    @Test
    public void asignarProyectoSinCupoRegresaError()
            throws SQLException {
        ProyectoPracticas proyecto = crearProyectoConCupo();
        proyecto.setCupo(0);

        RespuestaOperacion respuesta =
                AsignacionServicio.asignarProyecto(new Estudiante(), proyecto);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains("se quedó sin cupos"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void recuperarDetallesAsignacionConIdentificadoresInvalidosLanzaExcepcion()
            throws SQLException {
        AsignacionServicio.recuperarDetallesAsignacion(0, 0);
    }

    private ProyectoPracticas crearProyectoConCupo() {
        ProyectoPracticas proyecto = new ProyectoPracticas();
        proyecto.setNumProyecto(1);
        proyecto.setCupo(2);
        return proyecto;
    }
}