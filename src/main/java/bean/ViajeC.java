package bean;

import dao.ViajeImpl;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import modelo.Facturador;
import modelo.Guia;
import modelo.Viaje;
import org.joda.time.DateTime;
import org.primefaces.PrimeFaces;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;
import org.primefaces.util.LangUtils;
import services.ApiSms;
import services.JodaTime;
import services.Reportes;
import services.convertidorS;

@Named(value = "viajeC")
@SessionScoped
public class ViajeC implements Serializable {

    private Viaje via;  //La instacia que se hace de modelo Viaje
    private ViajeImpl dao;  //La instacia que se hace de ViajeImpl
    private List<Viaje> listadovia;  //Lista creada para el modelo Viaje
    private int tipo = 1;
    private Guia gui;
    private JodaTime report;
    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;
    private int mes;
    private convertidorS con;
    private Facturador fac;
    private JodaTime time;
    private ApiSms sms;

    public ViajeC() {  //Metodo constructor, es el primero en ejecutarse
        via = new Viaje();
        dao = new ViajeImpl();
        via = new Viaje();
        listadovia = new ArrayList<>();
        fac = new Facturador();
        report = new JodaTime();
        customizationOptions();
        con = new convertidorS();
        time = new JodaTime();
        sms = new ApiSms();
    }

    public void registrar() throws Throwable {
        try {

            via.setIDCON(con.convertirCadena(via.getIDCON()));
            via.setDIRVIA(con.convertirCadena(via.getDIRVIA()));
            String mensaje = "Nuevo viaje: " + "Ubicacion: " + via.getCODUBI() + " Dirección: " + via.getDIRVIA();
            via.setIDCON(dao.obtenerCodigoConductor(via.getIDCON()));  //lo que obtine del modelo idcon lo guarda en ese metodo y con ese metodo trae el id del conductor
            via.setCODUBI(dao.obtenerCodigoUbigeo(via.getCODUBI()));  //lo que obtine del modelo codubi lo guarda en ese metodo y con ese metodo trae el codigo del ubigeo
            via.setIDVEH(dao.obtenerCodigoVehiculo(via.getIDVEH()));  //lo que obtine del modelo idveh lo guarda en ese metodo y con ese metodo trae el id del vehiculo

            int caso = 0;
            caso = dao.validarviaje(via, caso);
            switch (caso) {
                case 0:
                    dao.registrar(via);  // LLama al metodo registrar y se lleva los datos del viaje

                    String celular = dao.obtenerCelular(via.getIDCON());
                    celular = "991692530";
                    System.out.println("Este es mensaje: " + mensaje + "Y este celular: " + celular);
                    sms.enviarSms(celular, mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registro Exitoso"));
                    PrimeFaces.current().ajax().update("dlgViaje:dlgdatos");
                    limpiar();
                    listar();
                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El viaje ingresado ya existe"));
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en RegistrarC" + e.getMessage());
        }
    }

    public void modificar() throws Throwable {
        try {
            via.setIDCON(con.convertirCadena(via.getIDCON()));
            via.setIDCON(dao.obtenerCodigoConductor(via.getIDCON()));
            via.setCODUBI(dao.obtenerCodigoUbigeo(via.getCODUBI()));
            via.setIDVEH(dao.obtenerCodigoVehiculo(via.getIDVEH()));
            via.setDIRVIA(con.convertirCadena(via.getDIRVIA()));
            int caso = 0;
//            caso = dao.validarviaje(via, caso);

            switch (caso) {
                case 0:
                    dao.modificar(via);  // LLama al metodo registrar y se lleva los datos del viaje
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificación Exitosa"));
                    PrimeFaces.current().ajax().update("dlgViajeModificar:dlgdatos");
                    limpiar();
                    listar();
                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ya existe el viaje modificado"));
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en ModificarC " + e.getMessage());
        }

    }

    public void eliminar(Viaje vias) {
        try {
            System.out.println("Aqui esta mi id via po" + vias.getIDVIA());
            dao.eliminar(vias);  // Llama al metodo eliminar
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminación Exitosa"));
        } catch (Exception e) {
            System.out.println("Error en EliminarC" + e.getMessage());
        }
    }

    public void listar() {
        try {
            listadovia = dao.listar(tipo);  //Llama al metodo listarTodos 
        } catch (Exception e) {
            System.out.println("Error en Listar ViajesC" + e.getMessage());
        }

    }

    public List<String> completeTextUbigeo(String query) throws SQLException, Exception {
        ViajeImpl daoUbi = new ViajeImpl();  //Crea un objeto llamando a ViajeImpl
        return daoUbi.autocompleteUbigeo(query);
    }

    public List<String> completeTextConductor(String query) throws SQLException, Exception {
        ViajeImpl daoCon = new ViajeImpl();
        return daoCon.autocompleteConductor(query);
    }

    public List<String> completeTextVehiculo(String query) throws SQLException, Exception {
        ViajeImpl daoVeh = new ViajeImpl();
        return daoVeh.autocompleteVehiculo(query);
    }

    public void llevarID(int numero) {
        gui.getViaje().setIDVIA(numero);
    }

    public void reporteMesYConductor() {
        try {
            Reportes reporte = new Reportes();
            String ruta = "/reports/Cond_Viaje.jasper";
            String conductor = via.getIDCON();
            reporte.verPdfParametros(conductor, null, mes, ruta, 1);
            FacesContext.getCurrentInstance().responseComplete();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en Reporte2 " + e.getMessage());
        }

    }

    public void descargarReporteMesYConductor() {
        try {
            String conductor = via.getIDCON(); //nombre
            Reportes reporte = new Reportes();
            Date dateTime = new Date();  // instancia un objeto
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(1, "Cond_Viaje.jasper", "Listado_ViajesPorConductorYMes-" + jodatime + ".pdf", conductor, null, mes); //caso, archivo, concatena nombre, fecha y extencion, conductor, mes 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

    public void reporteViaje() {

        Reportes reporte = new Reportes();
        try {
            String ruta = "/reports/Viaje.jasper";
            reporte.verPDF(ruta);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println("Error en Reporte Viaje" + e.getMessage());
        }
    }

    public void descargarReporte() {
        try {
            Reportes reporte = new Reportes();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(0, "Viaje.jasper", "Listado_De_Viajes-" + jodatime + ".pdf", null, null, 0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

    public void restaurar(Viaje vias) throws Exception {
        try {
            dao.restaurar(vias);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "OK", "Restaurado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en RestaurarC" + e.getMessage());
        }
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        Viaje via = (Viaje) value;
        return via.getIDCON().toLowerCase().contains(filterText)
                || via.getIDVEH().toLowerCase().contains(filterText)
                || via.getCODUBI().toLowerCase().contains(filterText)
                || via.getDIRVIA().toString().toLowerCase().contains(filterText)
                || via.getFECVIA().toString().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void customizationOptions() {
        excelOpt = new ExcelOptions();
        excelOpt.setFacetBgColor("#F88017");
        excelOpt.setFacetFontSize("10");
        excelOpt.setFacetFontColor("#0000ff");
        excelOpt.setFacetFontStyle("BOLD");
        excelOpt.setCellFontColor("#00ff00");
        excelOpt.setCellFontSize("8");
        excelOpt.setFontName("Verdana");

        pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#F88017");
        pdfOpt.setFacetFontColor("#0000ff");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontSize("12");
        pdfOpt.setFontName("Courier");
        pdfOpt.setOrientation(PDFOrientationType.LANDSCAPE);
    }

    public String estadoColor(Viaje via) {
        String color = "";
        if (via.getESTVIA().equals("A") || via.getESTVIA().equals(null)) {
            color = "5F5D5D";
        } else {
            color = "ff0000";
        }
        return color;
    }

    public void reporteConVehiculo() {
        try {
            Reportes reporte = new Reportes();
            String vehiculo = via.getIDVEH();
            String ruta = "/reports/Vehiculo.jasper";
            reporte.verPdfParametros(vehiculo, null, 0, ruta, 4);
            FacesContext.getCurrentInstance().responseComplete();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en ReporteC " + e.getMessage());
        }
        
        }
            

    public void descargarReporteVehiculo() {
        try {
            Reportes reporte = new Reportes();
            String vehiculo = via.getIDVEH();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(4, "Vehiculo.jasper", "Listado_De_Viajes-" + jodatime + ".pdf", vehiculo, null, 0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

public void limpiar() {
        via = new Viaje();
    }

    public Viaje getVia() {
        return via;
    }

    public void setVia(Viaje via) {
        this.via = via;
    }

    public ViajeImpl getDao() {
        return dao;
    }

    public void setDao(ViajeImpl dao) {
        this.dao = dao;
    }

    public List<Viaje> getListadovia() {
        return listadovia;
    }

    public void setListadovia(List<Viaje> listadovia) {
        this.listadovia = listadovia;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Guia getGui() {
        return gui;
    }

    public void setGui(Guia gui) {
        this.gui = gui;
    }

    public ExcelOptions getExcelOpt() {
        return excelOpt;
    }

    public void setExcelOpt(ExcelOptions excelOpt) {
        this.excelOpt = excelOpt;
    }

    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }

    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }

    public JodaTime getReport() {
        return report;
    }

    public void setReport(JodaTime report) {
        this.report = report;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public convertidorS getCon() {
        return con;
    }

    public void setCon(convertidorS con) {
        this.con = con;
    }

    public Facturador getFac() {
        return fac;
    }

    public void setFac(Facturador fac) {
        this.fac = fac;
    }

}
