package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.ProyectoPracticasDAO;
import sistemapracticasprofesionales.modelo.pojo.ProyectoPracticas;
import sistemapracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: Servicio encargado de validar las reglas de negocio de los
 * proyectos de prácticas profesionales y comunicarse con la capa DAO.
 */
public class ProyectoPracticasServicio {
    
    private static final String REGEX_NOMBRE_PROYECTO = "^.{1,100}$";
    private static final String REGEX_DESCRIPCION = "^.{1,150}$";
    private static final String REGEX_NOMBRE_RESPONSABLE = "^.{1,30}$";
    private static final String REGEX_APELLIDO_PATERNO = "^.{1,30}$";
    private static final String REGEX_APELLIDO_MATERNO = "^.{0,30}$";
    private static final String REGEX_TELEFONO = "^(?!0000000000)\\d{10}$";
    private static final String REGEX_CORREO =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_PUESTO = "^.{1,40}$";
    
    public static RespuestaOperacion registrarProyectoConResponsableNuevo(
            ProyectoPracticas proyectoPracticas,
            ResponsableProyecto responsableProyecto)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = validarProyecto(proyectoPracticas);
        
        if (respuesta.getError()) {
            return respuesta;
        }
        
        respuesta = validarResponsable(responsableProyecto);
        
        if (respuesta.getError()) {
            return respuesta;
        }
        
         if (ProyectoPracticasDAO.existeProyectoIgualEnOrganizacion(
                proyectoPracticas,
                 responsableProyecto.getNumOrganizacionVinculada())) {
            respuesta.setError(true);
            respuesta.setMensaje("- Ya existe un proyecto registrado "
                    + "con la misma información en esta organización "
                    + "vinculada.");
            return respuesta;
        }

        if (ProyectoPracticasDAO.existeResponsableIgual(
                responsableProyecto)) {
            respuesta.setError(true);
            respuesta.setMensaje("- Ya existe un responsable registrado "
                    + "con exactamente la misma información.");
            return respuesta;
        }
        
        return ProyectoPracticasDAO.registrarProyectoConResponsableNuevo(
                proyectoPracticas, responsableProyecto);
    }
    
    public static RespuestaOperacion registrarProyectoConResponsableExistente(
            ProyectoPracticas proyectoPracticas)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = validarProyecto(proyectoPracticas);
        
        if (respuesta.getError()) {
            return respuesta;
        }
        
        if (proyectoPracticas.getIdResponsable() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("- Debes seleccionar un responsable existente.");
            return respuesta;
        }
        
        if (ProyectoPracticasDAO.existeProyectoIgual(proyectoPracticas)) {
            respuesta.setError(true);
            respuesta.setMensaje("- Ya existe un proyecto registrado "
                    + "con exactamente la misma información.");
            return respuesta;
        }
        
        return ProyectoPracticasDAO.registrarProyectoConResponsableExistente(
                proyectoPracticas);
    }
    
    public static ArrayList<ResponsableProyecto>
            recuperarResponsablesPorOrganizacion(int numeroOrganizacion)
            throws SQLException, NullPointerException {
        
        return ProyectoPracticasDAO.obtenerResponsablesPorOrganizacion(
                numeroOrganizacion);
    }
    
    private static RespuestaOperacion validarProyecto(
            ProyectoPracticas proyectoPracticas) {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();
        
        if (proyectoPracticas == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió la información del proyecto.");
            return respuesta;
        }
        
        normalizarProyecto(proyectoPracticas);
        validarFormatoProyecto(proyectoPracticas, errores);
        
        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());
        
        return respuesta;
    }
    
    private static RespuestaOperacion validarResponsable(
            ResponsableProyecto responsableProyecto) {
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();
        
        if (responsableProyecto == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió la información del responsable.");
            return respuesta;
        }
        
        normalizarResponsable(responsableProyecto);
        validarFormatoResponsable(responsableProyecto, errores);
        
        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());
        
        return respuesta;
    }
    
    private static void normalizarProyecto(
            ProyectoPracticas proyectoPracticas) {
        
        proyectoPracticas.setNombre(limpiar(proyectoPracticas.getNombre()));
        proyectoPracticas.setDescripcion(
                limpiar(proyectoPracticas.getDescripcion()));
        proyectoPracticas.setDisponible(true);
    }
    
    private static void normalizarResponsable(
            ResponsableProyecto responsableProyecto) {
        
        responsableProyecto.setNombre(
                limpiar(responsableProyecto.getNombre()));
        responsableProyecto.setPaterno(
                limpiar(responsableProyecto.getPaterno()));
        responsableProyecto.setMaterno(
                limpiar(responsableProyecto.getMaterno()));
        responsableProyecto.setTelefono(
                limpiar(responsableProyecto.getTelefono()));
        responsableProyecto.setCorreo(
                limpiar(responsableProyecto.getCorreo()));
        responsableProyecto.setPuesto(
                limpiar(responsableProyecto.getPuesto()));
    }
    
    private static void validarFormatoProyecto(
            ProyectoPracticas proyectoPracticas, StringBuilder errores) {
        
        String nombreProyecto = proyectoPracticas.getNombre();
        if (nombreProyecto.isEmpty()
                || !nombreProyecto.matches(REGEX_NOMBRE_PROYECTO)) {
            agregarError(errores, "El nombre del proyecto es obligatorio "
                    + "y no puede exceder los 100 caracteres.");
        }
        
        String descripcion = proyectoPracticas.getDescripcion();
        if (descripcion.isEmpty()
                || !descripcion.matches(REGEX_DESCRIPCION)) {
            agregarError(errores, "La descripción del proyecto es obligatoria "
                    + "y no puede exceder los 150 caracteres.");
        }
        
        if (proyectoPracticas.getCupo() < 1) {
            agregarError(errores, "El cupo es obligatorio y debe ser mínimo 1.");
        }
        
        if (proyectoPracticas.getFechaFinalizacion() == null) {
            agregarError(errores, "La fecha de finalización es obligatoria.");
        }
        
        if (proyectoPracticas.getOrganizacionVinculada() == null
                || proyectoPracticas.getOrganizacionVinculada()
                        .getNumOrganizacionVinculada() <= 0) {
            agregarError(errores, "Debes seleccionar una organización vinculada.");
        }
    }
    
    private static void validarFormatoResponsable(
            ResponsableProyecto responsableProyecto, StringBuilder errores) {
        
        String nombreResponsable = responsableProyecto.getNombre();
        if (nombreResponsable.isEmpty()
                || !nombreResponsable.matches(REGEX_NOMBRE_RESPONSABLE)) {
            agregarError(errores, "El nombre del responsable es obligatorio "
                    + "y no puede exceder los 30 caracteres.");
        }
        
        String apellidoPaterno = responsableProyecto.getPaterno();
        if (apellidoPaterno.isEmpty()
                || !apellidoPaterno.matches(REGEX_APELLIDO_PATERNO)) {
            agregarError(errores, "El apellido paterno del responsable es "
                    + "obligatorio y no puede exceder los 30 caracteres.");
        }
        
        String apellidoMaterno = responsableProyecto.getMaterno();
        if (!apellidoMaterno.matches(REGEX_APELLIDO_MATERNO)) {
            agregarError(errores, "El apellido materno del responsable no "
                    + "puede exceder los 30 caracteres.");
        }
        
        String telefono = responsableProyecto.getTelefono();
        if (telefono.isEmpty() || !telefono.matches(REGEX_TELEFONO)) {
            agregarError(errores, "El teléfono del responsable es obligatorio "
                    + "y debe cumplir con el formato de 10 dígitos.");
        }
        
        String correo = responsableProyecto.getCorreo();
        if (correo.isEmpty() || !correo.matches(REGEX_CORREO)
                || correo.length() > 45) {
            agregarError(errores, "El correo electrónico del responsable es "
                    + "obligatorio, no puede exceder los 45 caracteres "
                    + "y debe cumplir con el formato de correo electrónico.");
        }
        
        String puesto = responsableProyecto.getPuesto();
        if (puesto.isEmpty() || !puesto.matches(REGEX_PUESTO)) {
            agregarError(errores, "El puesto del responsable es obligatorio "
                    + "y no puede exceder los 40 caracteres.");
        }
        
        if (responsableProyecto.getNumOrganizacionVinculada() <= 0) {
            agregarError(errores, "El responsable debe estar asociado a una "
                    + "organización vinculada.");
        }
    }
    
    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
    
    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }
        
        errores.append("- ").append(mensaje);
    }
    
    public static ArrayList<ProyectoPracticas> recuperarListadoProyectos()
        throws SQLException, NullPointerException {
        return ProyectoPracticasDAO.obtenerProyectos();
    }
}