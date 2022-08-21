package modelo;

import java.util.Objects;

public class Facturador {

    private String IDFAC;
    private String RUCFAC;
    private String RAZSOCFAC;
    private String CELFAC;
    private String CORRFAC;
    private String CODUBI;
    private String DIRFAC;
    private String RUCM;
    private String ESTFAC;
    private int CANFAC;

    public Facturador() {
    }

    public Facturador(String IDFAC,String RUCFAC, String RAZSOCFAC, String CELFAC, String CORRFAC, String CODUBI, String DIRFAC, String ESTFAC) {
        this.IDFAC = IDFAC;
        this.RUCFAC = RUCFAC;
        this.RAZSOCFAC = RAZSOCFAC;
        this.CELFAC = CELFAC;
        this.CORRFAC = CORRFAC;
        this.CODUBI = CODUBI;
        this.DIRFAC = DIRFAC;
        this.ESTFAC = ESTFAC;
    }

    public String getRUCFAC() {
        return RUCFAC;
    }

    public void setRUCFAC(String RUCFAC) {
        this.RUCFAC = RUCFAC;
    }

    public String getRAZSOCFAC() {
        return RAZSOCFAC;
    }

    public void setRAZSOCFAC(String RAZSOCFAC) {
        this.RAZSOCFAC = RAZSOCFAC;
    }

    public String getCELFAC() {
        return CELFAC;
    }

    public void setCELFAC(String CELFAC) {
        this.CELFAC = CELFAC;
    }

    public String getCODUBI() {
        return CODUBI;
    }

    public void setCODUBI(String CODUBI) {
        this.CODUBI = CODUBI;
    }

    public String getESTFAC() {
        return ESTFAC;
    }

    public void setESTFAC(String ESTFAC) {
        this.ESTFAC = ESTFAC;
    }

    public String getDIRFAC() {
        return DIRFAC;
    }

    public void setDIRFAC(String DIRFAC) {
        this.DIRFAC = DIRFAC;
    }

    public String getCORRFAC() {
        return CORRFAC;
    }

    public void setCORRFAC(String CORRFAC) {
        this.CORRFAC = CORRFAC;
    }
    
    public String getIDFAC() {
        return IDFAC;
    }

    public void setIDFAC(String IDFAC) {
        this.IDFAC = IDFAC;
    }

    @Override
    public String toString() {
        return "Facturador{" + "IDFAC=" + ", RUCFAC=" + RUCFAC + ", RAZSOCFAC=" + RAZSOCFAC + ", CELFAC=" + CELFAC + ", CODUBI=" + CODUBI + ", ESTFAC=" + ESTFAC + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.RUCFAC);
        hash = 29 * hash + Objects.hashCode(this.RAZSOCFAC);
        hash = 29 * hash + Objects.hashCode(this.CELFAC);
        hash = 29 * hash + Objects.hashCode(this.DIRFAC);
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
        final Facturador other = (Facturador) obj;
        if (!Objects.equals(this.RUCFAC, other.RUCFAC) && !Objects.equals(this.RAZSOCFAC, other.RAZSOCFAC) && !Objects.equals(this.CELFAC, other.CELFAC) && !Objects.equals(this.DIRFAC, other.DIRFAC)) {
            return false;
        }
        return true;
    }

    public String getRUCM() {
        return RUCM;
    }

    public void setRUCM(String RUCM) {
        this.RUCM = RUCM;
    }

    public int getCANFAC() {
        return CANFAC;
    }

    public void setCANFAC(int CANFAC) {
        this.CANFAC = CANFAC;
    }

   

    }
