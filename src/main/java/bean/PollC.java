package bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named (value="pollC")
@ViewScoped
public class PollC implements Serializable{

    private String estiloDecremento;

    
    public PollC(){
        numero = 60;
    }
    private int numero = 60;

    public void activarSesion() {
        numero = 60;
    }

    public void decremento() throws IOException {
        numero = numero - 1;
        System.out.println("En decremento " + numero);
        if (numero == 0) {
            System.out.println("Fuera");
            FacesContext.getCurrentInstance().getExternalContext().redirect("Loguin.xhtml");
        }
    }

    public String estadoColor(int numero) {
        String color = "";
        if (numero > 15) {
            color = "5F5D5D";
        } else {
            color = "ff0000";
        }
        return color;
    }
    
    
    
    public String getEstiloDecremento() {
        return estiloDecremento;
    }

    public void setEstiloDecremento(String estiloDecremento) {
        this.estiloDecremento = estiloDecremento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
