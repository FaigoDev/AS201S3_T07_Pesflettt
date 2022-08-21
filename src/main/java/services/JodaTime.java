package services;

import dao.Conexion;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import static javax.faces.context.FacesContext.getCurrentInstance;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperRunManager;

public class JodaTime extends Conexion {

    public void getReportePdf(String ruta, int mes,String nomPDF) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            File reportfile = new File(ruta);

            Map<String, Object> parameter = new HashMap<String, Object>();

            parameter.put("MES", new Integer(mes));
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputstream = httpServletResponse.getOutputStream();
            outputstream.write(bytes, 0, bytes.length);
            
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void getReportePdf2(String ruta,String Conductor, int mes,String nomPDF) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            File reportfile = new File(ruta);
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("NOMAPECON", Conductor);
            parameter.put("MES", mes);
            System.out.println("Pase esto gero ");
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputstream = httpServletResponse.getOutputStream();
            outputstream.write(bytes, 0, bytes.length);
            
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void getReportePdf3(String ruta,String planta,String nomPDF) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            File reportfile = new File(ruta);
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("PLANTA", planta);
            System.out.println("Pase esto gero ");
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputstream = httpServletResponse.getOutputStream();
            outputstream.write(bytes, 0, bytes.length);
            
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void getReporteVehiculo(String ruta,String vehiculo,String nomPDF) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            File reportfile = new File(ruta);
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("VEHICULO", vehiculo);
            System.out.println("Pase esto gero ");
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.conectar());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomPDF + "");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputstream = httpServletResponse.getOutputStream();
            outputstream.write(bytes, 0, bytes.length);
            
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
