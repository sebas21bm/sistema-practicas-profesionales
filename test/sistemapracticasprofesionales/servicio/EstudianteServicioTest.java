package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.time.Year;
import org.junit.Test;
import static org.junit.Assert.*;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 18/06/2026
 * Descripción: Pruebas unitarias de validación para EstudianteServicio.
 */
public class EstudianteServicioTest {

    @Test
    public void registrarEstudianteConObjetoNuloRegresaError()
            throws SQLException {
        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(null);

        assertTrue(respuesta.getError());
        assertEquals("No se recibió la información del estudiante.",
                respuesta.getMensaje());
    }

    @Test
    public void registrarEstudianteConCamposObligatoriosVaciosRegresaErrores()
            throws SQLException {
        Estudiante estudiante = new Estudiante();

        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(estudiante);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains("La matrícula es obligatoria."));
        assertTrue(respuesta.getMensaje().contains("El nombre es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El apellido paterno es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El teléfono es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El correo es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("El usuario es obligatorio."));
        assertTrue(respuesta.getMensaje().contains("La contraseña es obligatoria."));
    }

    @Test
    public void registrarEstudianteConNombreConNumerosRegresaErrorFormato()
            throws SQLException {
        Estudiante estudiante = crearEstudianteBase();
        estudiante.setNombre("Juan 123");

        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(estudiante);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El nombre debe contener solamente letras y espacios."));
    }

    @Test
    public void registrarEstudianteConCorreoNoInstitucionalRegresaError()
            throws SQLException {
        Estudiante estudiante = crearEstudianteBase();
        estudiante.setCorreo("estudiante@gmail.com");

        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(estudiante);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "El correo debe tener formato válido y terminar en @estudiantes.uv.mx."));
    }

    @Test
    public void registrarEstudianteConContraseniaConEspaciosRegresaError()
            throws SQLException {
        Estudiante estudiante = crearEstudianteBase();
        estudiante.setContrasenia("Abc 12345");

        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(estudiante);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "La contraseña debe tener al menos 8 caracteres"));
    }

    @Test
    public void registrarEstudianteConMatriculaDeAnioFueraDeRangoRegresaError()
            throws SQLException {
        Estudiante estudiante = crearEstudianteBase();
        int anioFueraRango = Year.now().getValue() - 2;
        estudiante.setMatricula(String.format("S%02d123456", anioFueraRango % 100));

        RespuestaOperacion respuesta =
                EstudianteServicio.registrarEstudiante(estudiante);

        assertTrue(respuesta.getError());
        assertTrue(respuesta.getMensaje().contains(
                "La matrícula debe corresponder a un año de ingreso"));
    }

    private Estudiante crearEstudianteBase() {
        Estudiante estudiante = new Estudiante();
        int anioValido = Year.now().getValue() - 4;
        estudiante.setMatricula(String.format("S%02d123456", anioValido % 100));
        estudiante.setNombre("Juan Carlos");
        estudiante.setApellidoPaterno("Pérez");
        estudiante.setApellidoMaterno("López");
        estudiante.setTelefono("2281234567");
        estudiante.setCorreo("a@estudiantes.uv.mx");
        estudiante.setIdUsuario("estudiante1");
        estudiante.setContrasenia("Abcdef12");
        return estudiante;
    }
}
