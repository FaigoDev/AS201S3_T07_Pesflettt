package bean;

import dao.FacturadorImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import modelo.Facturador;
import org.primefaces.PrimeFaces;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;
import org.primefaces.util.LangUtils;
import services.ReniecS;
import services.Reportes;
import services.convertidorS;

@Named(value = "facturador")
@SessionScoped
@FacesValidator("vFactura")
public class FacturadorC implements Serializable, Validator {

    private Facturador fac;
    private FacturadorImpl dao;
    private List<Facturador> listadofac;
    private int tipo = 1;
    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;
    private boolean valor;
    private boolean pase;
    private convertidorS con;

    public FacturadorC() {
        con = new convertidorS();
        fac = new Facturador();
        dao = new FacturadorImpl();
        listadofac = new ArrayList<>();
        customizationOptions();
    }

    public void registrar() throws Exception, Throwable {
        try {
            int caso = 0;
            caso = dao.validarfacturador(fac, caso);
            switch (caso) {
                case 0:
                    fac.setRAZSOCFAC(con.convertirCadena(fac.getRAZSOCFAC()));
                    fac.setDIRFAC(con.convertirCadena(fac.getDIRFAC()));
                    fac.setCODUBI(dao.obtenerCodigoUbigeo(fac.getCODUBI()));
                    dao.registrar(fac);
                    PrimeFaces.current().ajax().update("dlgFacturador:dlgdatos");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registro Exitoso"));
                    limpiar();
                    listar();
                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El RUC que intentaste registrar, ya existe."));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La Razón Social que intentaste registrar, ya existe."));
                    break;
                case 3:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El Celular que intentaste registrar, ya existe."));
                    break;
                case 4:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El Correo que intentaste registrar, ya existe."));
                    break;
                case 5:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La Dirección que intentaste registrar, ya existe."));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error en registrarC " + e.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Porfavor", "Completa correctamente el formulario"));
        }
    }

    public void modificar() throws Exception {
        try {
            fac.setCODUBI(dao.obtenerCodigoUbigeo(fac.getCODUBI()));
            fac.setRAZSOCFAC(con.convertirCadena(fac.getRAZSOCFAC()));
            fac.setDIRFAC(con.convertirCadena(fac.getDIRFAC()));

            System.out.println("FacturadorM " + fac.getIDFAC() + "// " + fac.getRAZSOCFAC());
            dao.modificar(fac);
            limpiar();
            listar();
            PrimeFaces.current().ajax().update("dlgFacturadorModificar:dlgdatos");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificación Exitosa"));
//            if (dao.existe(fac, listadofac) == false) {
//                fac.setCODUBI(dao.obtenerCodigoUbigeo(fac.getCODUBI()));
//                dao.modificar(fac);
//                PrimeFaces.current().ajax().update("dlgFacturadorModificar:dlgdatos");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificación Exitosa"));
//                limpiar();
//                listar();
//            } else {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El Facturador que intentaste modificar, ya existe."));
//            }
        } catch (Exception e) {
            System.out.println("Error en modicarC" + e.getMessage());
        }
    }

    public void eliminar(Facturador facs) throws Exception {
        try {
            dao.eliminar(facs);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en EliminarC" + e.getMessage());
        }
    }

    public void restaurar(Facturador facs) throws Exception {
        try {
            dao.restaurar(facs);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "OK", "Restaurado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en RestaurarC" + e.getMessage());
        }
    }

    public List<String> completeTextUbigeo(String query) throws SQLException, Exception {
        FacturadorImpl daoUbi = new FacturadorImpl();
        return daoUbi.autocompleteUbigeo(query);
    }

    public void listar() {
        try {
            listadofac = dao.listar(tipo);
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void limpiar() {
        System.out.println("Ya se limpio");
        pase = false;
        fac = new Facturador();
    }

    public void reporteFactura() {
        Reportes reporte = new Reportes();
        try {
            String ruta = "/reports/Facturador.jasper";
            reporte.verPDF(ruta);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            System.out.println("Error en Reporte Facturador" + e.getMessage());
        }
    }

    public void descargarReporte() {
        try {
            Reportes reporte = new Reportes();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(0, "Facturador.jasper", "Listado_Facturadores-" + jodatime + ".pdf",null,null,0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

    public String estadoColor(Facturador fac) {
        String color = "";
        if (fac.getESTFAC().equals("A") || fac.getESTFAC().equals(null)) {
            color = "5F5D5D";
        } else {
            color = "ff0000";
        }
        return color;
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        try {

            if (LangUtils.isValueBlank(filterText)) {
                System.out.println("Es null");
                listar();
                PrimeFaces.current().ajax().update("form:tablaFac");
                return true;
            } else {
//                int filterInt = getInteger(filterText);
                Facturador fac = (Facturador) value;

                return fac.getRUCFAC().toLowerCase().contains(filterText)
                        || fac.getRAZSOCFAC().toLowerCase().contains(filterText)
                        || fac.getCELFAC().toLowerCase().contains(filterText)
                        || fac.getCODUBI().toString().toLowerCase().contains(filterText)
                        || fac.getDIRFAC().toLowerCase().contains(filterText);
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return false;

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
        excelOpt.setFacetBgColor("#19C7FF");
        excelOpt.setFacetFontSize("10");
        excelOpt.setFacetFontColor("#FFFFFF");
        excelOpt.setFacetFontStyle("BOLD");
        excelOpt.setCellFontColor("#000000");
        excelOpt.setCellFontSize("8");
        excelOpt.setFontName("Verdana");

        pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#19C7FF");
        pdfOpt.setFacetFontColor("#000000");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontSize("12");
        pdfOpt.setFontName("Courier");
        pdfOpt.setOrientation(PDFOrientationType.LANDSCAPE);
    }

    public void buscarRuc() {
        try {
            if (fac.getRUCFAC() == null) {

            } else {
                ReniecS re = new ReniecS();
                System.out.println("este es el fac " + fac.getRUCFAC());
                pase = re.buscarRucFacturador(fac);
                if (pase == true) {
                    fac.setRAZSOCFAC(con.convertirCadena(fac.getRAZSOCFAC()));

                    remplazarDireccion();
                    completarDistrito();
                    pase = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Busqueda", "RUC encontrado"));
                    PrimeFaces.current().ajax().update("mensaje");
                } else if (pase == false) {
                    limpiar();
                    pase = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "RUC no encontrado"));
                    PrimeFaces.current().ajax().update("mensaje");
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "RUC no encontrado"));
            System.out.println("error en Busqueda RUCc" + e.getMessage());
        }
    }

    public void remplazarDireccion() {
        try {
            String distrito = fac.getCODUBI();
            distrito = dao.enviarUbigeo(distrito);
            System.out.println(fac.getDIRFAC());
            if (fac.getDIRFAC().contains(distrito)) {
                fac.setDIRFAC(fac.getDIRFAC().replaceAll(distrito, ""));
                System.out.println("a " + fac.getDIRFAC());
            }
            fac.setDIRFAC(con.convertirCadena(fac.getDIRFAC()));
        } catch (Exception e) {
            System.out.println("Error en remplazar dirección" + e.getMessage());
        }
    }

    public void completarDistrito() {
        try {
            fac.setCODUBI(dao.TraerDistrito(fac.getCODUBI()));
            fac.setCODUBI(con.convertirCadena(fac.getCODUBI()));

        } catch (Exception e) {
            System.out.println("Error en completar Distrito " + e.getMessage());
        }
    }

    public Facturador getFac() {
        return fac;
    }

    public void setFac(Facturador fac) {
        this.fac = fac;
    }

    public FacturadorImpl getDao() {
        return dao;
    }

    public void setDao(FacturadorImpl dao) {
        this.dao = dao;
    }

    public List<Facturador> getListadofac() {
        return listadofac;
    }

    public void setListadofac(List<Facturador> listadofac) {
        this.listadofac = listadofac;
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String numero = o.toString().trim();
        if (numero.length() != 0 && numero.length() < 10) {
            String plantilla = "^9\\d\\d\\d\\d\\d\\d\\d\\d$";
            boolean val = Pattern.matches(plantilla, numero);
            if (!val) {
                throw new ValidatorException(new FacesMessage("Formato inadecuado 9########"));

            }
        }
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

    public boolean isValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean isPase() {
        return pase;
    }

    public void setPase(boolean pase) {
        this.pase = pase;
    }

    public convertidorS getCon() {
        return con;
    }

    public void setCon(convertidorS con) {
        this.con = con;
    }

}
