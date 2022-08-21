package services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowagie.text.Element;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import static oracle.net.aso.b.i;
import org.apache.http.util.TextUtils;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;
import org.codehaus.jettison.json.JSONException;

public class ApiPaises {

    public static void main(String[] args) throws Exception {
        String nombre = "Peru";
        buscarPaises(nombre);
    }

    public static void getFormattedAddressFromArray(JSONArray array) {
        List strings = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            strings.add(array.get(i));
        }
    }

    public static void buscarPaises(String nombre) throws Exception {
//        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imp1bGlvLnF1aXNwZUB2YWxsZWdyYW5kZS5lZHUucGUifQ.6M-P2QMMvKFZEeMvTUXvkOooM02N_pWqt0OdlaYW3PM";
        String enlace = "https://restcountries.eu/rest/v2/region/europe";
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            if (root.isJsonArray()) {

                //Este es si te traer un objeto
//                JSONPObject data = new JSONPObject("tu api");
                //este un array
//                JsonArray members = new JSONArray(root);
//                List<String> powersFrontend = new ArrayList<>();
//
//                for (JSONPObject member : root) {
//                    JsonArray powers = member.getArray("powers")
//                    for (JSONPObject power : powers) {
//                        String powerString = powers.getString();
//                        powersFrontend.add(powerString);
//                    }
//                }
            }
        } catch (Exception ex) {
            System.out.println(" Error en APiPais: " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "DNI no encontrado"));
        }
    }

}
