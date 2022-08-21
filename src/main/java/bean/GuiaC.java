package bean;

import dao.GuiaImpl;
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
import modelo.Guia;
import modelo.Guia_Detalle;
import modelo.Viaje;
import org.joda.time.DateTime;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;
import org.primefaces.util.LangUtils;
import services.ApiCorreo;
import services.JodaTime;
import services.Reportes;

@Named(value = "guiaC")
@SessionScoped
public class GuiaC implements Serializable {

    private Guia_Detalle guiD;
    private Guia gui;
    private GuiaImpl dao;
    private Viaje via;
    private List<Guia> listadogui;
    private int tipo = 1;
    private List<Viaje> listadovia;
    private List<Guia_Detalle> listaGuiaDetalle;
    private List<Guia> listaGuiaDetalleFinal;
    private String asunto;
    private String cuerpo;
    private String correo;
    private ApiCorreo servi;
    private JodaTime time;
    private Date calendar;
    private int mes;
    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;
    private String nombrefile;

    public GuiaC() {
        listaGuiaDetalle = new ArrayList();
        gui = new Guia();
        guiD = new Guia_Detalle();
        dao = new GuiaImpl();
        listadogui = new ArrayList<>();
        via = new Viaje();
        servi = new ApiCorreo();
        time = new JodaTime();
        customizationOptions();
    }

    public void registrar() throws Exception, Throwable {
        try {
            int caso = 0;
            caso = dao.validarguia(gui, caso);
            switch (caso) {
                case 0:
                    gui.getFac().setIDFAC(dao.obtenerCodigoFacturador(gui.getFac().getRAZSOCFAC()));
                    gui.getPlan().setIDPLAMIN(dao.obtenerCodigoPlanta(gui.getPlan().getNOMPLAMIN()));
                    gui.getViaje().setIDVIA(via.getIDVIA());
//            guiD.getMin().setIDMIN(dao.obtenerCodigoMineral(guiD.getMin().getNOMMIN()));
                    dao.registrar(gui);
                    int idgui = dao.obtenerUltimoId();
                    dao.registroMultiple(listaGuiaDetalle, idgui);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registro Exitoso"));
                    listaGuiaDetalle = null;
                    limpiar();
                    listar();
                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La guia remitente que intentaste registrar, ya existe."));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La guia transportista que intentaste registrar, ya existe."));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error en registrarC " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Porfavor", "Completa correctamente el formulario"));
        }
    }
    

    public void modificar() throws Exception {
        try {
            gui.getFac().setIDFAC(dao.obtenerCodigoFacturador(gui.getFac().getRAZSOCFAC()));
            System.out.println(gui.getFac().getIDFAC());
            gui.getPlan().setIDPLAMIN(dao.obtenerCodigoPlanta(gui.getPlan().getNOMPLAMIN()));
            dao.modificar(gui);
//            System.out.println("Lo que llevo " + fac.getRUCFAC() + fac.getRAZSOCFAC() + fac.getCELFAC() + fac.getDIRFAC() + fac.getCODUBI());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificación Exitosa"));
            limpiar();
            listar();
            System.out.println("Estoy en Controlador Modificar");
        } catch (Exception e) {
            System.out.println("Error en modicarC" + e.getMessage());
        }
    }

    public void eliminar(Guia guis) throws Exception {
        try {
            dao.eliminar(guis);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en EliminarC" + e.getMessage());
        }
    }

    public void eliminarfila(Guia_Detalle guia) {
        try {
            listaGuiaDetalle.remove(guia);
            sumar();
        } catch (Exception e) {
            System.out.println("Error en eliminar fila" + e.getMessage());
        }

    }

    public void listar() {
        try {
            listadogui = dao.listar(tipo);
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void listarDetalle() {
        try {
            listaGuiaDetalle = dao.listarDetalle(gui.getIDCONGUI());
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void listarvia() {
        try {
            int tipo = 1;
            ViajeImpl daovia = new ViajeImpl();
            listadovia = daovia.listar(tipo);
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void traerViaje() {
        try {
            System.out.println("Este es id viaje :" + gui.getViaje().getIDVIA());
            dao.traerDatos(gui);
            via.setIDCON(gui.getViaje().getIDCON());
            via.setIDVEH(gui.getViaje().getIDVEH());
            via.setCODUBI(gui.getViaje().getCODUBI());
            via.setDIRVIA(gui.getViaje().getDIRVIA());
            System.out.println("Este es datos viaje " + gui.getViaje().getIDCON() + gui.getViaje().getIDVEH());
        } catch (Exception e) {
            System.out.println("Error en traerViaje" + e.getMessage());
        }
    }

  

    public void reporteGuiaMes() {

        Reportes reporte = new Reportes();
        try {
            String ruta = "/reports/Guia_P.jasper";
            reporte.verPdfParametros(null, null, mes, ruta, 5);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println("Error en Reporte VerGuiaMes" + e.getMessage());
        }
    }

    public void descargarReporteMes() {
        try {
            int caso = 5;
            Reportes reporte = new Reportes();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            System.out.println("caso: " + caso + "mes: " + mes);
            reporte.exportarPDFGlobal(caso, "Guia_P.jasper", "Listado_GuiasXMes-" + jodatime + ".pdf", null, null, mes);
        } catch (Exception e) {
            System.out.println("Error en ReportePC " + e.getMessage());
        }
    }

    public void reporteConPlanta() {
        try {
            Reportes reporte = new Reportes();
            String planta = gui.getPlan().getNOMPLAMIN();
            String ruta = "/reports/Planta_Viaje.jasper";
            reporte.verPdfParametros(planta, null, 0, ruta, 2);
            FacesContext.getCurrentInstance().responseComplete();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en ReporteC " + e.getMessage());
        }
    }

    public void descargarReportePlanta() {
        try {
            Reportes reporte = new Reportes();
            String planta = gui.getPlan().getNOMPLAMIN();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(2, "Planta_Viaje.jasper", "Listado_ViajesPorPlanta-" + jodatime + ".pdf", planta, null, 0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en ReporteC " + e.getMessage());
        }
    }

    public void reporteGuia() {
        Reportes reporte = new Reportes();
        try {
            String ruta = "/reports/Guia.jasper";
            reporte.verPDF(ruta);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            System.out.println("Error en generar reporte " + e.getMessage());
        }
    }

    public void descargarReporte() {
        try {
            Reportes reporte = new Reportes();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(0, "Guia.jasper", "Listado_De_Guias-" + jodatime + ".pdf", null, null, 0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

    public void limpiar() {
        System.out.println("Ya se limpio afdsgf");
        gui = new Guia();
        guiD = new Guia_Detalle();
        correo = null;
        asunto = null;
        cuerpo = null;
    }

    public void nuevo() {
        listaGuiaDetalle.clear();
    }

    public void restaurar(Guia guis) throws Exception {
        try {
            dao.restaurar(guis);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "OK", "Restaurado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en RestaurarC" + e.getMessage());
        }
    }

    public void agregarFila() {
        try {

            Guia_Detalle g = new Guia_Detalle();
            if (guiD.getMin().getNOMMIN() == null && guiD.getCANSAC() == 0 && guiD.getTMHDETCON() == 0.0
                    || guiD.getTMHDETCON() == 0.0 || guiD.getMin().getNOMMIN() == null && guiD.getTMHDETCON() == 0.0) {
                System.out.println("Entre a vacio detalle");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Inserte información en el Detalle"));
            } else {
                g.getMin().setNOMMIN(guiD.getMin().getNOMMIN());
                g.setCANSAC(guiD.getCANSAC());
                g.setTMHDETCON(guiD.getTMHDETCON());
                System.out.println(g.getCANSAC() + "  " + g.getTMHDETCON() + g.getMin().getNOMMIN());
                listaGuiaDetalle.add(g);
                guiD = new Guia_Detalle();
                sumar();
                System.out.println("Total Sacos=" + gui.getTOTCANSAC() + "/ tOTALToneladas=" + gui.getTOTTMHCON());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "HECHO", "Detalle Añadido"));
            }

        } catch (Exception e) {
            System.out.println("Error en agregar fila " + e.getMessage());
        }
    }

    public void sumar() {
        System.out.println("Estoy en el metodo sumar po");
        int sacos = 0;
        double toneladas = 0;
        try {
            for (Guia_Detalle guiD : listaGuiaDetalle) {
//                if (guiD.getCANSAC() == 0) {
//
//                    System.out.println("SOy Granel");
//                }
                sacos = sacos + guiD.getCANSAC();
                toneladas = toneladas + guiD.getTMHDETCON();
                gui.setTOTCANSAC(sacos);
                gui.setTOTTMHCON(toneladas);
                System.out.println("Helloword tantas veeces" + "Total Sacos=" + gui.getTOTCANSAC() + "/ tOTALToneladas=" + gui.getTOTTMHCON());
            }
        } catch (Exception e) {
            System.out.println("Error en sumar " + e.getMessage());
        }
    }

    public void obteneridviaje() {
        gui.getViaje().setIDVIA(via.getIDVIA());
    }

    public List<String> completeTextFacturador(String query) throws SQLException, Exception {
        GuiaImpl daoFac = new GuiaImpl();
        return daoFac.autocompleteFacturador(query);
    }

    public List<String> completeTextPlanta(String query) throws SQLException, Exception {
        GuiaImpl daoPlan = new GuiaImpl();
        return daoPlan.autocompletePlanta(query);
    }

    public List<String> completeTextMineral(String query) throws SQLException, Exception {
        GuiaImpl daoMin = new GuiaImpl();
        return daoMin.autocompleteMineral(query);
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

    public String estadoColor(Guia gui) {
        String color = "";
        if (gui.getESTCONGUI().equals("A") || gui.getESTCONGUI().equals(null)) {
            color = "5F5D5D";
        } else {
            color = "ff0000";
        }
        return color;
    }

    public Guia getGui() {
        return gui;
    }

    public void setGui(Guia gui) {
        this.gui = gui;
    }

    public GuiaImpl getDao() {
        return dao;
    }

    public void setDao(GuiaImpl dao) {
        this.dao = dao;
    }

    public List<Guia> getListadogui() {
        return listadogui;
    }

    public void setListadogui(List<Guia> listadogui) {
        this.listadogui = listadogui;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Viaje getVia() {
        return via;
    }

    public void setVia(Viaje via) {
        this.via = via;
    }

    public List<Viaje> getListadovia() {
        return listadovia;
    }

    public void setListadovia(List<Viaje> listadovia) {
        this.listadovia = listadovia;
    }

    public List<Guia> getListaGuiaDetalleFinal() {
        return listaGuiaDetalleFinal;
    }

    public void setListaGuiaDetalleFinal(List<Guia> listaGuiaDetalleFinal) {
        this.listaGuiaDetalleFinal = listaGuiaDetalleFinal;
    }

    public List<Guia_Detalle> getListaGuiaDetalle() {
        return listaGuiaDetalle;
    }

    public void setListaGuiaDetalle(List<Guia_Detalle> listaGuiaDetalle) {
        this.listaGuiaDetalle = listaGuiaDetalle;
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

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public ApiCorreo getServi() {
        return servi;
    }

    public void setServi(ApiCorreo servi) {
        this.servi = servi;
    }

    public JodaTime getTime() {
        return time;
    }

    public void setTime(JodaTime time) {
        this.time = time;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Guia_Detalle getGuiD() {
        return guiD;
    }

    public void setGuiD(Guia_Detalle guiD) {
        this.guiD = guiD;
    }

    public String getNombrefile() {
        return nombrefile;
    }

    public void setNombrefile(String nombrefile) {
        this.nombrefile = nombrefile;
    }

}
