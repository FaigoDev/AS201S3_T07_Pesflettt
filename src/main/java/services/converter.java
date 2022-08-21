/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("convertCargo")
public class converter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String cargo = "";
        if(value != null){
            cargo = (String) value;
            switch(cargo){
                case "C": cargo ="Conductor"; break;
                case "A": cargo ="Administrador"; break;
            }
        }
        return cargo;
    }




    
}
