
package services;


public class convertidorS {

    public static String convertirCadena(String cadena) {
        try {
            cadena = cadena.toLowerCase();
        char[] listaCadena = cadena.toCharArray();
        boolean espacioBlanco = true;
        for (int i = 0; i < listaCadena.length; i++) {
            // Detecta si es letra nuestra cadena en cada posición
            if (Character.isLetter(listaCadena[i])) {
                // Si hay un espacio en blanco
                if (espacioBlanco == true) {
                    listaCadena[i] = Character.toUpperCase(listaCadena[i]);
                    espacioBlanco = false;
                }
            } else {
                espacioBlanco = true;
            }
        }
        cadena = String.valueOf(listaCadena);
        cadena=cadena.trim();
        cadena=cadena.replaceAll("\\s{2,}", " ");
        } catch (Exception e) {
            System.out.println("Error en Convertir Cadena: " + e.getMessage());
        }
        return cadena;
    }

    public static void main(String[] args) {
        String cadena = "     CAL. LIMA NRO.      201 OTR. MIGUEL GRAU PAUC     ";
        cadena = convertirCadena(cadena);
        System.out.println(cadena);
    }

    
}
