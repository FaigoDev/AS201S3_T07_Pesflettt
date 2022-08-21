package services;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import modelo.Usuario;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;

public class UsuarioS {

    public static void main(String[] args) throws UnknownHostException {
        //        Usuario usu = new Usuario();
        //        usu.setNOMUSU("fa");
        //        UsuariosAzar(usu);
        //        generarCodigo();
        //        traerDatosSesion();

        
    }

    public static void generarContrasena(Usuario usu) {
        convertidorS con = new convertidorS();
        String palabra = usu.getNOMUSU(); //jose
        Random rand = new Random();
        String contraseña = "";
        String signos = "#$%/()";
        try {
            for (int i = 1; i < 3; i++) {
                int posicion = rand.nextInt(palabra.length());
                String letra = palabra.substring(posicion);
                contraseña = contraseña + letra + posicion;
                if (contraseña.length() < 8) {
                    i = 1;
                }
            }
            int tamañoContra = contraseña.length();
            StringBuilder contra = new StringBuilder(contraseña);
            System.out.println("tamaño " + tamañoContra + contra.length());
            while (contra.length() > 15) {
                tamañoContra = tamañoContra - 1;
                contra.deleteCharAt(tamañoContra);

            }
            System.out.println("Contra cons: " + contra);
            contraseña = con.convertirCadena(contra.toString());
            System.out.println("Contra cons2: " + contraseña + "    posi   ");
            contraseña = contraseña + signos.charAt((int) (Math.random() * 4));
            System.out.println("aleatorio sim " + contraseña);
            //Generar aleatoriamente de una: RandomStringUtils.randomAlphanumeric(12)
            usu.setCONUSU(contraseña);
        } catch (Exception e) {
            System.out.println("Error en generarContraseñaS: " + e.getMessage());
        }

    }

    public static String generarCodigo() {
        Random rand = new Random();
        String codigo = "";
        for (int i = 0; i < 8; i++) {
            codigo = codigo + String.valueOf(rand.nextInt(9));
        }

        System.out.println("codigo: " + codigo);
        return codigo;

    }

    public static void traerDatosSesion() throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        System.out.println(InetAddress.getLocalHost().getHostName());
//        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
//        System.out.println("dd/MMM/yyyy HH:mm:ss-> "+dtf2.format(LocalDateTime.now()));
        Date dateTime = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        String fechaHora = f.format(dateTime);
        System.out.println("fecha y hora " + fechaHora + "Segundos ");
    }

    public static String generarEncriptacion(String cadena) {
        String algoritmo = "MD5";
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance(algoritmo);
            byte[] array = md.digest(cadena.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }

            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
