package sistemapracticasprofesionales.modelo.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: POJO que representa un detalle de evaluación de un documento
 *              dentro del expediente de un estudiante.
 */
public class DetalleEvaluacion {

    private static final String CLASIFICACION_DOCUMENTO_INICIAL =
            "Documento inicial";
    private static final String PRIMERA_EVALUACION_PROFESOR =
            "Primera evaluación del profesor";
    private static final String SEGUNDA_EVALUACION_PROFESOR =
            "Segunda evaluación del profesor";

    private int idDetallesEvaluacion;
    private int idExpediente;
    private int idEntregaDocumento;
    private int idExperienciaEducativa;
    private int idDocumento;
    private Integer idArchivo;
    private String matricula;
    private String nombreEstudiante;
    private String tipoDocumento;
    private String clasificacion;
    private int numeroEntrega;
    private LocalDate fechaEntrega;
    private LocalDateTime fechaSubida;
    private String estado;
    private Double calificacion;
    private double valor;
    private double porcentajeObtenido;
    private String observaciones;
    private String nombreOriginal;
    private byte[] archivo;

    public int getIdDetallesEvaluacion() {
        return idDetallesEvaluacion;
    }

    public void setIdDetallesEvaluacion(int idDetallesEvaluacion) {
        this.idDetallesEvaluacion = idDetallesEvaluacion;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public int getIdEntregaDocumento() {
        return idEntregaDocumento;
    }

    public void setIdEntregaDocumento(int idEntregaDocumento) {
        this.idEntregaDocumento = idEntregaDocumento;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getNumeroEntrega() {
        return numeroEntrega;
    }

    public void setNumeroEntrega(int numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getPorcentajeObtenido() {
        return porcentajeObtenido;
    }

    public void setPorcentajeObtenido(double porcentajeObtenido) {
        this.porcentajeObtenido = porcentajeObtenido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getDocumentoEntrega() {
        if (numeroEntrega > 1) {
            return tipoDocumento + " - Entrega " + numeroEntrega;
        }

        return tipoDocumento;
    }

    public String getFechaEntregaTexto() {
        return fechaEntrega == null ? "Sin fecha" : fechaEntrega.toString();
    }

    public String getFechaSubidaTexto() {
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return fechaSubida == null
                ? "Sin archivo"
                : fechaSubida.format(formato);
    }

    public String getCalificacionTexto() {
        return calificacion == null
                ? ""
                : String.format("%.2f", calificacion);
    }

    public String getValorTexto() {
        return String.format("%.2f", valor);
    }

    public String getPorcentajeObtenidoTexto() {
        return String.format("%.2f", porcentajeObtenido);
    }

    public boolean esDocumentoInicial() {
        return CLASIFICACION_DOCUMENTO_INICIAL.equals(clasificacion);
    }

    public boolean esEvaluacionProfesor() {
        return PRIMERA_EVALUACION_PROFESOR.equals(tipoDocumento)
                || SEGUNDA_EVALUACION_PROFESOR.equals(tipoDocumento);
    }

    public boolean tieneArchivo() {
        return idArchivo != null;
    }
}
