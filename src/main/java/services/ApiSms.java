package services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ApiSms {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String numero = "938895028";
        String mensaje = "Hola";
        enviarSms(numero, mensaje);
    }

    public static void enviarSms(String numero, String mensaje ) throws UnsupportedEncodingException {

        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(60000).build();
        HttpClientBuilder builder = HttpClientBuilder.create();

        builder.setDefaultRequestConfig(config);
        CloseableHttpClient httpClient = builder.build();
        HttpPost post = new HttpPost("http://www.altiria.net/api/http");
        List<NameValuePair> parametersList = new ArrayList<NameValuePair>();

        parametersList.add(
                new BasicNameValuePair("cmd", "sendsms"));
        parametersList.add(
                new BasicNameValuePair("login", "hmnosyllanes@gmail.com"));
        parametersList.add(
                new BasicNameValuePair("passwd", "yllanes202110jpg"));
        parametersList.add(
                new BasicNameValuePair("dest", "51" + numero));
        parametersList.add(
                new BasicNameValuePair("msg", mensaje));
        try {
//Se fija la codificacion de caracteres de la peticion POST
            post.setEntity(new UrlEncodedFormEntity(parametersList, "UTF-8"));
        } catch (UnsupportedEncodingException uex) {
            System.out.println("ERROR: codificaci´on de caracteres no soportada");
        }
        CloseableHttpResponse response = null;

        try {
            System.out.println("Enviando petición");
            //Se env´ıa la petici´on51Interfaz HTTP para env´ıo de SMS, landings web y firma de documentos
            response = httpClient.execute(post);
//Se consigue la respuesta
            String resp = EntityUtils.toString(response.getEntity());
//Error en la respuesta del servidor
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("ERROR: Cóodigo de error HTTP: " + response.getStatusLine().getStatusCode());
                System.out.println("Compruebe que ha configurado correctamente la direccion/url ");
                System.out.println("suministrada por Altiria");
                return;
            } else {
//Se procesa la respuesta capturada en la cadena ’response’
                if (resp.startsWith("ERROR")) {
                    System.out.println(resp);
                    System.out.println("Código de error de Altiria. Compruebe las especificaciones");
                } else {
                    System.out.println(resp);
                }
            }
        } catch (Exception e) {
            System.out.println("Excepci´on");
            e.printStackTrace();
            return;
        } finally {
//En cualquier caso se cierra la conexi´onpost.releaseConnection();
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                    System.out.println("ERROR cerrando recursos");
                }
            }
        }
    }

}
