package services;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.primefaces.component.api.UICalendar;

//@FacesValidator("validar")
public class Validador implements Serializable, Validator {

    

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String factura = o.toString().trim();

        if (factura.length() == 0) {
            throw new ValidatorException(new FacesMessage("Ingrese el Nº de Factura"));
        } else {
            String plantilla = "^\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d$";
            boolean val = Pattern.matches(plantilla, factura);
            if (!val) {
                throw new ValidatorException(new FacesMessage("Ingrese un número de factura correcto"));

            }
        }

    }
    
    public static void main(String[] args) {
        Date fecha = new Date();
        DateTime dateTime = new DateTime(fecha);
        int mes = dateTime.getMonthOfYear();
        System.out.println("Mes: " + mes);
    }

}
