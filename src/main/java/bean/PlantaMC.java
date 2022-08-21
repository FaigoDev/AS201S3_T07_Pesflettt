package bean;

import dao.PlantaMineraImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.PlantaMinera;
import org.primefaces.PrimeFaces;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.component.export.PDFOrientationType;
import org.primefaces.util.LangUtils;
import services.ReniecS;
import services.Reportes;
import services.convertidorS;

@Named(value = "plantac")
@SessionScoped

public class PlantaMC implements Serializable {

    private PlantaMinera plan = new PlantaMinera(); //La instacia que se hace de modelo Planta Minera
    private PlantaMineraImpl dao = new PlantaMineraImpl();  //La instancia que se hace de PlanaMineraImpl
    private List<PlantaMinera> listadoPlan;  //Listado que se crea para el modelo planta Minera
    private int tipo = 1;
    private Reportes reporte;
    private ExcelOptions excelOpt;
    private PDFOptions pdfOpt;
    private boolean valor;
    private boolean pase;
    private convertidorS con;

    public PlantaMC() {
        customizationOptions();
        reporte = new Reportes();
        con = new convertidorS();

    }

    public void registrar() throws Exception, Throwable {
        try {
            int caso = 0;
            caso = dao.validarplantamin(plan, caso);
            switch (caso) {
                case 0:
                    plan.setCODUBI(dao.obtenerCodigoUbigeo(plan.getCODUBI()));  //Lo que se obtien del modelo codubi se guarda en ese metodo y con ese metodo tre el codigo del ubigeo
                    plan.setNOMPLAMIN(con.convertirCadena(plan.getNOMPLAMIN()));
                    plan.setDIRPLAMIN(con.convertirCadena(plan.getDIRPLAMIN()));
                    dao.registrar(plan);  // LLama al metodo registrar y se lleva los datos de la planta minera
                    PrimeFaces.current().ajax().update("dlgPlantas:dlgdatos");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registro Exitoso"));
                    limpiar();
                    listar();
                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El RUC que intentaste registrar, ya existe."));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre que intentaste registrar, ya existe."));
                    break;
                case 3:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El Celular que intentaste registrar, ya existe."));
                    break;
                case 4:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El Correo que intentaste registrar, ya existe."));
                    break;
                case 5:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La direccion que intentaste registrar, ya existe."));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error en registrarC " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        try {
            plan.setCODUBI(dao.obtenerCodigoUbigeo(plan.getCODUBI()));
            plan.setNOMPLAMIN(con.convertirCadena(plan.getNOMPLAMIN()));
            plan.setDIRPLAMIN(con.convertirCadena(plan.getDIRPLAMIN()));
            System.out.println("Lo que eh llevado modificaar " + plan.getIDPLAMIN() + " / " + plan.getNOMPLAMIN() + plan.getRUCPLAMIN() + plan.getCORPLAMIN());
            dao.modificar(plan);  //Llama al metodo modificar y se lleva los datos de la planta minera
            PrimeFaces.current().ajax().update("dlgPlantaM:dlgdatosP");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
            limpiar(); //llama al metodo limpiar
            listar();  //llama al metodo Listar
        } catch (Exception e) {
            System.out.println("Error en modificarC " + e.getMessage());
        }
    }

    public void eliminar(PlantaMinera plans) throws Exception {
        try {
            dao.eliminar(plans);  //Llama al metodo eliminar y lo guarda en este metodo
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();
            System.out.println("Estoy en eliminar Controlador");
        } catch (Exception e) {
            System.out.println("Error en eliminarC " + e.getMessage());
        }
    }

    public void buscarRuc() {
        try {
            if (plan.getRUCPLAMIN() == null) {

            } else {
                ReniecS re = new ReniecS();
                pase = re.buscarRucPlanta(plan);
                if (pase == true) {
                    plan.setNOMPLAMIN(con.convertirCadena(plan.getNOMPLAMIN()));
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
            System.out.println("error en Busqueda RUC " + e.getMessage());
        }
    }

    public void remplazarDireccion() {
        try {
            System.err.println("Ubigeo traido: " + plan.getCODUBI());
            String distrito = plan.getCODUBI();
            distrito = dao.enviarUbigeo(distrito);
            System.err.println(plan.getDIRPLAMIN());
            if (plan.getDIRPLAMIN().contains(distrito)) {
                plan.setDIRPLAMIN(plan.getDIRPLAMIN().replaceAll(distrito, ""));
                System.err.println("a " + plan.getDIRPLAMIN());
            }
            plan.setDIRPLAMIN(con.convertirCadena(plan.getDIRPLAMIN()));
            System.err.println("Esta es ahora la dirección: " + plan.getDIRPLAMIN());
        } catch (Exception e) {
            System.out.println("Error en remplazar dirección" + e.getMessage());
        }
    }

    public void restaurar(PlantaMinera mins) throws Exception {
        try {
            dao.restaurar(mins);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "OK", "Restaurado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en RestaurarC" + e.getMessage());
        }
    }

    public void completarDistrito() {
        try {
            plan.setCODUBI(dao.TraerDistrito(plan.getCODUBI()));
            plan.setCODUBI(con.convertirCadena(plan.getCODUBI()));
        } catch (Exception e) {
            System.out.println("Error en completar Distrito " + e.getMessage());
        }
    }

    public List<String> completeTextUbigeo(String query) throws SQLException, Exception {
        PlantaMineraImpl daoUbi = new PlantaMineraImpl();  //Crea un objeto llamando a ViajeImpl
        return daoUbi.autocompleteUbigeo(query);  //returna al objeto con el metodo autocompete
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        PlantaMinera plan = (PlantaMinera) value;
        return plan.getRUCPLAMIN().toLowerCase().contains(filterText)
                || plan.getNOMPLAMIN().toLowerCase().contains(filterText)
                || plan.getCODUBI().toLowerCase().contains(filterText)
                || plan.getDIRPLAMIN().toString().toLowerCase().contains(filterText);
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

    public void limpiar() {
        plan = new PlantaMinera();
    }

    public void listar() {
        try {
            listadoPlan = dao.listar(tipo);  //Llama al metodo listarTodos 
        } catch (Exception e) {
            System.out.println("Error en listarC Plantas " + e.getMessage());
        }
    }

    public void reportePlanta() {

        Reportes reporte = new Reportes();
        try {
            String ruta = "/reports/PlantaM.jasper";
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
            reporte.exportarPDFGlobal(0, "PlantaM.jasper", "Listado_PlantasM-" + jodatime + ".pdf",null,null,0);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }
    

    public String estadoColor(PlantaMinera plan) {
        String color = "";
        if (plan.getESTPLAMIN().equals("A") || plan.getESTPLAMIN().equals(null)) {
            color = "5F5D5D";
        } else {
            color = "ff0000";
        }
        return color;
    }

    public PlantaMinera getPlan() {
        return plan;
    }

    public void setPlan(PlantaMinera plan) {
        this.plan = plan;
    }

    public PlantaMineraImpl getDao() {
        return dao;
    }

    public void setDao(PlantaMineraImpl dao) {
        this.dao = dao;
    }

    public List<PlantaMinera> getListadoPlan() {
        return listadoPlan;
    }

    public void setListadoPlan(List<PlantaMinera> listadoPlan) {
        this.listadoPlan = listadoPlan;
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

    public Reportes getReporte() {
        return reporte;
    }

    public void setReporte(Reportes reporte) {
        this.reporte = reporte;
    }

//    public Convertidor getCam() {
//        return cam;
//    }
//
//    public void setCam(Convertidor cam) {
//        this.cam = cam;
//    }
    public convertidorS getCon() {
        return con;
    }

    public void setCon(convertidorS con) {
        this.con = con;
    }

}
