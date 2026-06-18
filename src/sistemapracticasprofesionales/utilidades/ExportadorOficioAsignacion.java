package sistemapracticasprofesionales.utilidades;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import sistemapracticasprofesionales.modelo.pojo.Asignacion;

/**
 * Autor: Sebastián Barrera Mora
 * Fecha de creación: 18/06/2026
 * Descripción: Clase encargada de llenar la plantilla PDF del oficio de
 * asignación con la información de una asignación.
 */
public class ExportadorOficioAsignacion {
    
    private static final String RUTA_PLANTILLA =
            "/sistemapracticasprofesionales/recursos/formatos/"
            + "pdf_oficio_asignacion.pdf";
    
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    
    public static void generarOficio(
            Asignacion asignacion, File archivoDestino)
            throws IOException {
        
        InputStream plantilla = ExportadorOficioAsignacion.class
                .getResourceAsStream(RUTA_PLANTILLA);
        
        if (plantilla == null) {
            throw new FileNotFoundException(
                    "No se encontró la plantilla del oficio.");
        }
        
        PdfReader lector = new PdfReader(plantilla);
        PdfWriter escritor = new PdfWriter(archivoDestino);
        PdfDocument documentoPdf = new PdfDocument(lector, escritor);
        PdfAcroForm formulario =
                PdfAcroForm.getAcroForm(documentoPdf, false);
        
        if (formulario == null) {
            documentoPdf.close();
            throw new IOException(
                    "La plantilla no contiene campos de formulario.");
        }
        
        formulario.setGenerateAppearance(true);
        llenarFormulario(formulario, asignacion);
        formulario.flattenFields();
        documentoPdf.close();
    }
    
    private static void llenarFormulario(
            PdfAcroForm formulario, Asignacion asignacion)
            throws IOException {
        
        llenarCampo(formulario, "fecha_emision",
                formatearFecha(new Date()));
        llenarCampo(formulario, "nombre_estudiante",
                asignacion.getNombreEstudiante());
        llenarCampo(formulario, "matricula",
                asignacion.getMatricula());
        llenarCampo(formulario, "correo_estudiante",
                asignacion.getCorreoEstudiante());
        llenarCampo(formulario, "nombre_proyecto",
                asignacion.getNombreProyecto());
        llenarCampo(formulario, "descripcion_proyecto",
                asignacion.getDescripcionProyecto());
        llenarCampo(formulario, "organizacion_vinculada",
                asignacion.getOrganizacionVinculada());
        llenarCampo(formulario, "fecha_finalizacion",
                formatearFecha(asignacion.getFechaFinalizacion()));
        llenarCampo(formulario, "nombre_responsable",
                asignacion.getNombreResponsable());
        llenarCampo(formulario, "correo_responsable",
                asignacion.getCorreoResponsable());
        llenarCampo(formulario, "nombre_coordinador",
                asignacion.getNombreCoordinador());
    }
    
    private static void llenarCampo(
            PdfAcroForm formulario, String nombreCampo, String valor)
            throws IOException {
        
        Map<String, PdfFormField> campos = formulario.getFormFields();
        PdfFormField campo = campos.get(nombreCampo);
        
        if (campo == null) {
            throw new IOException(
                    "No se encontró el campo: " + nombreCampo);
        }
        
        campo.setValue(obtenerTextoSeguro(valor));
    }
    
    private static String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "";
        }
        
        return new SimpleDateFormat(FORMATO_FECHA).format(fecha);
    }
    
    private static String obtenerTextoSeguro(String texto) {
        return texto == null ? "" : texto.trim();
    }
}