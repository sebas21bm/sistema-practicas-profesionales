/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import sistemapracticasprofesionales.modelo.pojo.Rol;

/**
 *
 * @author sebas
 */
public class ConexionBD {
    
    private static final String RUTA_BD =
            "/sistemapracticasprofesionales/recursos/properties/bd.properties";

    private static final String RUTA_AUTENTICACION =
            "/sistemapracticasprofesionales/recursos/properties/autenticacion.properties";

    private static final String RUTA_ESTUDIANTE =
            "/sistemapracticasprofesionales/recursos/properties/estudiante.properties";

    private static final String RUTA_PROFESOR =
            "/sistemapracticasprofesionales/recursos/properties/profesor.properties";

    private static final String RUTA_COORDINADOR =
            "/sistemapracticasprofesionales/recursos/properties/coordinador.properties";

    private static final String RUTA_ADMINISTRADOR =
            "/sistemapracticasprofesionales/recursos/properties/administrador.properties";

    private static Properties propiedadesBD;

    private ConexionBD() {
    }

    public static Connection crearParaAutenticacion() throws SQLException {
        Properties credenciales = cargarProperties(RUTA_AUTENTICACION);

        String usuario = credenciales.getProperty("db.user");
        String password = credenciales.getProperty("db.password");

        cargarDriver();

        return DriverManager.getConnection(getUrl(), usuario, password);
    }

    public static Connection crearParaRol(Rol rol) throws SQLException {
        String rutaProperties = determinarRutaProperties(rol);

        Properties credenciales = cargarProperties(rutaProperties);

        String usuario = credenciales.getProperty("db.user");
        String password = credenciales.getProperty("db.password");

        cargarDriver();

        return DriverManager.getConnection(getUrl(), usuario, password);
    }

    private static Properties getPropiedadesBD() throws SQLException {
        if (propiedadesBD == null) {
            propiedadesBD = cargarProperties(RUTA_BD);
        }

        return propiedadesBD;
    }

    private static Properties cargarProperties(String ruta) throws SQLException {
        Properties properties = new Properties();

        try (InputStream input = ConexionBD.class.getResourceAsStream(ruta)) {

            if (input == null) {
                throw new SQLException("No se encontró el archivo de configuración: " + ruta);
            }

            properties.load(input);

        } catch (IOException ex) {
            throw new SQLException("Error al cargar el archivo de configuración: " + ruta, ex);
        }

        return properties;
    }

    private static void cargarDriver() throws SQLException {
        try {
            Class.forName(getDriver());
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el driver de MySQL: " + ex.getMessage(), ex);
        }
    }

    private static String determinarRutaProperties(Rol rolUsuario) throws SQLException {

        if (rolUsuario == null) {
            throw new SQLException("El rol del usuario no puede ser null");
        }

        switch (rolUsuario) {
            case Estudiante:
                return RUTA_ESTUDIANTE;

            case Profesor:
                return RUTA_PROFESOR;

            case Coordinador:
                return RUTA_COORDINADOR;

            case Administrador:
                return RUTA_ADMINISTRADOR;

            default:
                throw new SQLException("Rol no reconocido: " + rolUsuario);
        }
    }

    private static String getDriver() throws SQLException {
        return getPropiedadesBD().getProperty("db.driver");
    }

    private static String getHost() throws SQLException {
        return getPropiedadesBD().getProperty("db.host");
    }

    private static String getPort() throws SQLException {
        return getPropiedadesBD().getProperty("db.port");
    }

    private static String getDatabase() throws SQLException {
        return getPropiedadesBD().getProperty("db.name");
    }

    private static String getUrl() throws SQLException {
        return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase()
                + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }
    
}
