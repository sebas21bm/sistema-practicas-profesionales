package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import org.junit.Test;
import sistemapracticasprofesionales.modelo.pojo.Sesion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para ExpedienteServicio.
 */
public class ExpedienteServicioTest {

    @Test(expected = IllegalArgumentException.class)
    public void obtenerExpedientesConExperienciaInvalidaLanzaExcepcion()
            throws SQLException {
        ExpedienteServicio.obtenerExpedientesEstudiantesProfesor(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buscarExpedientesConFiltroInvalidoLanzaExcepcion()
            throws SQLException {
        ExpedienteServicio.buscarExpedientesEstudiantesProfesor(
                1, "Correo", "juan");
    }

    @Test(expected = IllegalArgumentException.class)
    public void obtenerExpedientePorIdInvalidoLanzaExcepcion()
            throws SQLException {
        ExpedienteServicio.obtenerExpedienteEstudianteProfesorPorId(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calcularCalificacionConExpedienteInvalidoLanzaExcepcion()
            throws SQLException {
        ExpedienteServicio.calcularCalificacionExpediente(0, 1);
    }

    @Test(expected = NullPointerException.class)
    public void obtenerExpedientePropioSinSesionLanzaExcepcion()
            throws SQLException {
        Sesion.cerrarSesion();
        ExpedienteServicio.obtenerExpedientePropio();
    }
}