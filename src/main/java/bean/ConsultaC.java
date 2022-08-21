package bean;

import dao.ConsultasList;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.Facturador;
import modelo.Guia;
import modelo.Guia_Detalle;
import modelo.Usuario;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;
import services.Reportes;

@Named(value = "Consultas")
@SessionScoped

public class ConsultaC implements Serializable {

    private int tipo = 1;
    private int filt = 1;
    private List<Guia> listadocon;
    private List<Guia_Detalle> listadodet;
    private List<Guia> seleccion;
    private Guia gui;
    private ConsultasList dao;
    private double total = 0.0;
    private Reportes reports;

    private BarChartModel barModel;
    private List<Facturador> listaFacturadores;
    
    private PieChartModel pieModel;
    List<Usuario> listaConductores;

    public ConsultaC() throws Exception {
        gui = new Guia();
        dao = new ConsultasList();
        listadocon = new ArrayList<>();
        listadodet = new ArrayList<>();
        seleccion = new ArrayList<>();
        reports = new Reportes();
        listaFacturadores = new ArrayList();
        listaConductores = new ArrayList();
    }


    public void listar() throws Exception {
        try {
            listadocon = dao.listar(); //Llama al metodo listarTodos 
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void listardet(int codigo) throws Exception {
        try {
            listadodet = dao.listarDetGui(codigo); //Llama al metodo listarTodos 
            for (Guia_Detalle det : listadodet) {
                total += det.getCANSAC();
            }
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) throws Exception {
        try {
            System.out.println("Yoy escogiendo unp");
            Guia gui = (Guia) event.getObject();
            listadodet = dao.listarDetGui(gui.getIDCONGUI());
        } catch (Exception e) {
            System.out.println("Error en OnRow: " + e.getMessage());
        }

    }

    public void mostraDetalle() {
        try {
            int id = gui.getIDCONGUI();
            System.out.println("Este es id C " + id);
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
//            String ruta = servletContext.getRealPath("/reports/Cabecera.jasper");
//            FacesContext.getCurrentInstance().responseComplete();
//            reports.generarDetalle(id,ruta, "Guia_.pdf");
//            FacesContext.getCurrentInstance().responseComplete();
//            
            String ruta = "/reports/Cabecera.jasper";
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            reports.verPdfParametros(null, null, id, ruta, 3);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println("Error en mostrarDetalleC " + e);
        }
    }

    public void mostrarGraficos() throws Exception, Throwable{
        System.out.println("Estoy en mostrar graficos");
        
    }
    
    public void descargarReporteDetalle() {
        try {
            int id = gui.getIDCONGUI();
            String facturador = gui.getFac().getRAZSOCFAC();
            Reportes reporte = new Reportes();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
            String jodatime = f.format(dateTime);
            reporte.exportarPDFGlobal(3, "Cabecera.jasper", "GuiaNro" + id + "_" + facturador + "-" + jodatime + ".pdf", null, null, id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Reporte hecho"));
        } catch (Exception e) {
            System.out.println("Error en descargar Reporte");
        }
    }

   
    
 

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getFilt() {
        return filt;
    }

    public void setFilt(int filt) {
        this.filt = filt;
    }

    public ConsultasList getDao() {
        return dao;
    }

    public void setDao(ConsultasList dao) {
        this.dao = dao;
    }

    public Guia getGui() {
        return gui;
    }

    public void setGui(Guia gui) {
        this.gui = gui;
    }

    public List<Guia> getListadocon() {
        return listadocon;
    }

    public void setListadocon(List<Guia> listadocon) {
        this.listadocon = listadocon;
    }

    public List<Guia_Detalle> getListadodet() {
        return listadodet;
    }

    public void setListadodet(List<Guia_Detalle> listadodet) {
        this.listadodet = listadodet;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Guia> getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(List<Guia> seleccion) {
        this.seleccion = seleccion;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }
    

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

}
