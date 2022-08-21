package modelo;

import java.util.Objects;


public class PlantaMinera {
    private String IDPLAMIN;
    private String RUCPLAMIN;
    private String NOMPLAMIN;
    private String CELPLAMIN;
    private String CORPLAMIN;
    private String DIRPLAMIN;
    private String CODUBI;
    private String ESTPLAMIN;

    public String getIDPLAMIN() {
        return IDPLAMIN;
    }

    public void setIDPLAMIN(String IDPLAMIN) {
        this.IDPLAMIN = IDPLAMIN;
    }

    public String getNOMPLAMIN() {
        return NOMPLAMIN;
    }

    public void setNOMPLAMIN(String NOMPLAMIN) {
        this.NOMPLAMIN = NOMPLAMIN;
    }

    public String getDIRPLAMIN() {
        return DIRPLAMIN;
    }

    public void setDIRPLAMIN(String DIRPLAMIN) {
        this.DIRPLAMIN = DIRPLAMIN;
    }

    public String getCODUBI() {
        return CODUBI;
    }

    public void setCODUBI(String CODUBI) {
        this.CODUBI = CODUBI;
    }

    public String getESTPLAMIN() {
        return ESTPLAMIN;
    }

    public void setESTPLAMIN(String ESTAPLAMIN) {
        this.ESTPLAMIN = ESTAPLAMIN;
    }

    public String getRUCPLAMIN() {
        return RUCPLAMIN;
    }

    public void setRUCPLAMIN(String RUCPLAMIN) {
        this.RUCPLAMIN = RUCPLAMIN;
    }

    @Override
    public String toString() {
        return "PlantaMinera{" + "IDPLAMIN=" + IDPLAMIN + ", RUCPLAMIN=" + RUCPLAMIN + ", NOMPLAMIN=" + NOMPLAMIN + ", DIRPLAMIN=" + DIRPLAMIN + ", CODUBI=" + CODUBI + ", ESTPLAMIN=" + ESTPLAMIN + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.RUCPLAMIN);
        hash = 97 * hash + Objects.hashCode(this.NOMPLAMIN);
        hash = 97 * hash + Objects.hashCode(this.DIRPLAMIN);
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
        final PlantaMinera other = (PlantaMinera) obj;
        if (!Objects.equals(this.RUCPLAMIN, other.RUCPLAMIN) && !Objects.equals(this.NOMPLAMIN, other.NOMPLAMIN) && !Objects.equals(this.DIRPLAMIN, other.DIRPLAMIN)  ) {
            return false;
        }
        return true;
    }

    public String getCORPLAMIN() {
        return CORPLAMIN;
    }

    public void setCORPLAMIN(String CORPLAMIN) {
        this.CORPLAMIN = CORPLAMIN;
    }

    public String getCELPLAMIN() {
        return CELPLAMIN;
    }

    public void setCELPLAMIN(String CELPLAMIN) {
        this.CELPLAMIN = CELPLAMIN;
    }
    
    
}
