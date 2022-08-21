package services;

import bean.FacturadorC;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Facturador;
import modelo.PlantaMinera;
import modelo.Usuario;

public class ReniecS {

    public static boolean buscarDni(Usuario usu) throws Exception {
        String dni = usu.getDNIPER();
        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imp1bGlvLnF1aXNwZUB2YWxsZWdyYW5kZS5lZHUucGUifQ.6M-P2QMMvKFZEeMvTUXvkOooM02N_pWqt0OdlaYW3PM";
        String enlace = "https://dniruc.apisperu.com/api/v1/dni/" + dni + token;
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            if (root.isJsonObject()) {
                JsonObject rootobj = root.getAsJsonObject();
                String apellido_paterno = rootobj.get("apellidoPaterno").getAsString();
                String apellido_materno = rootobj.get("apellidoMaterno").getAsString();
                String nombres = rootobj.get("nombres").getAsString();

                System.out.println("Resultado\n");
                System.out.println(rootobj);

                usu.setNOMPER(nombres);
                usu.setAPEPER(apellido_paterno + " " + apellido_materno);
//                per.setNombre(nombres);
//                per.setApePaterno(apellido_paterno);
//                per.setApeMaterno(apellido_materno);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public static boolean buscarRucFacturador(Facturador fac) throws Exception {
        String ruc = fac.getRUCFAC();
        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imp1bGlvLnF1aXNwZUB2YWxsZWdyYW5kZS5lZHUucGUifQ.6M-P2QMMvKFZEeMvTUXvkOooM02N_pWqt0OdlaYW3PM";
        String enlace = "https://dniruc.apisperu.com/api/v1/ruc/" + ruc + token;
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            System.out.println("Este es json " + root);
            if (root.isJsonObject() && root != null) {
                JsonObject rootobj = root.getAsJsonObject();
                String razon = rootobj.get("razonSocial").getAsString();
                String direccion = rootobj.get("direccion").getAsString();
                String ubigeo = rootobj.get("ubigeo").getAsString();
                fac.setRAZSOCFAC(razon);
                fac.setDIRFAC(direccion);
                fac.setCODUBI(ubigeo);
            }
        } catch (Exception ex) {
            System.out.println("Error en ServicioRuc " + ex.getMessage());
            fac.setCODUBI(null);
            return false;
        }
        return true;
    }

    public static boolean buscarRucPlanta(PlantaMinera plan) throws Exception {
        String ruc = plan.getRUCPLAMIN();
        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imp1bGlvLnF1aXNwZUB2YWxsZWdyYW5kZS5lZHUucGUifQ.6M-P2QMMvKFZEeMvTUXvkOooM02N_pWqt0OdlaYW3PM";
        String enlace = "https://dniruc.apisperu.com/api/v1/ruc/" + ruc + token;
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            System.out.print("Este es json " + root);
            if (root.isJsonObject() && root != null) {
                JsonObject rootobj = root.getAsJsonObject();
                String razon = rootobj.get("razonSocial").getAsString();
                String direccion = rootobj.get("direccion").getAsString();
                String ubigeo = rootobj.get("ubigeo").getAsString();

                plan.setNOMPLAMIN(razon);
                plan.setDIRPLAMIN(direccion);
                plan.setCODUBI(ubigeo);
            }
        } catch (Exception ex) {
            System.out.println("Error en ServicioRuc " + ex.getMessage());
            plan.setCODUBI(null);
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Facturador fac = new Facturador();
        Usuario usu = new Usuario();
        usu.setDNIPER("76019224");
//        fac.setRUCFAC("20606000571");
        buscarDni(usu);
//        buscarRuc(fac);
    }
}
