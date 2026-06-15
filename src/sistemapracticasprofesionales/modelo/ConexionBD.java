package sistemapracticasprofesionales.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import sistemapracticasprofesionales.modelo.pojo.Rol;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 14/06/2026
 * Descripción: Clase para manejar las conexiones con la base de datos de acuerdo
 * a los roles del usuario y manejar los archivos de properties.
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

    /**
     * Método para acceder a la base de datos con las credenciales del usuario
     * de autenticación. Esto es asi porque es necesario proteger los registros
     * de la base de datos, se hace uso de un usuario que unicamente tiene permisos
     * sobre las tablas necesarias para validar la sesión.
     * 
     * @return conexion con la base de datos para el usuario de autenticacion.
     * @throws SQLException si ocurre un error al cargar las credenciales, 
     *                          cargar el driver o al establecer la conexion.
     */
    public static Connection crearParaAutenticacion() throws SQLException {
        Properties credenciales = cargarProperties(RUTA_AUTENTICACION);

        String usuario = credenciales.getProperty("db.user");
        String password = credenciales.getProperty("db.password");

        cargarDriver();

        return DriverManager.getConnection(getUrl(), usuario, password);
    }

    /**
     * Método para acceder a la base de datos con las credenciales de alguno
     * de los usuarios creados para la conexion. Esto es asi porque es necesario 
     * proteger los registros de la base de datos, y restringir ciertas operaciones
     * de otros usuarios, para esto se hace uso distintos usuarios con distintos
     * permisos sobre las tablas que le correspondan.
     * 
     * @param rol rol del usuario actual que se obtiene del atributo de la
     *              clase Sesión. Será utilizado para saber que properties leer.
     * @return conexion a la base de datos para el usuario que corresponda.
     * @throws SQLException si ocurre un error al cargar las credenciales, 
     *                          cargar el driver o al establecer la conexion.
     */
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
