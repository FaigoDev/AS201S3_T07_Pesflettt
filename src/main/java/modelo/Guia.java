
package modelo;

import java.util.Date;
import java.util.Objects;

public class Guia {
    //Transaccional CONTROL_GUIA
    private int IDCONGUI;
    private String CODGUITRA;
    private String CODGUIREM;
    private Facturador fac = new Facturador();
    private Viaje viaje = new Viaje();
    private PlantaMinera plan = new PlantaMinera();
    private Date FECGUI;
    private String FECGUIVER;
    private double TOTTMHCON;
    private int TOTCANSAC;
    private String ESTCOT;
    private String ESTCONGUI;

    public int getIDCONGUI() {
        return IDCONGUI;
    }

    public void setIDCONGUI(int IDCONGUI) {
        this.IDCONGUI = IDCONGUI;
    }

    public String getCODGUITRA() {
        return CODGUITRA;
    }

    public void setCODGUITRA(String CODGUITRA) {
        this.CODGUITRA = CODGUITRA;
    }

    public String getCODGUIREM() {
        return CODGUIREM;
    }

    public void setCODGUIREM(String CODGUIREM) {
        this.CODGUIREM = CODGUIREM;
    }

    public Date getFECGUI() {
        return FECGUI;
    }

    public void setFECGUI(Date FECGUI) {
        this.FECGUI = FECGUI;
    }

    public String getESTCOT() {
        return ESTCOT;
    }

    public void setESTCOT(String ESTCOT) {
        this.ESTCOT = ESTCOT;
    }
    
    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public String getFECGUIVER() {
        return FECGUIVER;
    }

    public void setFECGUIVER(String FECGUIVER) {
        this.FECGUIVER = FECGUIVER;
    }

    public Facturador getFac() {
        return fac;
    }

    public void setFac(Facturador fac) {
        this.fac = fac;
    }

    public PlantaMinera getPlan() {
        return plan;
    }

    public void setPlan(PlantaMinera plan) {
        this.plan = plan;
    }

    public double getTOTTMHCON() {
        return TOTTMHCON;
    }

    public void setTOTTMHCON(double TOTTMHCON) {
        this.TOTTMHCON = TOTTMHCON;
    }

    public int getTOTCANSAC() {
        return TOTCANSAC;
    }

    public void setTOTCANSAC(int TOTCANSAC) {
        this.TOTCANSAC = TOTCANSAC;
    }

    @Override
    public String toString() {
        return "Guia{" + "IDCONGUI=" + IDCONGUI + ", CODGUITRA=" + CODGUITRA + ", CODGUIREM=" + CODGUIREM + ", fac=" + fac + ", viaje=" + viaje + ", plan=" + plan + ", FECGUI=" + FECGUI + ", FECGUIVER=" + FECGUIVER + ", TOTTMHCON=" + TOTTMHCON + ", TOTCANSAC=" + TOTCANSAC + ", ESTCOT=" + ESTCOT + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.CODGUITRA);
        hash = 79 * hash + Objects.hashCode(this.CODGUIREM);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Guia other = (Guia) obj;
        if (!Objects.equals(this.CODGUITRA, other.CODGUITRA) && !Objects.equals(this.CODGUIREM, other.CODGUIREM)) {
            return false;
        }
        return true;
    }

    public String getESTCONGUI() {
        return ESTCONGUI;
    }

    public void setESTCONGUI(String ESTCONGUI) {
        this.ESTCONGUI = ESTCONGUI;
    }

    
}