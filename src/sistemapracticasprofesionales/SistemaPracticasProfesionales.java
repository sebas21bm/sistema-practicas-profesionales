package sistemapracticasprofesionales;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 *
 * @author sebas
 */
public class SistemaPracticasProfesionales extends Application {
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException{
        FXMLLoader cargador = Utilidades.cargarFXML("FXMLInicioSesion");
        Parent raiz = cargador.load();
        Scene escena = new Scene(raiz);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.setTitle("Inicio de sesión");
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
