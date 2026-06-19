package sistemapracticasprofesionales.modelo.pojo;

/**
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 14/06/2026
 * Descripción: POJO que representa la información de un coordinador dentro del SPP.
 */
public class Coordinador {
    private int idPersonalAcademico;
    private int numeroEmpleado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String correo;
    private boolean activo;
    
    private String idUsuario;
    private String contrasenia;

    public Coordinador() {}

    public int getIdPersonalAcademico() {
        return idPersonalAcademico;
    }

    public void setIdPersonalAcademico(int idPersonalAcademico) {
        this.idPersonalAcademico = idPersonalAcademico;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
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

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    
    @Override
    public String toString() {
        return nombre + apellidoPaterno + apellidoMaterno;
    }
}
