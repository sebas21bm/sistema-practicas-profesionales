package sistemapracticasprofesionales.modelo.pojo;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: POJO que representa un documento entregado dentro del
 *              expediente de un estudiante.
 */
public class DocumentoExpediente {

    private int idDetallesEvaluacion;
    private int numeroEntrega;
    private String fechaEntrega;
    private Double calificacion;
    private String observaciones;
    private String estado;
    private Integer idArchivo;
    private int idDocumento;
    private int idExpediente;
    private String tipoDocumento;
    private double valor;
    private String clasificacion;
    private int cantidadEntregar;
    private String nombreOriginal;
    private byte[] archivo;

    public DocumentoExpediente() {
    }

    public int getIdDetallesEvaluacion() {
        return idDetallesEvaluacion;
    }

    public void setIdDetallesEvaluacion(int idDetallesEvaluacion) {
        this.idDetallesEvaluacion = idDetallesEvaluacion;
    }

    public int getNumeroEntrega() {
        return numeroEntrega;
    }

    public void setNumeroEntrega(int numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado == null || estado.trim().isEmpty()
                ? "En revisión" : estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getCantidadEntregar() {
        return cantidadEntregar;
    }

    public void setCantidadEntregar(int cantidadEntregar) {
        this.cantidadEntregar = cantidadEntregar;
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

    public String getValorTexto() {
        return formatearPorcentaje(valor);
    }

    public String getCalificacionTexto() {
        if (calificacion == null) {
            return "";
        }

        return String.valueOf(calificacion);
    }

    public String getPorcentajeObtenidoTexto() {
        if (calificacion == null) {
            return "0%";
        }

        if (calificacion > 0) {
            return formatearPorcentaje(valor);
        }

        return "0%";
    }

    public boolean getTieneArchivo() {
        return idArchivo != null;
    }

    private String formatearPorcentaje(double porcentaje) {
        if (porcentaje % 1 == 0) {
            return String.valueOf((int) porcentaje) + "%";
        }

        return String.valueOf(porcentaje) + "%";
    }

    @Override
    public String toString() {
        return tipoDocumento;
    }
}