package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.Profesor;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para ProfesorServicio.
 */
public class ProfesorServicioTest {

    @Test
    public void registrarProfesorConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del profesor.",
                respuesta.getMensaje());
    }

    @Test
    public void registrarProfesorConCamposObligatoriosVaciosRegresaErrores()
            throws SQLException {
        Profesor profesor = new Profesor();

        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(profesor);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains("El número de empleado es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El nombre es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El apellido paterno es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El teléfono es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El correo es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El usuario es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("La contraseña es obligatoria."));
    }

    @Test
    public void registrarProfesorConNumeroEmpleadoInvalidoRegresaError()
            throws SQLException {
        Profesor profesor = crearProfesorBase();
        profesor.setNumeroEmpleado("12A45");

        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(profesor);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El número de empleado debe contener 5 dígitos."));
    }

    @Test
    public void registrarProfesorConNombreConNumerosRegresaErrorFormato()
            throws SQLException {
        Profesor profesor = crearProfesorBase();
        profesor.setNombre("Laura 2");

        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(profesor);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El nombre debe contener solamente letras y espacios."));
    }

    @Test
    public void registrarProfesorConCorreoNoUvRegresaError()
            throws SQLException {
        Profesor profesor = crearProfesorBase();
        profesor.setCorreo("profesor@gmail.com");

        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(profesor);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El correo debe tener formato válido y terminar en @uv.mx."));
    }

    @Test
    public void registrarProfesorConContraseniaSinMayusculaRegresaError()
            throws SQLException {
        Profesor profesor = crearProfesorBase();
        profesor.setContrasenia("abcdef12");

        RespuestaOperacion respuesta = ProfesorServicio.registrarProfesor(profesor);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "La contraseña debe tener al menos 8 caracteres"));
    }

    private Profesor crearProfesorBase() {
        Profesor profesor = new Profesor();
        profesor.setNumeroEmpleado("12345");
        profesor.setNombre("Laura");
        profesor.setApellidoPaterno("García");
        profesor.setApellidoMaterno("Mora");
        profesor.setTelefono("2281234567");
        profesor.setCorreo("lgm@uv.mx");
        profesor.setIdUsuario("profesor1");
        profesor.setContrasenia("Abcdef12");
        return profesor;
    }
}