/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapracticasprofesionales.utilidades;

import javafx.fxml.FXMLLoader;
import sistemapracticasprofesionales.SistemaPracticasProfesionales;

/**
 *
 * @author sebas
 */
public class Utilidades {
    
    public static FXMLLoader cargarFXML(String nombreVista) {
        return new FXMLLoader(SistemaPracticasProfesionales.class.
                getResource("vista/" + nombreVista + ".fxml"));
    }
    
}
