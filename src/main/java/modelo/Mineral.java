package modelo;

import java.util.Objects;

public class Mineral {
    private String IDMIN;
    private String NOMMIN;
    private String TIPMIN;
    private String SITMIN;
    private String ESTMIN;
    
    public Mineral() {
    }

    public Mineral(String IDMIN, String NOMMIN, String TIPMIN, String SITMIN, String ESTMIN) {
        this.IDMIN = IDMIN;
        this.NOMMIN = NOMMIN;
        this.TIPMIN = TIPMIN;
        this.SITMIN = SITMIN;
        this.ESTMIN = ESTMIN;
    }

    public String getIDMIN() {
        return IDMIN;
    }

    public void setIDMIN(String IDMIN) {
        this.IDMIN = IDMIN;
    }

    public String getNOMMIN() {
        return NOMMIN;
    }

    public void setNOMMIN(String NOMMIN) {
        this.NOMMIN = NOMMIN;
    }

    public String getTIPMIN() {
        return TIPMIN;
    }

    public void setTIPMIN(String TIPMIN) {
        this.TIPMIN = TIPMIN;
    }

    public String getESTMIN() {
        return ESTMIN;
    }

    public void setESTMIN(String ESTMIN) {
        this.ESTMIN = ESTMIN;
    }

    public String getSITMIN() {
        return SITMIN;
    }

    public void setSITMIN(String SITMIN) {
        this.SITMIN = SITMIN;
    }

    @Override
    public String toString() {
        return "Mineral{" + "IDMIN=" + IDMIN + ", NOMMIN=" + NOMMIN + ", TIPMIN=" + TIPMIN + ", SITMIN=" + SITMIN + ", ESTMIN=" + ESTMIN + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.NOMMIN);
        hash = 67 * hash + Objects.hashCode(this.TIPMIN);
        hash = 67 * hash + Objects.hashCode(this.SITMIN);
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
        final Mineral other = (Mineral) obj;
        if (!Objects.equals(this.NOMMIN, other.NOMMIN) && !Objects.equals(this.TIPMIN, other.TIPMIN) && !Objects.equals(this.SITMIN, other.SITMIN)) {
            return false;
        }
        return true;
    }
}