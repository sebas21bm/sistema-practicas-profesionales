package sistemapracticasprofesionales.modelo.pojo;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 14/06/2026
 * Descripción: POJO utilizado para regresar el resultado de una operación.
 */
public class RespuestaOperacion {

    private boolean error;
    private String mensaje;

    public RespuestaOperacion() {
    }

    public RespuestaOperacion(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}