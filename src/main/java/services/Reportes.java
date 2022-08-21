package services;

import dao.Conexion;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import static javax.faces.context.FacesContext.getCurrentInstance;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import org.joda.time.DateTime;

public class Reportes extends Conexion {

    public static void main(String[] args) {
        Date fecha = new Date();
        DateTime dateTime = new DateTime(fecha);
        int mes = dateTime.getMonthOfYear();
        System.out.println("Mes: " + mes);
    }

    public void verPDF(String ruta) throws Exception {
        try {
            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(ruta));
            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, conectar());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            System.out.println("Error en ver pdf" + e);
        }
    }

    public void verPdfParametros(String parametro1, String parametro2, int parametro3, String ruta, int caso) throws Exception {
        try {
            Map<String, Object> parameter = new HashMap<>();
            switch (caso) {
                
                
                
                case 1: 
                    parameter.put("NOMAPECON", parametro1); //Viaje por conductor
                    parameter.put("MES", parametro3);
                    break;
                case 2:
                    parameter.put("PLANTA", parametro1); //Viaje por planta
                    break;
                case 3:
                    parameter.put("ID", parametro3); //Traer detalleGuia
                    break;
                 case 4:
                    parameter.put("VEHICULO", parametro1); //Viaje por vehiculos
                    break;
                case 5:
                    parameter.put("MES", parametro3); //Guia por mes
                    break;

            }

            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(ruta));
            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parameter, conectar());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            System.out.println("Error en ver pdf" + e);
        }

//			
//		FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarPDFGlobal(int caso, String url, String nomPDF,String parametro1, String parametro2, int parametro3) throws JRException, IOException, Exception {
//        conectar();
        try {
            
            Map<String, Object> parameter = new HashMap<>(); // mapea los parametros
            switch (caso) {
                case 0:
                    //SIn parámetros
                
                case 1: 
                    parameter.put("NOMAPECON", parametro1); //Viaje por conductor
                    parameter.put("MES", parametro3); // solo se usa estos parametros
                    break;
                case 2:
                    parameter.put("PLANTA", parametro1); //Viaje por planta
                    break;
                case 3:
                    parameter.put("ID", parametro3); //Traer detalleGuia
                    break;
                 case 4:
                    parameter.put("VEHICULO", parametro1); //Viaje por vehiculos
                    break;
                    
                 case 5: 
                    parameter.put("MES", parametro3); //Guia por mes
                    break;
                

            }
            File jasper = new File(getCurrentInstance().getExternalContext().getRealPath("./reports/" + url + ""));
            JasperPrint jasperPrint = fillReport(jasper.getPath(), parameter, conectar());
            HttpServletResponse response = (HttpServletResponse) getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            
            
            
            try (ServletOutputStream stream = response.getOutputStream()) {
                exportReportToPdfStream(jasperPrint, stream);
                stream.flush();
                stream.close();
            }
            getCurrentInstance().responseComplete();
        } catch (IOException | JRException e) {
            out.println("Error en generar Reporte Servicio: " + e);
        }
    }

    public void generarDetalle(int idGuia, String ruta, String nomPDF) throws JRException, ClassNotFoundException, IOException, Exception {
        try {
            System.out.println("idGuia: " + idGuia);
//            File jasper = new File(getCurrentInstance().getExternalContext().getRealPath("./reports/Cabecera.jasper"));
//            HashMap parameters = new HashMap();
//            parameters.put("ID", idGuia);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parameters, conectar());
//            HttpServletResponse response = (HttpServletResponse) getCurrentInstance().getExternalContext().getResponse();
//            response.addHeader("Content-disposition", "attachment; filename=detalleGuia_(" + "" + ").pdf");
//            ServletOutputStream stream = response.getOutputStream();
//            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
//            stream.flush();
//            stream.close();
//            getCurrentInstance().responseComplete();
//            System.out.println("Estoy aca");
////            return jasperPrint;

            File reportfile = new File(ruta);
//            File reportfile2 = new File(ruta2);
            Map<String, Object> parameter = new HashMap<>();

            parameter.put("ID", idGuia);
            System.out.println("Pase esto gero ");
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputstream = httpServletResponse.getOutputStream();
            outputstream.write(bytes, 0, bytes.length);
//            
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
        }
//        return jasperPrint;
    }
}
