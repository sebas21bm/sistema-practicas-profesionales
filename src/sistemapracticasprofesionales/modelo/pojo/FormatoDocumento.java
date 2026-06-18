package sistemapracticasprofesionales.modelo.pojo;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: POJO que representa un formato de documento del SPP.
 */
public class FormatoDocumento {

    private int idFormatoDocumento;
    private int idExperienciaEducativa;
    private int idDocumento;
    private String tipoDocumento;
    private Integer idArchivo;
    private String nombreOriginal;
    private byte[] archivo;

    public FormatoDocumento() {}

    public int getIdFormatoDocumento() {
        return idFormatoDocumento;
    }

    public void setIdFormatoDocumento(int idFormatoDocumento) {
        this.idFormatoDocumento = idFormatoDocumento;
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

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
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

    public String getEstadoFormato() {
        return idArchivo != null ? "Cargado" : "Sin formato";
    }
    

    @Override
    public String toString() {
        return tipoDocumento;
    }
}