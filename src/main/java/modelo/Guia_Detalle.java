package modelo;

public class Guia_Detalle {

    private int IDDETCON;
    private Guia gui = new Guia();
    private Viaje via = new Viaje();
    private Mineral min = new Mineral();
    private int CANSAC;
    private double TMHDETCON;

    

    public double getTMHDETCON() {
        return TMHDETCON;
    }

    public void setTMHDETCON(double TMHDETCON) {
        this.TMHDETCON = TMHDETCON;
    }

    public int getIDDETCON() {
        return IDDETCON;
    }

    public void setIDDETCON(int IDDETCON) {
        this.IDDETCON = IDDETCON;
    }

    public Guia getGui() {
        return gui;
    }

    public void setGui(Guia gui) {
        this.gui = gui;
    }

    public Viaje getVia() {
        return via;
    }

    public void setVia(Viaje via) {
        this.via = via;
    }

    public Mineral getMin() {
        return min;
    }

    public void setMin(Mineral min) {
        this.min = min;
    }

    public int getCANSAC() {
        return CANSAC;
    }

    public void setCANSAC(int CANSAC) {
        this.CANSAC = CANSAC;
    }

}