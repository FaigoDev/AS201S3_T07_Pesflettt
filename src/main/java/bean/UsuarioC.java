package bean;

import dao.UsuarioD;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.swing.ImageIcon;
import modelo.Usuario;
import org.primefaces.PrimeFaces;
import services.ApiCorreo;
import services.ApiSms;
import services.ReniecS;
import services.UsuarioS;
import services.convertidorS;

@Named(value = "usuarioC")
@SessionScoped
public class UsuarioC implements Serializable {

    private Usuario usu;
    private UsuarioD dao;
    private ApiCorreo cor;
    private UsuarioS ser;
    private int nivel;
    private List<Usuario> listadousu;
    private int intento = 3;
    private int intento2 = 2;
    private String pase;
    private String pagina;
    private int contador;
    private int segundo = 5;
    private boolean activarIntento;
    private boolean activarLicencia;
    private boolean confirmacion;
    private boolean paseDni;
    private boolean captcha;
    private String nombre;
    private String correo;
    private String asunto;
    private String cuerpo;
    private String usuario;
    private String contrasenaConfirm;
    private convertidorS con;
    private int paseSalir = 0;
    private int tamanoLista;

    public UsuarioC() {
        usu = new Usuario();
        dao = new UsuarioD();
        cor = new ApiCorreo();
        con = new convertidorS();

    }

    public void solicitarContraseña() throws Throwable {
        try {
            int caso = 0;
            caso = dao.validarUsuario(usu, caso);
            switch (caso) {
                case 0:
                    if (usu.getCARGO().equals("CONDUCTOR")) {
                        if (usu.getLICCON().length() == 9) {
                            System.out.println("Entre aca conductor");
                            enviarCorreo();
                            nivel = 1;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Solicitud Enviada"));
                        }
                    } else if (usu.getCARGO().equals("ADMINISTRADOR")) {
                        nivel = 2;
                        System.out.println("Este es correo " + usu.getCORPER());
                        enviarCorreo();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Solicitud Enviada"));
                    }
                    usuario = usu.getNOMUSU();
                    correo = usu.getCORPER();

                    System.out.println("Mi correo y usuario " + correo + usuario);
                    PrimeFaces.current().ajax().update("form");
                    PrimeFaces.current().ajax().update("formC");
                    usu.setDIRPER(con.convertirCadena(usu.getDIRPER()));
                    usu.setCODUBI(dao.obtenerCodigoUbigeo(usu.getCODUBI()));
                    dao.registrarUsuario(usu, nivel);
                    int id = dao.obtenerUltimoId();
                    usu.setIDUSU(id);
                    dao.registrarPersona(usu, nivel);
                    FacesContext.getCurrentInstance().getExternalContext().redirect("Usuario_Lista.xhtml");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Registro Exitoso"));

                    break;
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El correo ya existe"));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El Usuario ya existe"));
                    break;
                case 3:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El Dni ya existe"));
                    break;
                case 4:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La Dirección ya existe"));
                    break;
                case 5:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El celular ya existe"));
                    break;

            }
            System.out.println("confirmacion = " + confirmacion);
        } catch (Exception e) {
            System.out.println("Error en enviarDatos " + e.getMessage());
        }
    }

    public void confirmarContraseña() throws Exception {
        try {
            if (usu.getCONUSUCON().equals(contrasenaConfirm)) {
                usu.setNOMUSU(usuario);
                usu.setCONUSU(contrasenaConfirm);
                System.out.println("Nombre persona y nivel " + usu.getNOMPER() + nivel);
                System.out.println("nombres " + usu.getNOMPER() + usu.getAPEPER());
                usu.setDIRPER(con.convertirCadena(usu.getDIRPER()));
                usu.setCODUBI(dao.obtenerCodigoUbigeo(usu.getCODUBI()));
                usu.setCodigo(UsuarioS.generarCodigo());
                dao.registrarUsuario(usu, nivel);
                int id = dao.obtenerUltimoId();
                usu.setIDUSU(id);
                dao.registrarPersona(usu, nivel);
                FacesContext.getCurrentInstance().getExternalContext().redirect("Usuario_Lista.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Registro Exitoso"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Confirme contraseña válida"));
                System.out.println("Estoy en error de contra");
            }
        } catch (Exception e) {
            System.out.println("Error en ConfirmarContraseñaC " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Confirme bien la contraseña"));
        }

    }

    public void modificar() {
        try {

        } catch (Exception e) {
            System.out.println("Error en Modificar " + e.getMessage());
        }
    }

    public List<String> completeTextUbigeo(String query) throws SQLException, Exception {
        UsuarioD daoUbi = new UsuarioD();
        return daoUbi.autocompleteUbigeo(query);
    }

    public void enviarCorreo() {
        try {
            ser.generarContrasena(usu);
            usu.setCodigo(ser.generarCodigo());
            String destinatario = usu.getCORPER();
            String asunto = "Invitación Pesflet";
            String cuerpo = "";
            contrasenaConfirm = usu.getCONUSU();
            cor.enviarConGMailContrasena(destinatario, asunto, cuerpo, usu);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Solicitud Enviada"));
        } catch (Exception e) {
            System.out.println("Error en EnviarCorreo " + e.getMessage());
        }
    }

    public void validarUsuario() throws Throwable {
        try {
            int caso = 0;
            caso = dao.validarUsuario(usu, caso);
            switch (caso) {
                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Correo ya está en uso"));
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Correo ya está en uso"));
                    break;
                case 3:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Usuario ya está en uso"));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error en ValidarUsuarioC: " + e.getMessage());
        }
    }

    public void enviarMensaje() {
        try {
            ApiSms sms = new ApiSms();
            sms.enviarSms(usu.getCELPER(), usu.getCUERPO());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Mesaje Enviado"));
            usu.setCELPER("");
            usu.setCUERPO("");

        } catch (Exception e) {
            System.out.println("Error en enviar mensaje " + e.getMessage());

        }

    }

    public void listar() {
        try {
            listadousu = dao.listar();
        } catch (Exception e) {
            System.out.println("Error en ListarC" + e.getMessage());
        }
    }

    public void listarTopVehiculo() {
        try {
            listadousu = dao.datosTablaVehiculo();
        } catch (Exception e) {
            System.out.println("Error en Listartopcon" + e.getMessage());
        }
    }

    public void paseConfirm() {
        confirmacion = false;
        System.out.println("Aca en pase confirm " + confirmacion);
    }

    public void buscarDni() {
        try {
            ReniecS re = new ReniecS();
            convertidorS con = new convertidorS();
            paseDni = re.buscarDni(usu);
            if (paseDni == true) {
                usu.setNOMPER(con.convertirCadena(usu.getNOMPER()));
                usu.setAPEPER(con.convertirCadena(usu.getAPEPER()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Busqueda", "Dni encontrado"));
            } else if (paseDni == false) {
                usu.setNOMPER("");
                usu.setAPEPER("");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "Dni no encontrado"));
            }

        } catch (Exception e) {
        }
    }

    public void submitCaptcha() {
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Verificado", "Correcto");
        FacesContext.getCurrentInstance().addMessage(null, mensaje);
    }

    public void actualizar() {
        try {
            System.out.println("contraactualizar " + usu.getContraseña());
        } catch (Exception e) {
        }
    }

    public void mensaje() {
        try {
            PrimeFaces.current().ajax().update("dlgdatos");
            PrimeFaces.current().ajax().update("msj");
        } catch (Exception e) {
            System.out.println("Error en mostrar mensaje " + e.getMessage());
        }

    }

    public void licencia() {
        try {
            if (usu.getCARGO().equals("ADMINISTRADOR")) {
                activarLicencia = true;
                System.out.println("Toyaca");
            } else {
                System.out.println("Ahora en else");
                activarLicencia = false;
            }

        } catch (Exception e) {
            System.out.println("Error en traer licencia " + e.getMessage());
        }

    }

    /////////////Loguin/////////
    public void ingresoUsuario() throws Throwable {
        try {
            int caso = 0;
            caso = dao.ingresoUsuario(usu, caso);

            switch (caso) {

                case 0:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Usuario o Contraseña Incorrecta"));
                    intento = intento - 1;
                    if (intento != 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Precausión", "Te quedan " + intento + " Intentos"));
                    }
                    if (intento == 0 && intento2 != 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Precausión", "Captcha Activado"));

                        activarIntento = false;
                        captcha = true;
                        intento = 3;
                        intento2 = intento2 - 1;
                        System.out.println("intento2 " + intento2);
                    }
                    if (intento == 3 && intento2 == 0) {
                        PrimeFaces current = PrimeFaces.current();
                        current.executeScript("PF('bui').show();");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Precausión", "Sistema Bloqueado"));
                        captcha = false;
                        PrimeFaces.current().ajax().update("captcha");

                    }
                    break;
                case 1:
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoUsuario", usu);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Inicio Exitoso"));
                    if (usu.getCARGO().equals("1")) {
                        pase = "1";
                    } else {
                        pase = "2";
                    }
                    FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
                    dao.guardarUsuario(usu);
                    nombre = usu.getUSUOFICIAL();
                    numero = 60;
                    ApiCorreo.enviarActividad("Nuevo Inicio de Sesión", usu);
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en ingresar Usuario: " + e.getMessage());
        }
    }

    public void recuperarContrasena() throws Throwable {
        try {
            int caso = 0;
            caso = dao.verificarCorreo(usu, caso);

            switch (caso) {

                case 0:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Correo inexistente"));
                    break;
                case 1:
                    enviarCorreoRecuperacion();
                    dao.cambiarCodigo(usu);
                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('wdlgMensaje').show();");
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en recuperar contraseña p: " + e.getMessage());
        }
    }

    public void enviarCorreoRecuperacion() {
        try {
            usu.setCodigo(ser.generarCodigo());
            String destinatario = usu.getCORPER();
            String asunto = "Recuperación de contraseña";
            String cuerpo = "";
            cor.enviarConGMailRecuperarContrasena(destinatario, asunto, cuerpo, usu);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Solicitud Enviada"));
        } catch (Exception e) {
            System.out.println("Error en EnviarCorreo de recuperación " + e.getMessage());
        }
    }

    public void cambiarContrasena() throws Throwable {
        try {
            int caso = 0;
            caso = dao.verificarContraseña(usu, caso, nombre);

            switch (caso) {

                case 0:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Contraseña incorrecta"));
                    break;
                case 1:
                    enviarCorreoCambiarContrasena();
                    System.out.println("usuario" + nombre);
                    dao.cambiarCodigo(usu);
                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('wdlgMensaje').show();");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en cambiar contraseña p: " + e.getMessage());
        }
    }

    public void enviarCorreoCambiarContrasena() {
        try {

            usu.setCodigo(ser.generarCodigo());
            String destinatario = usu.getCORPER();
            String asunto = "Nueva Contraseña";
            String cuerpo = "";
            cor.enviarConGMailCambiarContrasena(destinatario, asunto, cuerpo, usu, nombre);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Solicitud Enviada"));
        } catch (Exception e) {
            System.out.println("Error en EnviarCorreo de cambio " + e.getMessage());
        }
    }

    public void conteoTiempo() {
        try {
            if (intento == 3 && intento2 == 0) {
                intento = 3;
                intento2 = 2;
                for (segundo = 5; segundo > 0; segundo--) {
                    System.out.println(segundo);
                    PrimeFaces.current().ajax().update("block");
                    PrimeFaces.current().ajax().update("conteo");
                    delaySegundo();

                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('bui').hide();");
                    salir();
                }
                intento = 3;
                activarIntento = true;

            }

        } catch (Exception e) {
            System.out.println("Error en conteo: " + e.getMessage());
        }

    }

    public static void delaySegundo() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error en Delay: " + e.getMessage());
        }
    }

    public void validarCodigo() {
        try {
            int caso = 0;
            caso = dao.validarCodigo(caso, usu.getCodigo());
            switch (caso) {
                case 0:
                    FacesContext.getCurrentInstance().getExternalContext().redirect("Loguin.xhtml");
                    break;

                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ingrese un código Válido"));
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en validar CódigoC: " + e.getMessage());
        }
    }

    public void validarCodigoCambio() {
        try {
            int caso = 0;
            caso = dao.validarCodigoCambio(caso, usu);
            switch (caso) {
                case 0:
                    ser.generarContrasena(usu);
                    dao.cambiarContrasena(usu);
                    enviarCorreoContrasenaNueva();
                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('wdlgMensaje').show();");
                    break;

                case 1:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ingrese un código Válido"));
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en validar CódigoC de cambio: " + e.getMessage());
        }
    }

    public void enviarCorreoContrasenaNueva() {
        try {
            String destinatario = usu.getCORPER();
            String asunto = "Nueva contraseña";
            String cuerpo = "";
            cor.enviarConGMailNuevaContrasena(destinatario, asunto, cuerpo, usu);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Enviada"));
        } catch (Exception e) {
            System.out.println("Error en EnviarCorreo de cambio " + e.getMessage());
        }
    }

    public void enviarCorreoAyuda() {
        try {
            correo = "pesflet@gmail.com";
            System.out.println("Este es la info : " + correo + asunto + cuerpo);
            System.out.println("este es el cuerpo" + cuerpo);
            cor.enviarConGMailAyuda(correo, asunto, cuerpo);
            rellenarCuerpo();
            cuerpo = "";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Correo Enviado Correctamente"));
            PrimeFaces.current().ajax().update("dlgDatos");
        } catch (Exception e) {
            System.out.println("Error en enviar correoC " + e.getMessage());
        }
    }

    public void rellenarCuerpo() {
        cuerpo = "Hola, Mi nombre es: ______, Quería ayuda en: ________, Espero su respuesta, Mi correo es: _________ gracias.";
    }

//    public String puestoConductor() {
//        String color = "";
//        tamanoLista = listadousu.size();
//        System.out.println("este es tamaño " + tamanoLista);
//        switch (tamanoLista) {
//            case 1:
//                color = "5F5D5D";
//                break;
//            case 2:
//                color = "ff0000";
//                tamanoLista = tamanoLista - 1;
//                break;
//            case 3:
//                color = "ff0000";
//                tamanoLista = tamanoLista - 1;
//                break;
//
//        }
//        return color;
//    }
    public void pasar() {
    }

    public void salir() throws IOException {
        try {
            Usuario us = obtenerObjetoSesion();
            if (us != null && paseSalir == 1) {
                cerrarSesion();
                System.out.println("salir en cerrar sesion");
            }
        } catch (Exception e) {
            System.out.println("Error en salir/ ELiminar Sesion " + e.getMessage());
        }
    }

//    // Obtener el objeto de la sesión activa
    public Usuario obtenerObjetoSesion() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("objetoUsuario");
    }

//    // Si la sesión no está iniciada no permitirá entrar a otra vista de la aplicación
    public void seguridadSesion() throws IOException {
        if (obtenerObjetoSesion() == null) {
            System.out.println("Entre a null seguridadSesion ");
            FacesContext.getCurrentInstance().getExternalContext().redirect("Loguin.xhtml");
        }

    }

//    // Cerrar y limpiar la sesión y direccionar al xhtml inicial del proyecto
    public void cerrarSesion() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().redirect("Loguin.xhtml");
    }

    // Si la sesión está activa se redirecciona a la vista principal
    public void seguridadLogin() throws IOException {
        Usuario us = obtenerObjetoSesion();
        if (us != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
        }
    }

    //Si intenta entrar a una vista que no es de su nivel
    public void seguridadNivel() throws IOException {
        try {
            if (pase.equals("1")) {
                System.out.println("Tpy ava nivelsaso");
                FacesContext.getCurrentInstance().getExternalContext().
                        redirect("dashboard.xhtml");
            }
        } catch (Exception e) {
            System.out.println("Error en seguridadNivel " + e.getMessage());
        }
    }

    private int numero = 60;

    public void decrementoNumero() throws IOException {
        numero--;
        if (numero == 0) {
            cerrarSesion();
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

    public void activarSesion() {
        numero = 60;
    }

    public void limpiar() {
        usu = new Usuario();
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public UsuarioD getDao() {
        return dao;
    }

    public void setDao(UsuarioD dao) {
        this.dao = dao;
    }

    public ApiCorreo getCor() {
        return cor;
    }

    public void setCor(ApiCorreo cor) {
        this.cor = cor;
    }

    public UsuarioS getSer() {
        return ser;
    }

    public void setSer(UsuarioS ser) {
        this.ser = ser;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<Usuario> getListadousu() {
        return listadousu;
    }

    public void setListadousu(List<Usuario> listadousu) {
        this.listadousu = listadousu;
    }

    public int getIntento() {
        return intento;
    }

    public void setIntento(int intento) {
        this.intento = intento;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public boolean isActivarIntento() {
        return activarIntento;
    }

    public void setActivarIntento(boolean activarIntento) {
        this.activarIntento = activarIntento;
    }

    public boolean isActivarLicencia() {
        return activarLicencia;
    }

    public void setActivarLicencia(boolean activarLicencia) {
        this.activarLicencia = activarLicencia;
    }

    public boolean isConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenaConfirm() {
        return contrasenaConfirm;
    }

    public void setContrasenaConfirm(String contrasenaConfirm) {
        this.contrasenaConfirm = contrasenaConfirm;
    }

    public boolean isPaseDni() {
        return paseDni;
    }

    public void setPaseDni(boolean paseDni) {
        this.paseDni = paseDni;
    }

    public convertidorS getCon() {
        return con;
    }

    public void setCon(convertidorS con) {
        this.con = con;
    }

    public String getPase() {
        return pase;
    }

    public void setPase(String pase) {
        this.pase = pase;
    }

    public boolean isCaptcha() {
        return captcha;
    }

    public void setCaptcha(boolean captcha) {
        this.captcha = captcha;
    }

    public int getIntento2() {
        return intento2;
    }

    public void setIntento2(int intento2) {
        this.intento2 = intento2;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public int getTamanoLista() {
        return tamanoLista;
    }

    public void setTamanoLista(int tamanoLista) {
        this.tamanoLista = tamanoLista;
    }

    public int getPaseSalir() {
        return paseSalir;
    }

    public void setPaseSalir(int paseSalir) {
        this.paseSalir = paseSalir;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

}
