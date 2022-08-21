package services;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import modelo.Usuario;

public class ApiCorreo {

    public static void enviarConGMailContraseña(String destinatario, String asunto, String cuerpo) {
        String remitente = "Aqui poner el correo remitente";
        String clave = "Aqui poner la contraseña";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try { 
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();
            System.out.println("Errorcito en awita de uwu: " + me.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void enviarConGMail(String destinatario, String asunto, String cuerpo) {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        asunto = "Reporte de Guías";
        cuerpo = "Empresa de tranporte, Hermanos Yllanes";
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
//        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            String url = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\PESFLET_Maven\\src\\main\\webapp\\reports\\prueba.pdf";

            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
//            message.setText(cuerpo);

//            
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(url)));
            adjunto.setFileName("prueba.pdf");

            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(adjunto);
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en: " + me.getMessage());
        }
    }

    public static void enviarConGMailCon(String destinatario, String asunto, String cuerpo) {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            String url = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\PESFLET_Maven\\src\\main\\webapp\\reports\\Bienvenida.png";

            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(url)));
            adjunto.setFileName("Bienvenida.png");

            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(adjunto);
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en: " + me.getMessage());
        }
    }

    public static void enviarConGMailContrasena(String destinatario, String asunto, String cuerpo, Usuario usu) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            String url = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\PESFLET_Maven\\src\\main\\webapp\\reports\\Bienvenida.png";

            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(url)));
            adjunto.setFileName("Bienvenida.png");

            BodyPart texto = new MimeBodyPart();

//            texto.setText(cuerpo);
            message.setContent("<h1>PESFLET, inventario de guias</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Bienvenido: <h2 style=\"color: red\" >" + usu.getNOMPER() + " " + usu.getAPEPER() + "<h2/> </h2>"
                    + "<h3> Nombre de Usuario:  <h3 style=\"color: red\">" + usu.getNOMUSU() + "<h3/> </h3>"
                    + "<h3> Contraseña:  <h3 style=\"color: red\">" + usu.getCONUSU() + "<h3/> </h3>"
                    + "<h3> Código Confirmación:  <h3 style=\"color: red\">" + usu.getCodigo() + "<h3/> </h3>"
                    + "<h3>Link Pesflet: http://localhost:9090/PESFLET_Maven/faces/vistas/Loguin.xhtml</h3>"
                    + "<h3>Link Pesflet: http://localhost:26672/PESFLET_Maven/faces/vistas/Loguin.xhtml</h3>", "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(adjunto);
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en: " + me.getMessage());
        }
    }

    public static void enviarConGMailRecuperarContrasena(String destinatario, String asunto, String cuerpo, Usuario usu) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();

            message.setContent("<h1>PESFLET, inventario de guias</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Hola: <h2 style=\"color: red\" >" + usu.getNOMPER() + " " + usu.getAPEPER() + "<h2/> </h2>"
                    + "<h3> Nombre de Usuario:  <h3 style=\"color: red\">" + usu.getNOMUSU() + "<h3/> </h3>"
                    + "<h3> Código Confirmación:  <h3 style=\"color: red\">" + usu.getCodigo() + "<h3/> </h3>"
                    + "<h3>Link Pesflet: http://localhost:9090/PESFLET_Maven/faces/vistas/Validar_Codigo_cambio.xhtml</h3>"
                    + "<h3>Link Pesflet: http://localhost:26672/PESFLET_Maven/faces/vistas/Validar_Codigo_cambio.xhtml</h3>", "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito de recuperar con en: " + me.getMessage());
        }
    }

    public static void enviarConGMailCambiarContrasena(String destinatario, String asunto, String cuerpo, Usuario usu, String nombre) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();

            message.setContent("<h1>PESFLET, inventario de guias</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Hola: <h2 style=\"color: red\" >" + nombre + "<h2/> </h2>"
                    + "<h3> Nombre de Usuario:  <h3 style=\"color: red\">" + usu.getNOMUSU() + "<h3/> </h3>"
                    + "<h3> Código Confirmación:  <h3 style=\"color: red\">" + usu.getCodigo() + "<h3/> </h3>"
                    + "<h3>Link Pesflet: http://localhost:9090/PESFLET_Maven/faces/vistas/Validar_Codigo_cambio.xhtml</h3>"
                    + "<h3>Link Pesflet: http://localhost:26672/PESFLET_Maven/faces/vistas/Validar_Codigo_cambio.xhtml</h3>", "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en enviar cambiar contraseña: " + me.getMessage());
        }
    }

    public static void enviarConGMailNuevaContrasena(String destinatario, String asunto, String cuerpo, Usuario usu) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();

            message.setContent("<h1>PESFLET, inventario de guias</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Hola: <h2 style=\"color: red\" >" + usu.getNOMPER() + ", " + usu.getAPEPER() + "<h2/> </h2>"
                    + "<h3> Nombre de Usuario:  <h3 style=\"color: red\">" + usu.getNOMUSU() + "<h3/> </h3>"
                    + "<h3> Tu Nueva Contraseña:  <h3 style=\"color: red\">" + usu.getCONUSU() + "<h3/> </h3>"
                    + "<h3>Link Pesflet: http://localhost:9090/PESFLET_Maven/faces/vistas/Loguin.xhtml</h3>"
                    + "<h3>Link Pesflet: http://localhost:26672/PESFLET_Maven/faces/vistas/Loguin.xhtml</h3>", "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en enviar nueva contraseña: " + me.getMessage());
        }
    }

    public static void enviarActividad(String asunto, Usuario usu) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        String destinatario = usu.getCORPER();
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String nombreHost = InetAddress.getLocalHost().getHostName();
            Date dateTime = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            String fechaHora = f.format(dateTime);

            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            BodyPart texto = new MimeBodyPart();
//            texto.setText(cuerpo);
            message.setContent("<h1>PESFLET, inventario de guias</h1>", "text/html");
            texto.setContent(message.getContent() + "<h2>Saludos: " + usu.getUSUOFICIAL() + " " + "<h2 style=\"color: red\" >" + " Nueva actividad en tu cuenta:<h2/> </h2>"
                    + "<h3> Fecha y Hora:  <h3 style=\"color: red\">" + fechaHora + "<h3/> </h3>"
                    + "<h3> Dirección IP:  <h3 style=\"color: red\">" + ip + "<h3/> </h3>"
                    + "<h3> localhost  <h3 style=\"color: red\">" + nombreHost + "<h3/> </h3>",
                    "text/html");
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito en: " + me.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
//        Date fecha = new Date();
//        DateTime dateTime = new DateTime(fecha);
//        int mes = dateTime.getMonthOfYear();
//        System.out.println("Mes: " + mes);
        String destinatario = "josearicochea4@gmail.com";
        String asunto = "Invitación Pesflet";
        String cuerpo = "";
        Usuario usu = new Usuario();
        usu.setNOMPER("Flavio");
        usu.setAPEPER("Illanes");
        usu.setNOMUSU("Faigo");
        usu.setCONUSU("COnater/56");
        usu.setCodigo("38492840");
//        UsuarioS usuarios = new UsuarioS();
//        usuarios.traerDatosSesion();
//        enviarConGMailContrasena(destinatario, asunto, cuerpo, usu);
        String fechaHora = "09/may/2021 23:13.21";
        String ip = "12424234234";
        String nombreHost = "0_0";

        enviarActividad(asunto, usu);

    }
    
      public static void enviarConGMailAyuda(String destinatario, String asunto, String cuerpo) throws IOException {
        String remitente = "pesflet@gmail.com";
        String clave = "pesflet2021";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
            System.out.println("Errorcito de recuperar con en: " + me.getMessage());
        }
    }
      
      
      
      


      
}


