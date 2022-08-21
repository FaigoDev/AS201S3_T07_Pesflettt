package modelo;

import java.util.Date;
import java.util.Objects;

public class Viaje {

    private int IDVIA;
    private String IDCON;
    private String IDVEH;
    private String CODUBI;
    private String DIRVIA;
    private double GASVIA;
    private Date FECVIA;
    private String FECVER;
    private String ESTVIA;

    public Viaje() {
    }

    public Viaje(int IDVIA, String IDCON, String IDVEH, String CODUBI, String DIRVIA, double GASVIA, Date FECVIA, String FECVER, String ESTVIA) {
        this.IDVIA = IDVIA;
        this.IDCON = IDCON;
        this.IDVEH = IDVEH;
        this.CODUBI = CODUBI;
        this.DIRVIA = DIRVIA;
        this.GASVIA = GASVIA;
        this.FECVIA = FECVIA;
        this.FECVER = FECVER;
        this.ESTVIA = ESTVIA;
    }

    public int getIDVIA() {
        return IDVIA;
    }

    public void setIDVIA(int IDVIA) {
        this.IDVIA = IDVIA;
    }

    public String getCODUBI() {
        return CODUBI;
    }

    public void setCODUBI(String CODUBI) {
        this.CODUBI = CODUBI;
    }

    public String getDIRVIA() {
        return DIRVIA;
    }

    public void setDIRVIA(String DIRVIA) {
        this.DIRVIA = DIRVIA;
    }

    public double getGASVIA() {
        return GASVIA;
    }

    public void setGASVIA(double GASVIA) {
        this.GASVIA = GASVIA;
    }

    public String getESTVIA() {
        return ESTVIA;
    }

    public void setESTVIA(String ESTVIA) {
        this.ESTVIA = ESTVIA;
    }

    public String getIDCON() {
        return IDCON;
    }

    public void setIDCON(String IDCON) {
        this.IDCON = IDCON;
    }

    public String getIDVEH() {
        return IDVEH;
    }

    public void setIDVEH(String IDVEH) {
        this.IDVEH = IDVEH;
    }

    public Date getFECVIA() {
        return FECVIA;
    }

    public void setFECVIA(Date FECVIA) {
        this.FECVIA = FECVIA;
    }

    public String getFECVER() {
        return FECVER;
    }

    public void setFECVER(String FECVER) {
        this.FECVER = FECVER;
    }

    @Override
    public String toString() {
        return "Viaje{" + "IDVIA=" + IDVIA + ", IDCON=" + IDCON + ", IDVEH=" + IDVEH + ", CODUBI=" + CODUBI + ", DIRVIA=" + DIRVIA + ", GASVIA=" + GASVIA + ", FECVIA=" + FECVIA + ", FECVER=" + FECVER + ", ESTVIA=" + ESTVIA + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.IDCON);
        hash = 71 * hash + Objects.hashCode(this.IDVEH);
        hash = 71 * hash + Objects.hashCode(this.CODUBI);
        hash = 71 * hash + Objects.hashCode(this.DIRVIA);
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.GASVIA) ^ (Double.doubleToLongBits(this.GASVIA) >>> 32));
        hash = 71 * hash + Objects.hashCode(this.FECVER);
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
        final Viaje other = (Viaje) obj;
        if (!Objects.equals(this.IDCON, other.IDCON) && !Objects.equals(this.IDVEH, other.IDVEH) && !Objects.equals(this.CODUBI, other.CODUBI) && !Objects.equals(this.DIRVIA, other.DIRVIA) && !Objects.equals(this.GASVIA, other.GASVIA) && !Objects.equals(this.FECVER, other.FECVER)) {
            return false;
        }
        return true;
    }

}
