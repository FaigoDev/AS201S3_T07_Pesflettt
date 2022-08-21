package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Usuario;
import services.UsuarioS;
import services.convertidorS;

public class UsuarioD extends Conexion {

    public void registrarUsuario(Usuario usu, int nivel) throws Exception {
        String contrasenaEncriptada = usu.getCONUSU();
        contrasenaEncriptada = UsuarioS.generarEncriptacion(contrasenaEncriptada);
        String SQL = "INSERT INTO USUARIO (NOMUSU,CONUSU,NIVUSU,CODUSU,ESTUSU) VALUES (?,?,?,?,'E')";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(SQL);
            ps.setString(1, usu.getNOMUSU());
            ps.setString(2, contrasenaEncriptada);
            ps.setInt(3, nivel);
            ps.setString(4, usu.getCodigo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al Registrar UsuarioD" + e.getMessage());
        }
    }

    public int obtenerUltimoId() {
        try {
            PreparedStatement ps1 = this.conectar().prepareStatement("SELECT MAX(IDUSU) as IDUSU FROM USUARIO");
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                return rs.getInt("IDUSU");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en obtenerUltimoId" + e.getMessage());
        }
        return -1;
    }

    public void registrarPersona(Usuario usu, int nivel) throws Exception {
        String SQL = "INSERT INTO PERSONA (NOMPER,APEPER,DNIPER,LICPER,CORPER,CELPER,SUEPER,CODUBI,IDUSU,ESTPER,DIRPER,TIPPER) VALUES (?,?,?,?,?,?,?,?,?,'A',?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(SQL);
            ps.setString(1, usu.getNOMPER());
            ps.setString(2, usu.getAPEPER());
            ps.setString(3, usu.getDNIPER());
            ps.setString(4, usu.getLICCON());
            ps.setString(5, usu.getCORPER());
            ps.setString(6, usu.getCELPER());
            ps.setDouble(7, usu.getSUEPER());
            ps.setString(8, usu.getCODUBI());
            ps.setInt(9, usu.getIDUSU());
            ps.setString(10, usu.getDIRPER());
            if (nivel == 1) {
                ps.setString(11, "C");
            } else {
                ps.setString(11, "A");
            }

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al Registrar PersonaD" + e.getMessage());
        }
    }

    public List<String> autocompleteUbigeo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        String sql = "select  CONCAT(CONCAT(CONCAT(u.NOMDIS,', '),CONCAT(u.NOMPRO,', ')),u.NOMDEP) AS UBIGEODESC from UBIGEO u WHERE u.NOMDIS LIKE ? AND ROWNUM <= 5";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, "%" + consulta.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("UBIGEODESC"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteUbigeoDao" + e.getMessage());
        }
        return lista;
    }

    public String obtenerCodigoUbigeo(String cadenaUbi) throws SQLException, Exception {
        String sql = "select CODUBI  FROM UBIGEO u WHERE CONCAT(CONCAT(CONCAT(u.NOMDIS,', '),CONCAT(u.NOMPRO,', ')),u.NOMDEP) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, cadenaUbi.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("CODUBI");
            }
            return rs.getString("CODUBI");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Escribe un distrito válido"));
            System.out.println("Error en obtenerCodigoUbigeo " + e.getMessage());
            throw e;
        }
    }

    public int validarUsuario(Usuario usu, int caso) throws Exception, Throwable {
        String correo = usu.getCORPER();
        String usuario = usu.getNOMUSU();
        String dni = usu.getDNIPER();
        String direccion = usu.getDIRPER();
        String celular = usu.getCELPER();
        String SQL1 = "SELECT IDPER FROM PERSONA WHERE CORPER='" + correo + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                caso = 1;
                rs.close();
                System.out.println("Este es caso 1" + caso);
            } else {
                String SQL2 = "SELECT IDUSU FROM USUARIO WHERE NOMUSU='" + usuario + "'";
                rs = st.executeQuery(SQL2);
            }
            if (rs.next()) {
                caso = 2;
                rs.close();
                System.out.println("Este es caso 2" + caso);
            } else {
                String SQL3 = "SELECT IDPER FROM PERSONA WHERE DNIPER='" + dni + "' ";
                rs = st.executeQuery(SQL3);
            }
            if (rs.next()) {
                caso = 3;
                rs.close();
                System.out.println("Este es caso 3" + caso);
            } else {
                String SQL3 = "SELECT IDPER FROM PERSONA WHERE DIRPER='" + direccion + "' ";
                rs = st.executeQuery(SQL3);
            }
            if (rs.next()) {
                caso = 3;
                rs.close();
                System.out.println("Este es caso 4" + caso);
            } else {
                String SQL4 = "SELECT IDPER FROM PERSONA WHERE CELPER='" + celular + "' ";
                rs = st.executeQuery(SQL4);
            }
            if (rs.next()) {
                caso = 5;
                rs.close();
                System.out.println("Este es caso 5" + caso);
            }
            rs.close();
            st.close();
        } catch (Exception e) {

        }
        System.out.println("Este va a ser el retorno po" + caso);
        return caso;
    }

    public List listar() throws Exception {
        List<Usuario> listado = null;
        Usuario usus;
        String SQL = "SELECT * FROM VUSUARIOSA";
        convertidorS con = new convertidorS();
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                usus = new Usuario();
                usus.setIDUSU(rs.getInt("IDUSU"));
                usus.setNOMUSU(rs.getString("NOMUSU"));
                usus.setNOMPER(rs.getString("NOMBRE"));
                usus.setCORPER(rs.getString("CORPER"));
                usus.setCELPER(rs.getString("CELPER"));
                usus.setCODUBI(rs.getString("DISUSU"));
                usus.setCODUBI(con.convertirCadena(usus.getCODUBI()));
                usus.setDIRPER(rs.getString("DIRUSU"));
                usus.setCARGO(rs.getString("TIPPER"));
                listado.add(usus);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarUsuariosD: " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public int ingresoUsuario(Usuario usu, int caso) throws Exception, Throwable {
        String usuario = usu.getNOMUSU();
        String contrasena = usu.getCONUSU();
        contrasena = UsuarioS.generarEncriptacion(contrasena);

        String SQL1 = "SELECT IDUSU,NOMUSU,NIVUSU FROM USUARIO WHERE UPPER(NOMUSU) = UPPER('" + usuario + "') AND CONUSU = '" + contrasena + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                usu.setIDUSU(rs.getInt("IDUSU"));
                usu.setNOMUSU(rs.getString("NOMUSU"));
                usu.setCARGO(rs.getString("NIVUSU"));
                caso = 1;
                rs.close();
            } else {
                caso = 0;
                System.out.println("Caso 0");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Usuario y COntraseña no encontrado " + e);
        }
        return caso;
    }

    public int verificarCorreo(Usuario usu, int caso) throws Exception, Throwable {
        String correo = usu.getCORPER();
        System.out.println("Usuario es esto " + correo);

        String SQL1 = "SELECT U.IDUSU AS IDUSU ,NOMUSU,NOMPER,APEPER,CORPER FROM USUARIO U, PERSONA P WHERE U.IDUSU = P.IDUSU AND UPPER(CORPER) = UPPER('" + correo + "')";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                usu.setIDUSU(rs.getInt("IDUSU"));
                usu.setNOMUSU(rs.getString("NOMUSU"));
                usu.setNOMPER(rs.getString("NOMPER"));
                usu.setAPEPER(rs.getString("APEPER"));
                usu.setCORPER(rs.getString("CORPER"));
//                usu.setCARGO(rs.getString("NIVUSU"));
                caso = 1;
                rs.close();
                System.out.println("Este es caso 1" + caso + usu.getIDUSU());
            } else {
                caso = 0;
                System.out.println("Caso 0");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Usuario no encontrado " + e);
        }
        return caso;
    }
    
    public int verificarContraseña(Usuario usu, int caso, String nombre) throws Exception, Throwable {
        String contrasena = usu.getCONUSU();
        contrasena = UsuarioS.generarEncriptacion(contrasena);
        System.out.println("Usuario es esto " + nombre + "esta es la contra" + contrasena);

        String SQL1 = "SELECT U.IDUSU AS IDUSU ,NOMUSU,CORPER,CONUSU FROM USUARIO U, PERSONA P WHERE U.IDUSU = P.IDUSU AND CONUSU = '" + contrasena + "' AND NOMPER || ', ' || APEPER = '" + nombre +"'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                usu.setIDUSU(rs.getInt("IDUSU"));
                usu.setNOMUSU(rs.getString("NOMUSU"));
                usu.setCORPER(rs.getString("CORPER"));
//                usu.setCARGO(rs.getString("NIVUSU"));
                caso = 1;
                rs.close();
                System.out.println("Este es caso 1" + caso + usu.getIDUSU());
            } else {
                caso = 0;
                System.out.println("Caso 0");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en verficar contraseña" + e);
        }
        return caso;
    }
    
    public void guardarUsuario(Usuario usu) throws Exception, Throwable {
        String usuario = usu.getNOMUSU();
        String contrasena = usu.getCONUSU();
        contrasena = UsuarioS.generarEncriptacion(contrasena);
        String SQL1 = "SELECT NOMPER,APEPER,CORPER FROM PERSONA P,USUARIO U WHERE NOMUSU = '" + usuario + "' AND CONUSU = '" + contrasena + "' AND P.IDUSU=U.IDUSU";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            rs.next();
            usu.setUSUOFICIAL(rs.getString("NOMPER"));
            usu.setUSUOFICIAL(usu.getUSUOFICIAL()+", "+rs.getString("APEPER"));
            usu.setCORPER(rs.getString("CORPER"));
            System.out.println("nombre " + usu.getUSUOFICIAL());
            rs.close();
            st.close();
        } catch (Exception e) {

        }
    }

    public int validarCodigo(int caso, String codigo) {
        Usuario usu = new Usuario();
        String SQL1 = "SELECT IDUSU FROM USUARIO WHERE CODUSU = '" + codigo + "'";
        System.out.println("el codigo" + codigo);
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                usu.setIDUSU(rs.getInt("IDUSU"));
                caso = 0;
                String sql = "UPDATE USUARIO set ESTUSU = 'A' where IDUSU=?";
                try {
                    PreparedStatement ps = this.conectar().prepareStatement(sql);
                    ps.setInt(1, usu.getIDUSU());
                    ps.executeUpdate();
                    ps.close();
                } catch (Exception e) {
                    System.out.println("Error en ActivarD" + e.getMessage());
                }
                rs.close();
                System.out.println("Este es caso 1: " + usu.getIDUSU());
            } else {
                caso = 1;
                System.out.println("Caso 1");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en validarCodigo " + e);
        }
        return caso;
    }
    
    public int validarCodigoCambio(int caso, Usuario usu) {
        System.out.println("le codigo: " + usu.getCodigo());
        String codigo = usu.getCodigo();
        String SQL1 = "SELECT U.IDUSU AS IDUSU,NOMUSU,CORPER,NOMPER,APEPER FROM USUARIO U, PERSONA P WHERE U.IDUSU = P.IDUSU AND CODUSU = '" + codigo + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                usu.setIDUSU(rs.getInt("IDUSU"));
                usu.setNOMUSU(rs.getString("NOMUSU"));
                usu.setCORPER(rs.getString("CORPER"));
                usu.setNOMPER(rs.getString("NOMPER"));
                usu.setAPEPER(rs.getString("APEPER"));
                caso = 0;
                System.out.println("Este es caso 1: " + usu.getIDUSU());
            } else {
                caso = 1;
                System.out.println("Caso 1");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en validarCodigo " + e);
        }
        return caso;
    }

    public List<Usuario> datosTablaVehiculo() throws Exception {
        this.conectar();
        List<Usuario> listUsuarios = null;
        Usuario usu;
        String SQL = "SELECT * FROM VDatosTopVeh";
        try {
            listUsuarios = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                usu = new Usuario();
                usu.setVEHICULO(rs.getString("VEHICULO"));
                usu.setCANVIA(rs.getInt("CANVIA"));
                listUsuarios.add(usu);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao/datostab" + e.getMessage());
        }
        return listUsuarios;
    }

    public void cambiarContrasena(Usuario usu) throws Exception {
        String contrasenaEncriptada = usu.getCONUSU();
        contrasenaEncriptada = UsuarioS.generarEncriptacion(contrasenaEncriptada);
        System.out.println("el Id: " + usu.getIDUSU() + " la contra " + contrasenaEncriptada);
        String sql = "UPDATE USUARIO set CONUSU = ? where IDUSU=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, contrasenaEncriptada);
            ps.setInt(2, usu.getIDUSU());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en cambiar contraseña " + usu.getIDUSU());
        } catch (Exception e) {
            System.out.println("Error en cambiar contraseña" + e.getMessage());
        }
    }
    
    public void cambiarCodigo(Usuario usu) throws Exception {
        String sql = "UPDATE USUARIO set CODUSU = ? where IDUSU=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, usu.getCodigo());
            ps.setInt(2, usu.getIDUSU());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en cambiar codigo " + usu.getIDUSU());
        } catch (Exception e) {
            System.out.println("Error en cambiar codigo" + e.getMessage());
        }
    }
    
}
