package sistemapracticasprofesionales.modelo.pojo;

import java.util.Date;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 16/06/2026
 * Descripción: POJO para representar los datos de las asignaciones de proyectos
 * de practicas profesionales.
 */
public class Asignacion {
    
    private int numProyecto;
    private int idEstudiante;
    private int avanceCalificacion;
    private int horasAcumuladas;
    private String estado;
    private int idPersonalAcademico;
    
    private String nombreProyecto;
    private String descripcionProyecto;
    private Date fechaFinalizacion;
    private String organizacionVinculada;
    private String nombreResponsable;
    private String correoResponsable;
    private String nombreEstudiante;
    private String matricula;
    private String correoEstudiante;
    private String nombreCoordinador;

    public int getNumProyecto() {
        return numProyecto;
    }

    public void setNumProyecto(int numProyecto) {
        this.numProyecto = numProyecto;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getAvanceCalificacion() {
        return avanceCalificacion;
    }

    public void setAvanceCalificacion(int avanceCalificacion) {
        this.avanceCalificacion = avanceCalificacion;
    }

    public int getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(int horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdPersonalAcademico() {
        return idPersonalAcademico;
    }

    public void setIdPersonalAcademico(int idPersonalAcademico) {
        this.idPersonalAcademico = idPersonalAcademico;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcionProyecto() {
        return descripcionProyecto;
    }

    public void setDescripcionProyecto(String descripcionProyecto) {
        this.descripcionProyecto = descripcionProyecto;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getOrganizacionVinculada() {
        return organizacionVinculada;
    }

    public void setOrganizacionVinculada(String organizacionVinculada) {
        this.organizacionVinculada = organizacionVinculada;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getCorreoResponsable() {
        return correoResponsable;
    }

    public void setCorreoResponsable(String correoResponsable) {
        this.correoResponsable = correoResponsable;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getNombreCoordinador() {
        return nombreCoordinador;
    }

    public void setNombreCoordinador(String nombreCoordinador) {
        this.nombreCoordinador = nombreCoordinador;
    }
    
}