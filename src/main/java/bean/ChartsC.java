package bean;

import dao.ConsultasList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import modelo.Facturador;
import modelo.Usuario;
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

@Named(value = "barController")
@SessionScoped
public class ChartsC implements Serializable {

    private ConsultasList dao;
    private BarChartModel barModel;
    private BarChartModel barModel2;
    private List<Facturador> listaFacturadores;
    private PieChartModel pieModel;
    private PieChartModel pieModel2;
    private List<Usuario> listaConductores;
    private static int inicioChartBar = 0;
    private static int inicioChartPie = 0;
    private static int inicioChartBar2 = 0;
    private static int inicioChartPie2 = 0;

    public ChartsC() throws Exception {
        dao = new ConsultasList();
        listaFacturadores = new ArrayList();
        listaConductores = new ArrayList();
    }

    public void empezarVista() throws Exception {
        try {
                createBarModel();
                createPieModel();
                createBarModel2();
                createPieModel2();
        } catch (Exception e) {
            System.out.println("Error en empezar vista: " + e.getMessage());
        }

    }

    public void createBarModel() throws Exception {
        try {
//        if(inicioChartBar == 0){
            listaFacturadores = dao.datosChartBar();
            dao = new ConsultasList();
            barModel = new BarChartModel();
            ChartData data = new ChartData();

            BarChartDataSet barDataSet = new BarChartDataSet();
            barDataSet.setLabel("Facturadores");
            barDataSet.setBackgroundColor("black");

            //Cargando datos
            List<String> labels = new ArrayList<>();
            List<Number> valores = new ArrayList<>();

            for (Facturador fac : listaFacturadores) {
                labels.add(fac.getRAZSOCFAC());
                valores.add(fac.getCANFAC());
            }

            data.setLabels(labels);
            barDataSet.setData(valores);

            //Definiendo colores para las barras
            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            barDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            barDataSet.setBorderColor(borderColor);
            barDataSet.setBorderWidth(1);

            data.addChartDataSet(barDataSet);

            barModel.setData(data);

            //Opciones
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            ticks.setBeginAtZero(true);
            linearAxes.setTicks(ticks);
            cScales.addYAxesData(linearAxes);
            options.setScales(cScales);
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Facturadores más Frecuentes");
            title.setFontColor("black");
            title.setFontStyle("bold");
            title.setFontSize(25);
            options.setTitle(title);

            Legend legend = new Legend();
            legend.setDisplay(true);
            legend.setPosition("top");
            LegendLabel legendLabels = new LegendLabel();
            legendLabels.setFontStyle("bold");
            legendLabels.setFontColor("#909090");
            legendLabels.setFontSize(12);
            legend.setLabels(legendLabels);
            options.setLegend(legend);

            barModel.setOptions(options);
//        }
            inicioChartBar = 1;
        } catch (Exception e) {
            System.out.println("Error en chartC " + e.getMessage());
        }
    }

    public void createPieModel() throws Exception {
        try {
//        if(inicioChartPie == 0){
//             
            listaConductores = dao.datosChartPie();
            dao = new ConsultasList();
            pieModel = new PieChartModel();
            ChartData data = new ChartData();

            //Cargando datos
            PieChartDataSet dataSet = new PieChartDataSet();
            List<Number> valores = new ArrayList<>();
            for (Usuario usu : listaConductores) {
                valores.add(usu.getCANVIA());
            }
            dataSet.setData(valores);

            //Asignando colores
            List<String> bgColors = new ArrayList<>();
            bgColors.add("rgb(255, 99, 132)");
            bgColors.add("rgb(54, 162, 235)");
            bgColors.add("rgb(255, 205, 86)");
            bgColors.add("rgb(255, 34, 86)");
            bgColors.add("rgb(255, 205, 125)");
            bgColors.add("rgb(255, 205, 235)");
            dataSet.setBackgroundColor(bgColors);

            data.addChartDataSet(dataSet);

            //Asignando detalle (label)
            List<String> tipo = new ArrayList<>();
            for (Usuario usu : listaConductores) {
                tipo.add(usu.getNOMBRE());
            }
            data.setLabels(tipo);

            //Opciones
            PieChartOptions options = new PieChartOptions();
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Conductores con más viajes");
            title.setFontColor("black");
            title.setFontStyle("bold");
            title.setFontSize(25);
            options.setTitle(title);
            pieModel.setOptions(options);

            pieModel.setData(data);
//        }
            inicioChartPie = 1;
        } catch (Exception e) {
            System.out.println("Error en traer ChartPie" + e.getMessage());
        }
    }

    public void createBarModel2() throws Exception {
        try {
            listaConductores = dao.datosChartBar2();
            dao = new ConsultasList();
            barModel2 = new BarChartModel();
            ChartData data = new ChartData();

            BarChartDataSet barDataSet2 = new BarChartDataSet();
            barDataSet2.setLabel("Vehículos");
            barDataSet2.setBackgroundColor("rgba(0, 0, 0, 0)");
            barDataSet2.setBorderColor("rgb(255, 255, 0)");
            barDataSet2.setBorderWidth(1);

            //Cargando datos
            List<String> labels2 = new ArrayList<>();
            List<Number> valores2 = new ArrayList<>();

            for (Usuario usu : listaConductores) {
                labels2.add(usu.getVEHICULO());
                valores2.add(usu.getCANUSU());
            }

            data.setLabels(labels2);
            barDataSet2.setData(valores2);
            //Definiendo colores para las barras
            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            barDataSet2.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            barDataSet2.setBorderColor(borderColor);
            barDataSet2.setBorderWidth(1);

            data.addChartDataSet(barDataSet2);

            barModel2.setData(data);

            //Opciones
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            ticks.setBeginAtZero(true);
            linearAxes.setTicks(ticks);
            cScales.addYAxesData(linearAxes);
            options.setScales(cScales);
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Vehículos que mas has usado");
            title.setFontColor("black");
            title.setFontStyle("bold");
            title.setFontSize(25);
            options.setTitle(title);

            Legend legend = new Legend();
            legend.setDisplay(true);
            legend.setPosition("top");
            LegendLabel legendLabels = new LegendLabel();
            legendLabels.setFontStyle("bold");
            legendLabels.setFontColor("#909090");
            legendLabels.setFontSize(12);
            legend.setLabels(legendLabels);
            options.setLegend(legend);

            barModel2.setOptions(options);
//        }
            inicioChartBar = 1;
        } catch (Exception e) {
            System.out.println("Error en chartC 2" + e.getMessage());
        }
    }

    public void createPieModel2() throws Exception {
        try {
            listaConductores = dao.datosChartPie2();
            dao = new ConsultasList();
            pieModel2 = new PieChartModel();
            ChartData data = new ChartData();

            //Cargando datos
            PieChartDataSet dataSet = new PieChartDataSet();
            List<Number> valores = new ArrayList<>();
            for (Usuario usu : listaConductores) {
                valores.add(usu.getCANVIS());
            }
            dataSet.setData(valores);

            //Asignando colores
            List<String> bgColors = new ArrayList<>();
            bgColors.add("rgb(255, 99, 132)");
            bgColors.add("rgb(54, 162, 235)");
            bgColors.add("rgb(255, 205, 86)");
            bgColors.add("rgb(255, 34, 86)");
            bgColors.add("rgb(255, 205, 125)");
            bgColors.add("rgb(255, 205, 235)");
            dataSet.setBackgroundColor(bgColors);

            data.addChartDataSet(dataSet);

            //Asignando detalle (label)
            List<String> tipo = new ArrayList<>();
            for (Usuario usu : listaConductores) {
                tipo.add(usu.getPLANTA());
            }
            data.setLabels(tipo);

            //Opciones
            PieChartOptions options = new PieChartOptions();
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Plantas Mineras que has visitado");
            title.setFontColor("black");
            title.setFontStyle("bold");
            title.setFontSize(25);
            options.setTitle(title);
            pieModel2.setOptions(options);

            pieModel2.setData(data);
//        }
            inicioChartPie = 1;
        } catch (Exception e) {
            System.out.println("Error en traer ChartPie 2" + e.getMessage());
        }
    }

    public static void retraso() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            System.out.println("Error en DelayC: " + e.getMessage());
        }
    }

    public List<Usuario> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Usuario> listaConductores) {
        this.listaConductores = listaConductores;
    }

    public ConsultasList getDao() {
        return dao;
    }

    public void setDao(ConsultasList dao) {
        this.dao = dao;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public List<Facturador> getListaFacturadores() {
        return listaFacturadores;
    }

    public void setListaFacturadores(List<Facturador> listaFacturadores) {
        this.listaFacturadores = listaFacturadores;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public static int getInicioChartBar() {
        return inicioChartBar;
    }

    public static void setInicioChartBar(int aInicioChartBar) {
        inicioChartBar = aInicioChartBar;
    }

    public static int getInicioChartPie() {
        return inicioChartPie;
    }

    public static void setInicioChartPie(int aInicioChartPie) {
        inicioChartPie = aInicioChartPie;
    }

    public BarChartModel getBarModel2() {
        return barModel2;
    }

    public void setBarModel2(BarChartModel barModel2) {
        this.barModel2 = barModel2;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    public static int getInicioChartBar2() {
        return inicioChartBar2;
    }

    public static void setInicioChartBar2(int aInicioChartBar2) {
        inicioChartBar2 = aInicioChartBar2;
    }

    public static int getInicioChartPie2() {
        return inicioChartPie2;
    }

    public static void setInicioChartPie2(int aInicioChartPie2) {
        inicioChartPie2 = aInicioChartPie2;
    }

}
