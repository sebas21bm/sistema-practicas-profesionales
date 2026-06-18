package sistemapracticasprofesionales.modelo.pojo;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: POJO que representa el expediente de un estudiante para
 *              consulta del profesor.
 */
public class ExpedienteEstudiante {

    private int idExpediente;
    private int idEstudiante;
    private int idExperienciaEducativa;
    private int numProyecto;
    private boolean completo;
    private String matricula;
    private String nombreEstudiante;
    private String nombreProyecto;
    private Double calificacion;

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public int getNumProyecto() {
        return numProyecto;
    }

    public void setNumProyecto(int numProyecto) {
        this.numProyecto = numProyecto;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getCalificacionTexto() {
        if (calificacion == null) {
            return "Sin calcular";
        }

        return String.format("%.2f", calificacion);
    }

    public String getEstadoExpediente() {
        return completo ? "Completo" : "Incompleto";
    }
}