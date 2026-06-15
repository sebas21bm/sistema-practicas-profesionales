package sistemapracticasprofesionales.modelo.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sistemapracticasprofesionales.excepciones.UsuarioNoEncontradoException;
import sistemapracticasprofesionales.modelo.pojo.Rol;
import sistemapracticasprofesionales.modelo.pojo.Usuario;
import sistemapracticasprofesionales.modelo.ConexionBD;
import sistemapracticasprofesionales.utilidades.Constantes;
import sistemapracticasprofesionales.utilidades.Utilidades;


/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase creada para la autenticación del usuario dentro del sistema
 */
public class AutenticacionDAO {
    
    public static Usuario validarSesionUsuario(String usuario, String contrasenia) 
            throws SQLException, NullPointerException, NoSuchAlgorithmException{
        
        Usuario usuarioLogin = new Usuario();

        try (Connection conexionBD = ConexionBD.crearParaAutenticacion()){
            if (conexionBD == null){
                throw new SQLException(Constantes.MSJ_SIN_CONEXION_BD);
            }
            
            byte[] contraseniaCifrada = Utilidades.cifrarContrasenia(contrasenia);
            
            String consulta = "SELECT id_usuario, contrasenia, id_rol FROM cuenta "
                    + "WHERE id_usuario = ? AND contrasenia = ?;";
            PreparedStatement sentenciaBD = conexionBD.prepareStatement(consulta);
            sentenciaBD.setString(1, usuario);
            sentenciaBD.setBytes(2, contraseniaCifrada);
            ResultSet resultado = sentenciaBD.executeQuery();
            
            
            if (!resultado.next()){
                throw new UsuarioNoEncontradoException("Usuario no encontrado. "
                        + "El usuario y/o contraseña no coinciden.");
            }
            
            usuarioLogin.setNombreUsuario(resultado.
                    getString("id_usuario"));
            
            int idRol = resultado.getInt("id_rol");
            switch (idRol) {
                case 1:
                    usuarioLogin.setRolUsuario(Rol.Estudiante);
                    break;
                case 2:
                    usuarioLogin.setRolUsuario(Rol.Profesor);
                    break;
                case 3:
                    usuarioLogin.setRolUsuario(Rol.Coordinador);
                    break;
                case 4:
                    usuarioLogin.setRolUsuario(Rol.Administrador);
                    break;
                default:
                    throw new NullPointerException();
            }
            
            return usuarioLogin;
        }
    }
}
