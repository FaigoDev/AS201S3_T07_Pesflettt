package bean;

import dao.MineralImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Mineral;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.primefaces.PrimeFaces;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;
import org.primefaces.util.LangUtils;
import services.Reportes;
import services.convertidorS;

@Named(value = "mineralC")
@SessionScoped

public class MineralC implements Serializable {

    private Mineral min = new Mineral();  //La instacia que se hace de modelo Planta Minera
    private MineralImpl dao = new MineralImpl();  //La instacia que se hace de MineralImpl
    private List<Mineral> listadomin;  //Listado que se crea para el modelo Mineral
    private int tipo = 1;

    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;
    private convertidorS con;

    public MineralC() {
        customizationOptions();
        con = new convertidorS();
    }

    public void registrar() throws Exception, Throwable {
        try {
            int caso = 0;
            caso = dao.validarmineral(min, caso);

            if (min.getSITMIN().equals("NADA")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Rellene la situación"));
            } else {
                switch (caso) {
                    case 0:
                        min.setNOMMIN(con.convertirCadena(min.getNOMMIN()));
                        min.setSITMIN(con.convertirCadena(min.getSITMIN()));
                        min.setTIPMIN(con.convertirCadena(min.getTIPMIN()));
                        dao.registrar(min);  // LLama al metodo registrar y se lleva los datos del Mineral
                        PrimeFaces.current().ajax().update("dlgMineral:dlgdatos");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registro Exitoso"));
                        limpiar();  //llama al metodo limpiar
                        listar();  //llama al metodo Listar
                        break;
                    case 1:
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El mineral que intentaste registrar, ya existe."));
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error en registrarC" + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Porfavor", "Completa correctamente el formulario"));
        }
    }

    public void modificar() throws Exception {
        try {
            if (min.getSITMIN().equals("NADA")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Rellene la situación"));
            } else {
                min.setNOMMIN(con.convertirCadena(min.getNOMMIN()));
                min.setSITMIN(con.convertirCadena(min.getSITMIN()));
                min.setTIPMIN(con.convertirCadena(min.getTIPMIN()));
                dao.modificar(min);  // LLama al metodo modificar y se lleva los datos del Mineral
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificación Exitosa"));
                PrimeFaces.current().ajax().update("dlgMineralMod:dlgdatos");
                limpiar();
                listar();
            }
        } catch (Exception e) {
            System.out.println("Error en modicarC" + e.getMessage());
        }
    }

    public void eliminar(Mineral mins) throws Exception {
        try {
            dao.eliminar(mins);  //Llama al metodo eliminar y lo guarda en este metodo
            limpiar();
            listar();
            PrimeFaces.current().ajax().update("dlgMineral:dlgdatos");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
        } catch (Exception e) {
            System.out.println("Error en EliminarC" + e.getMessage());
        }
    }

    public void listar() {
        try {
            listadomin = dao.listar(tipo);  //Llama al metodo
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
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

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        Mineral min = (Mineral) value;
        return min.getNOMMIN().toLowerCase().contains(filterText)
                || min.getTIPMIN().toLowerCase().contains(filterText)
                || min.getSITMIN().toLowerCase().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void limpiar() {
        min = new Mineral();
    }

//    public void reporteMineral() {
//        Reportes reporte = new Reportes();
//        try {
//            Date dateTime = new Date();
//            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
//            String jodatime = f.format(dateTime);
//            Map<String, Object> parameters = new HashMap();
//            System.out.println("Este sera la fecha " + jodatime);
//            reporte.exportarPDFGlobal(parameters, "Mineral.jasper", "Listado_Minerales-" + jodatime + ".pdf");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
//        } catch (Exception e) {
//            System.out.println("Error en Reporte Mineral" + e.getMessage());
//        }
//    }

    public void restaurar(Mineral min) throws Exception {
        try {
            dao.restaurar(min);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "OK", "Restaurado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en RestaurarC" + e.getMessage());
        }
    }

    public String estadoColor(Mineral min) {
        String color = "";
        if (min.getESTMIN().equals("A") || min.getESTMIN().equals(null)) {
            color = "#5F5D5D";

        } else {
            color = "#ff0000";
            System.out.println("estoy en else");
        }
        return color;
    }

    public Mineral getMin() {
        return min;
    }

    public void setMin(Mineral min) {
        this.min = min;
    }

    public MineralImpl getDao() {
        return dao;
    }

    public void setDao(MineralImpl dao) {
        this.dao = dao;
    }

    public List<Mineral> getListadomin() {
        return listadomin;
    }

    public void setListadomin(List<Mineral> listadomin) {
        this.listadomin = listadomin;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
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

    public convertidorS getCon() {
        return con;
    }

    public void setCon(convertidorS con) {
        this.con = con;
    }

}
