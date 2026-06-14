/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package sistemapracticasprofesionales;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sistemapracticasprofesionales.utilidades.Utilidades;

/**
 *
 * @author sebas
 */
public class SistemaPracticasProfesionales extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader cargador = Utilidades.cargarFXML("FXMLInicioSesion");
        Parent raiz = cargador.load();
        
        Scene escena = new Scene(raiz);
        primaryStage.setScene(escena);
        primaryStage.setTitle("Inicio de sesión");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
