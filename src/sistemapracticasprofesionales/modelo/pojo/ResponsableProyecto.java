package sistemapracticasprofesionales.modelo.pojo;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 16/06/2026
 * Descripción: POJO que representa la información de un responsable de proyecto
 * perteneciente a una organización vinculada.
 */
public class ResponsableProyecto {
    
    private int idResponsable;
    private String nombre;
    private String paterno;
    private String materno;
    private String telefono;
    private String correo;
    private String puesto;
    private int numOrganizacionVinculada;

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
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

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public int getNumOrganizacionVinculada() {
        return numOrganizacionVinculada;
    }

    public void setNumOrganizacionVinculada(int numOrganizacionVinculada) {
        this.numOrganizacionVinculada = numOrganizacionVinculada;
    }
    
    public String getNombreCompleto() {
        String nombreCompleto = nombre + " " + paterno;

        if (materno != null && !materno.trim().isEmpty()) {
            nombreCompleto += " " + materno;
        }

        return nombreCompleto;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
    
}
