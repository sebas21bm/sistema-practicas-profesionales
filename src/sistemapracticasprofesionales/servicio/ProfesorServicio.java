package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.ProfesorDAO;
import sistemapracticasprofesionales.modelo.pojo.Profesor;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Servicio encargado de validar reglas de negocio de profesores.
 */
public class ProfesorServicio {

    private static final String REGEX_NUMERO_EMPLEADO = "^\\d{5}$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";
    private static final String REGEX_CORREO_PROFESOR =
            "^[A-Za-z0-9._%+-]+@uv\\.mx$";
    private static final String REGEX_CONTRASENIA =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)\\S{8,64}$";
    private static final String REGEX_NOMBRE_PERSONA =
        "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+"
        + "( [A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$";

    public static RespuestaOperacion registrarProfesor(Profesor profesor)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = validarProfesor(profesor);

        if (respuesta.getError()) {
            return respuesta;
        }

        return ProfesorDAO.registrarProfesor(profesor);
    }

    public static ArrayList<Profesor> obtenerProfesores()
            throws SQLException, NullPointerException {
        return ProfesorDAO.obtenerProfesores();
    }

    public static ArrayList<Profesor> buscarProfesoresPorNumeroEmpleado(
        String numeroEmpleado) throws SQLException, NullPointerException {
        return ProfesorDAO.buscarProfesoresPorNumeroEmpleado(numeroEmpleado);
    }

    public static ArrayList<Profesor> buscarProfesoresPorNombre(String nombre)
            throws SQLException, NullPointerException {
        return ProfesorDAO.buscarProfesoresPorNombre(nombre);
    }
    
    private static RespuestaOperacion validarProfesor(Profesor profesor)
            throws SQLException, NullPointerException {
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores =  new StringBuilder("No es posible "
                + "continuar con el registro");

        if (profesor == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió la información del profesor.");
            return respuesta;
        }

        limpiarDatosProfesor(profesor);
        validarCamposObligatorios(profesor, errores);
        validarFormatoDatos(profesor, errores);

        if (errores.length() == 0) {
            validarDatosExistentes(profesor, errores);
        }

        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }

    private static void validarCamposObligatorios(
            Profesor profesor, StringBuilder errores) {
        if (estaVacio(profesor.getNumeroEmpleado())) {
            agregarError(errores, "El número de empleado es obligatorio.");
        }

        if (estaVacio(profesor.getNombre())) {
            agregarError(errores, "El nombre es obligatorio.");
        }

        if (estaVacio(profesor.getApellidoPaterno())) {
            agregarError(errores, "El apellido paterno es obligatorio.");
        }

        if (estaVacio(profesor.getTelefono())) {
            agregarError(errores, "El teléfono es obligatorio.");
        }

        if (estaVacio(profesor.getCorreo())) {
            agregarError(errores, "El correo es obligatorio.");
        }

        if (estaVacio(profesor.getIdUsuario())) {
            agregarError(errores, "El usuario es obligatorio.");
        }

        if (estaVacio(profesor.getContrasenia())) {
            agregarError(errores, "La contraseña es obligatoria.");
        }
    }

    private static void validarFormatoDatos(
            Profesor profesor, StringBuilder errores) {
        if (!estaVacio(profesor.getNumeroEmpleado())
                && !(profesor.getNumeroEmpleado()
                        .matches(REGEX_NUMERO_EMPLEADO))) {
            agregarError(errores,
                    "El número de empleado debe contener 5 dígitos.");
        }

        validarFormatoNombre(profesor.getNombre(), "nombre", errores);
        validarFormatoNombre(profesor.getApellidoPaterno(),
                        "apellido paterno", errores);
        validarFormatoNombre(profesor.getApellidoMaterno(),
                        "apellido materno", errores);

        if (!estaVacio(profesor.getTelefono())
                && !(profesor.getTelefono().matches(REGEX_TELEFONO))) {
            agregarError(errores,
                    "El teléfono debe contener 10 dígitos.");
        }

        if (!estaVacio(profesor.getCorreo())
                && !(profesor.getCorreo().matches(REGEX_CORREO_PROFESOR))) {
            agregarError(errores,
                    "El correo debe tener formato válido y terminar en @uv.mx.");
        }

        if (!estaVacio(profesor.getCorreo())
                && !cumpleLongitud(profesor.getCorreo(), 40)) {
            agregarError(errores,
                    "El correo no debe superar 40 caracteres.");
        }

        if (!estaVacio(profesor.getIdUsuario())
                && !cumpleLongitud(profesor.getIdUsuario(), 20)) {
            agregarError(errores,
                    "El usuario no debe superar 20 caracteres.");
        }

        if (!estaVacio(profesor.getContrasenia())
                && !(profesor.getContrasenia().matches(REGEX_CONTRASENIA))) {
            agregarError(errores,
                    "La contraseña debe tener al menos 8 caracteres, "
                    + "mayúsculas, minúsculas, dígitos y no contener "
                    + "espacios.");
        }
    }

    private static void validarDatosExistentes(
            Profesor profesor, StringBuilder errores)
            throws SQLException, NullPointerException {
        if (ProfesorDAO.verificarNumeroEmpleadoExistente(
                profesor.getNumeroEmpleado())) {
            agregarError(errores,
                    "El número de empleado ya está registrado.");
        }

        if (ProfesorDAO.verificarCorreoProfesorExistente(
                profesor.getCorreo())) {
            agregarError(errores,
                    "El correo ya está asociado a una cuenta registrada.");
        }

        if (ProfesorDAO.verificarUsuarioExistente(
                profesor.getIdUsuario())) {
            agregarError(errores,
                    "El usuario ya está registrado en el sistema.");
        }
    }

    private static void limpiarDatosProfesor(Profesor profesor) {
        profesor.setNumeroEmpleado(
                limpiarTexto(profesor.getNumeroEmpleado()));
        profesor.setNombre(limpiarTexto(profesor.getNombre()));
        profesor.setApellidoPaterno(
                limpiarTexto(profesor.getApellidoPaterno()));
        profesor.setApellidoMaterno(
                limpiarTexto(profesor.getApellidoMaterno()));
        profesor.setTelefono(limpiarTexto(profesor.getTelefono()));
        profesor.setCorreo(limpiarTexto(profesor.getCorreo()).toLowerCase());
        profesor.setIdUsuario(limpiarTexto(profesor.getIdUsuario()));
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