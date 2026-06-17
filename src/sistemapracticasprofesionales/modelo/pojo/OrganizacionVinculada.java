package sistemapracticasprofesionales.modelo.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: POJO que representa la información de una 
 * organización vinculada del SPP.
 */
public class OrganizacionVinculada {
    
    private int numOrganizacionVinculada;
    private String nombre;
    private String calle;
    private String colonia;
    private String codigoPostal;
    private String telefono;
    private String correo;
    private String tipo;
    private boolean estado;
    
    private List<ResponsableProyecto> responsables = new ArrayList<>();
    private List<ProyectoPracticas> proyectos = new ArrayList<>();

    public int getNumOrganizacionVinculada() {
        return numOrganizacionVinculada;
    }

    public void setNumOrganizacionVinculada(int numOrganizacionVinculada) {
        this.numOrganizacionVinculada = numOrganizacionVinculada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    public String getUbicacionCompleta() {
        return calle + ", " + colonia + ", C.P. " + codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<ResponsableProyecto> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<ResponsableProyecto> responsables) {
        this.responsables = responsables;
    }

    public List<ProyectoPracticas> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<ProyectoPracticas> proyectos) {
        this.proyectos = proyectos;
    }
    
}
