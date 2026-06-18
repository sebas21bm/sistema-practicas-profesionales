package sistemapracticasprofesionales.modelo.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 15/06/2026
 * Descripción: POJO que representa la información de un proyecto de practicas
 * profesionales del SPP.
 */
public class ProyectoPracticas {
    
    private int numProyecto;
    private String nombre;
    private String descripcion;
    private int cupo;
    private Date fechaFinalizacion;
    private int idResponsable;
    private boolean disponible;
    
    private OrganizacionVinculada organizacionVinculada;
    private ResponsableProyecto responsableProyecto;
    private List<Asignacion> asignaciones = new ArrayList<>();
    
    private String nombreEstudiante;
    private String nombreOrganizacionVinculada;
    
    public int getNumProyecto() {
        return numProyecto;
    }

    public void setNumProyecto(int numProyecto) {
        this.numProyecto = numProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public OrganizacionVinculada getOrganizacionVinculada() {
        return organizacionVinculada;
    }

    public void setOrganizacionVinculada(
            OrganizacionVinculada organizacionVinculada) {
        this.organizacionVinculada = organizacionVinculada;
    }

    public ResponsableProyecto getResponsableProyecto() {
        return responsableProyecto;
    }

    public void setResponsableProyecto(
            ResponsableProyecto responsableProyecto) {
        this.responsableProyecto = responsableProyecto;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreOrganizacionVinculada() {
        return nombreOrganizacionVinculada;
    }

    public void setNombreOrganizacionVinculada(String nombreOrganizacionVinculada) {
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
    }
    
    
    
}
