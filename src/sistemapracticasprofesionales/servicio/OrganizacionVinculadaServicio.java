package sistemapracticasprofesionales.servicio;

import java.sql.SQLException;
import java.util.ArrayList;
import sistemapracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemapracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 16/06/2026
 * Descripción: Servicio encargado de validar las reglas de negocio de las
 * organizaciones vinculadas y comunicarse con la capa DAO.
 */
public class OrganizacionVinculadaServicio {
    
    private static final String REGEX_NOMBRE_ORGANIZACION = "^.{1,60}$";
    private static final String REGEX_CALLE = "^.{1,40}$";
    private static final String REGEX_COLONIA = "^.{1,30}$";
    private static final String REGEX_CODIGO_POSTAL = "^\\d{5}$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";
    private static final String REGEX_CORREO =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    public static RespuestaOperacion registrarOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada)
            throws SQLException, NullPointerException {
        
        RespuestaOperacion respuesta = validarOrganizacionVinculada(
                organizacionVinculada);
        
        if (respuesta.getError()){
            return respuesta;
        }
        
        if (OrganizacionVinculadaDAO.existeOrganizacionVinculadaIgual(
                organizacionVinculada)) {
            respuesta.setError(true);
            respuesta.setMensaje("- Ya existe una organización vinculada "
                    + "registrada con exactamente la misma información.");
            return respuesta;
        }
        
        return OrganizacionVinculadaDAO.registrarOrganizacionVinculada(
                organizacionVinculada);
    }
    
    public static ArrayList<OrganizacionVinculada> recuperarListadoOrganizacionesVinculadas() 
           throws SQLException, NullPointerException {
        return OrganizacionVinculadaDAO.obtenerOrganizaciones();
    }
    
    public static OrganizacionVinculada recuperarOrganizacionCompleta(
            int numeroOrganizacion) throws SQLException, NullPointerException {
        return OrganizacionVinculadaDAO.obtenerOrganizacionCompleta(numeroOrganizacion);
    }
    
    public static RespuestaOperacion actualizarOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada) 
            throws SQLException, NullPointerException{
        
        RespuestaOperacion respuesta = validarOrganizacionVinculada(
                organizacionVinculada);
        
        if (respuesta.getError()){
            return respuesta;
        }
        
        return OrganizacionVinculadaDAO.actualizarOrganizacionVinculada(
                organizacionVinculada);
        
    }
    
    private static RespuestaOperacion validarOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada){
        
        RespuestaOperacion respuesta = new RespuestaOperacion();
        StringBuilder errores = new StringBuilder();
        
        if (organizacionVinculada == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió la información de la "
                    + "organización vinculada");
            return respuesta;
        }
        
        normalizarDatos(organizacionVinculada);
        validarFormatoDatos(organizacionVinculada, errores);
        
        respuesta.setError(errores.length() > 0);
        respuesta.setMensaje(errores.toString());

        return respuesta;
    }
    
    private static void normalizarDatos(OrganizacionVinculada organizacionVinculada) {

        organizacionVinculada.setNombre(
                limpiar(organizacionVinculada.getNombre()));
        organizacionVinculada.setCalle(
                limpiar(organizacionVinculada.getCalle()));
        organizacionVinculada.setColonia(
                limpiar(organizacionVinculada.getColonia()));
        organizacionVinculada.setCodigoPostal(
                limpiar(organizacionVinculada.getCodigoPostal()));
        organizacionVinculada.setTelefono(
                limpiar(organizacionVinculada.getTelefono()));
        organizacionVinculada.setCorreo(
                limpiar(organizacionVinculada.getCorreo()));
        organizacionVinculada.setTipo(
                limpiar(organizacionVinculada.getTipo()));
    }

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
    
    private static void validarFormatoDatos(
            OrganizacionVinculada organizacionVinculada, StringBuilder errores) {
       
        String nombreOrganizacion = organizacionVinculada.getNombre();
        if (nombreOrganizacion.isEmpty()
                || !nombreOrganizacion.matches(REGEX_NOMBRE_ORGANIZACION)) {
            agregarError(errores, "El nombre de la OrganizacionVinculada no "
                    + "puede ir vacío ni exceder los 60 caracteres.");
        }

        String calle = organizacionVinculada.getCalle();
        if (calle.isEmpty() || !calle.matches(REGEX_CALLE)) {
            agregarError(errores, "El registro de la calle es obligatorio "
                    + "y no puede exceder los 40 caracteres.");
        }

        String colonia = organizacionVinculada.getColonia();
        if (colonia.isEmpty() || !colonia.matches(REGEX_COLONIA)) {
            agregarError(errores, "El registro de la colonia es obligatorio "
                    + "y no puede exceder los 30 caracteres.");
        }

        String codigoPostal = organizacionVinculada.getCodigoPostal();
        if (codigoPostal.isEmpty()
                || !codigoPostal.matches(REGEX_CODIGO_POSTAL)) {
            agregarError(errores, "El registro del código postal es obligatorio "
                    + "y debe contener exactamente 5 dígitos.");
        }

        String telefono = organizacionVinculada.getTelefono();
        if (telefono.isEmpty()
                || !telefono.matches(REGEX_TELEFONO)) {
            agregarError(errores, "El registro del teléfono es obligatorio "
                    + "y debe cumplir el formato de 10 dígitos.");
        }

        String correoElectronico = organizacionVinculada.getCorreo();
        if (correoElectronico.isEmpty()
                || !correoElectronico.matches(REGEX_CORREO)
                || correoElectronico.length() > 60) {
            agregarError(errores, "El registro del correo electrónico es "
                    + "obligatorio, no puede exceder de los 60 caracteres "
                    + "y debe cumplir con el formato de correo electrónico.");
        }

        String tipo = organizacionVinculada.getTipo();
        if (tipo.isEmpty()
                || (!tipo.equals("Pública") && !tipo.equals("Privada"))) {
            agregarError(errores, "El tipo de organización es obligatorio "
                    + "y debe ser Pública o Privada.");
        }
    }
    
    private static void agregarError(StringBuilder errores, String mensaje) {
        if (errores.length() > 0) {
            errores.append("\n");
        }

        errores.append("- ").append(mensaje);
    }
}
