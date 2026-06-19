package sistemapracticasprofesionales.modelo.pojo;

import java.time.LocalDate;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 17/06/2026
 * Descripción: POJO que representa una entrega programada de documento.
 */
public class EntregaDocumento {

    private int idEntregaDocumento;
    private int idExperienciaEducativa;
    private int idDocumento;
    private String tipoDocumento;
    private double valor;
    private int numeroEntrega;
    private LocalDate fechaEntrega;

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

    public String getDescripcionDocumento() {
        return tipoDocumento + " - Entrega " + numeroEntrega;
    }

    public String getFechaEntregaTexto() {
        return fechaEntrega == null ? "Sin fecha" : fechaEntrega.toString();
    }

    public String getEstadoFecha() {
        return fechaEntrega == null ? "Sin asignar" : "Asignada";
    }

    @Override
    public String toString() {
        return getDescripcionDocumento();
    }
}