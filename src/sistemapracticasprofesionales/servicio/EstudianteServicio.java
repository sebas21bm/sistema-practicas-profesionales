package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemapracticasprofesionales.modelo.pojo.Estudiante;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio de estudiantes.
 */
public class EstudianteServicio {

    private static final String REGEX_MATRICULA = "^S\\d{8}$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";
    private static final String REGEX_CORREO_ESTUDIANTE =
            "^[A-Za-z0-9._%+-]+@estudiantes\\.uv\\.mx$";
    private static final String REGEX_CONTRASENIA =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)\\S{8,64}$";
    private static final String REGEX_NOMBRE_PERSONA =
        "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+"
        + "( [A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$";

    public static RespuestaOperacion registrarEstudiante(
            Estudiante estudiante) throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = validarEstudiante(estudiante);

        if (respuesta.getError()) {
            return respuesta;
        }

        return EstudianteDAO.registrarEstudiante(estudiante);
    }

    public static ArrayList<Estudiante> obtenerEstudiantes()
            throws SQLException, NullPointerException {
        return EstudianteDAO.obtenerEstudiantes();
    }
    
    public static ArrayList<Estudiante> buscarEstudiantesPorMatricula(
        String matricula) throws SQLException, NullPointerException {
        return EstudianteDAO.buscarEstudiantesPorMatricula(matricula);
    }

    public static ArrayList<Estudiante> buscarEstudiantesPorNombre(String nombre)
            throws SQLException, NullPointerException {
        return EstudianteDAO.buscarEstudiantesPorNombre(nombre);
    }

    private static RespuestaOperacion validarEstudiante(
            Estudiante estudiante) throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder("No es posible "
                + "continuar con el registro");

        if (estudiante == null) {
            respuesta.setError(true);
            respuesta.setMensaje(
                    "No se recibió la información del estudiante.");
            return respuesta;
        }

        limpiarDatosEstudiante(estudiante);
        validarCamposObligatorios(estudiante, errores);
        validarFormatoDatos(estudiante, errores);

        if (errores.length() == 0) {
            validarDatosExistentes(estudiante, errores);
        }

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void validarCamposObligatorios(
            Estudiante estudiante, StringBuilder errores) {
        if (estaVacio(estudiante.getMatricula())) {
            agregarError(errores, "La matrícula es obligatoria.");
        }

        if (estaVacio(estudiante.getNombre())) {
            agregarError(errores, "El nombre es obligatorio.");
        }

        if (estaVacio(estudiante.getApellidoPaterno())) {
            agregarError(errores, "El apellido paterno es obligatorio.");
        }

        if (estaVacio(estudiante.getTelefono())) {
            agregarError(errores, "El teléfono es obligatorio.");
        }

        if (estaVacio(estudiante.getCorreo())) {
            agregarError(errores, "El correo es obligatorio.");
        }

        if (estaVacio(estudiante.getIdUsuario())) {
            agregarError(errores, "El usuario es obligatorio.");
        }

        if (estaVacio(estudiante.getContrasenia())) {
            agregarError(errores, "La contraseña es obligatoria.");
        }
    }

    private static void validarFormatoDatos(
            Estudiante estudiante, StringBuilder errores) {
        if (!estaVacio(estudiante.getMatricula())
                && !(estudiante.getMatricula().matches(REGEX_MATRICULA))) {
            agregarError(errores,
                    "La matrícula debe iniciar con S y contener 8 dígitos.");
        }

        if (!estaVacio(estudiante.getMatricula())
                && estudiante.getMatricula().matches(REGEX_MATRICULA)
                && !esAnioMatriculaValido(estudiante.getMatricula())) {
            agregarError(errores,
                    "La matrícula debe corresponder a un año de ingreso "
                    + "de 3 a 5 años anteriores al año actual.");
        }

        validarFormatoNombre(estudiante.getNombre(), "nombre", errores);
        validarFormatoNombre(estudiante.getApellidoPaterno(),
                "apellido paterno", errores);
        validarFormatoNombre(estudiante.getApellidoMaterno(),
                "apellido materno", errores);

        if (!estaVacio(estudiante.getTelefono())
                && !(estudiante.getTelefono().matches(REGEX_TELEFONO))) {
            agregarError(errores,
                    "El teléfono debe contener 10 dígitos.");
        }

        if (!estaVacio(estudiante.getCorreo())
                && !(estudiante.getCorreo()
                        .matches(REGEX_CORREO_ESTUDIANTE))) {
            agregarError(errores,
                    "El correo debe tener formato válido y terminar en "
                    + "@estudiantes.uv.mx.");
        }

        if (!estaVacio(estudiante.getCorreo())
                && !cumpleLongitud(estudiante.getCorreo(), 27)) {
            agregarError(errores,
                    "El correo no debe superar 27 caracteres.");
        }

        if (!estaVacio(estudiante.getIdUsuario())
                && !cumpleLongitud(estudiante.getIdUsuario(), 20)) {
            agregarError(errores,
                    "El usuario no debe superar 20 caracteres.");
        }

        if (!estaVacio(estudiante.getContrasenia())
                && !(estudiante.getContrasenia()
                        .matches(REGEX_CONTRASENIA))) {
            agregarError(errores,
                    "La contraseña debe tener al menos 8 caracteres, "
                    + "mayúsculas, minúsculas, dígitos "
                    + "y no debe tener espacios.");
        }
    }

    private static void validarDatosExistentes(
            Estudiante estudiante, StringBuilder errores)
            throws SQLException, NullPointerException {
        if (EstudianteDAO.verificarMatriculaExistente(
                estudiante.getMatricula())) {
            agregarError(errores,
                    "La matrícula ya está registrada.");
        }

        if (EstudianteDAO.verificarCorreoEstudianteExistente(
                estudiante.getCorreo())) {
            agregarError(errores,
                    "El correo ya está asociado a una cuenta registrada.");
        }

        if (EstudianteDAO.verificarUsuarioExistente(
                estudiante.getIdUsuario())) {
            agregarError(errores,
                    "El usuario ya está registrado en el sistema.");
        }
    }

    private static void limpiarDatosEstudiante(Estudiante estudiante) {
        estudiante.setMatricula(
                limpiarTexto(estudiante.getMatricula()).toUpperCase());
        estudiante.setNombre(limpiarTexto(estudiante.getNombre()));
        estudiante.setApellidoPaterno(
                limpiarTexto(estudiante.getApellidoPaterno()));
        estudiante.setApellidoMaterno(
                limpiarTexto(estudiante.getApellidoMaterno()));
        estudiante.setTelefono(limpiarTexto(estudiante.getTelefono()));
        estudiante.setCorreo(
                limpiarTexto(estudiante.getCorreo()).toLowerCase());
        estudiante.setIdUsuario(limpiarTexto(estudiante.getIdUsuario()));
    }

    private static boolean esAnioMatriculaValido(String matricula) {
        int anioActual = Year.now().getValue();
        int anioIngreso = 2000 + Integer.parseInt(matricula.substring(1, 3));

        return anioIngreso >= anioActual - 5
                && anioIngreso <= anioActual - 3;
    }
    
    private static void validarFormatoNombre(
        String texto, String nombreCampo, StringBuilder errores) {
    if (!estaVacio(texto) && !cumpleLongitud(texto, 30)) {
        agregarError(errores,
                "El " + nombreCampo + " no debe superar 30 caracteres.");
    }

    if (!estaVacio(texto) && !texto.matches(REGEX_NOMBRE_PERSONA)) {
        agregarError(errores,
                "El " + nombreCampo
                + " debe contener solamente letras y espacios.");
    }
}

    private static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private static boolean cumpleLongitud(String texto, int longitudMaxima) {
        return texto != null && texto.length() <= longitudMaxima;
    }

    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }

        errores.append("- ").append(mensaje);
    }
}