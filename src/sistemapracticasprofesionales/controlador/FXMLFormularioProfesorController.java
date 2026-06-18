package sistemapracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemapracticasprofesionales.modelo.pojo.Profesor;
import sistemapracticasprofesionales.modelo.pojo.RespuestaOperacion;
import sistemapracticasprofesionales.servicio.ProfesorServicio;
import sistemapracticasprofesionales.utilidades.Utilidades;

/*
 * Autor: Yarazareth Zacnite Ortiz Olmos
 * Fecha de creación: 15/06/2026
 * Descripción: Controlador del formulario para registrar profesores.
 */
public class FXMLFormularioProfesorController implements Initializable {

    @FXML
    private TextField txt_numeroEmpleado;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_apellidoPaterno;
    @FXML
    private TextField txt_apellidoMaterno;
    @FXML
    private TextField txt_telefono;
    @FXML
    private TextField txt_correo;
    @FXML
    private TextField txt_usuario;
    @FXML
    private PasswordField pf_contrasenia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (sonCamposValidos()) {
            confirmarRegistro();
        } else {
            mostrarCamposObligatorios();
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }

    private boolean sonCamposValidos() {
        if (txt_numeroEmpleado.getText().trim().isEmpty()) {
            return false;
        }

        if (txt_nombre.getText().trim().isEmpty()) {
            return false;
        }

        if (txt_apellidoPaterno.getText().trim().isEmpty()) {
            return false;
        }

        if (txt_telefono.getText().trim().isEmpty()) {
            return false;
        }

        if (txt_correo.getText().trim().isEmpty()) {
            return false;
        }

        if (txt_usuario.getText().trim().isEmpty()) {
            return false;
        }

        return !pf_contrasenia.getText().trim().isEmpty();
    }

    private void confirmarRegistro() {
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmación de registro");
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(
                "¿Seguro que desea registrar el profesor al sistema?");

        Optional<ButtonType> respuesta = alertaConfirmacion.showAndWait();

        if (respuesta.isPresent()
                && respuesta.get() == ButtonType.OK) {
            guardarProfesor();
        } else {
            limpiarFormulario();
        }
    }

    private void guardarProfesor() {
        try {
            Profesor profesor = obtenerProfesor();
            RespuestaOperacion respuesta =
                    ProfesorServicio.registrarProfesor(profesor);

            if (!respuesta.getError()) {
                Utilidades.mostrarAlertaSimple(
                        "Registro exitoso",
                        "El profesor se ha registrado correctamente.",
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple(
                        "Datos inválidos",
                        respuesta.getMensaje() + "\n Ingrese los datos "
                                + "nuevamente para poder continuar",
                        Alert.AlertType.WARNING);
            }
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error al guardar",
                    "No es posible registrar al profesor. "
                    + "Error al guardar el registro. Intente nuevamente.",
                    Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "No fue posible obtener la información del profesor.",
                    Alert.AlertType.ERROR);
        }
    }

    private Profesor obtenerProfesor() {
        Profesor profesor = new Profesor();

        profesor.setNumeroEmpleado(txt_numeroEmpleado.getText());
        profesor.setNombre(txt_nombre.getText());
        profesor.setApellidoPaterno(txt_apellidoPaterno.getText());
        profesor.setApellidoMaterno(txt_apellidoMaterno.getText());
        profesor.setTelefono(txt_telefono.getText());
        profesor.setCorreo(txt_correo.getText());
        profesor.setIdUsuario(txt_usuario.getText());
        profesor.setContrasenia(pf_contrasenia.getText());

        return profesor;
    }

    private void mostrarCamposObligatorios() {
        String mensaje =
                "No es posible continuar con el registro. Existen campos "
                + "obligatorios vacíos. Ingrese los datos faltantes para "
                + "poder continuar.";

        Utilidades.mostrarAlertaSimple(
                "Campos obligatorios",
                mensaje,
                Alert.AlertType.WARNING);
    }

    private void limpiarFormulario() {
        txt_numeroEmpleado.clear();
        txt_nombre.clear();
        txt_apellidoPaterno.clear();
        txt_apellidoMaterno.clear();
        txt_telefono.clear();
        txt_correo.clear();
        txt_usuario.clear();
        pf_contrasenia.clear();
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) txt_nombre.getScene().getWindow();
        escenario.close();
    }
}