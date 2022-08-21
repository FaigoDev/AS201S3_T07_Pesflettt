package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Facturador;
import org.primefaces.PrimeFaces;
import services.convertidorS;
public class FacturadorImpl extends Conexion implements ICRUD<Facturador> {

    @Override
    public void registrar(Facturador fac) throws Exception {
        String sql = "insert into FACTURADOR (RUCFAC,RAZFAC,CELFAC,CORFAC,CODUBI,DIRFAC,ESTFAC) values (?,?,?,?,?,?,'A')";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, fac.getRUCFAC());
            ps.setString(2, fac.getRAZSOCFAC());
            ps.setString(3, fac.getCELFAC());
            ps.setString(4, fac.getCORRFAC());
            ps.setString(5, fac.getCODUBI());
            ps.setString(6, fac.getDIRFAC());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en registrarDao " + fac.getRUCFAC() + fac.getRAZSOCFAC() + fac.getCELFAC() + fac.getCODUBI());
        } catch (Exception e) {
            System.out.println("Error al ingresar FacturadorD " + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    public void modificar(Facturador fac) throws Exception {
        String sql = "update FACTURADOR set  RUCFAC=? ,RAZFAC = ?,CELFAC = ? ,CORFAC=?,CODUBI = ?, DIRFAC=?  where IDFAC = ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, fac.getRUCFAC());
            ps.setString(2, fac.getRAZSOCFAC());
            ps.setString(3, fac.getCELFAC());
            ps.setString(4, fac.getCORRFAC());
            ps.setString(5, fac.getCODUBI());
            ps.setString(6, fac.getDIRFAC());
            ps.setString(7, fac.getIDFAC());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al Modificar FacturadorD" + e.getMessage());
        }
    }

    @Override
    public void eliminar(Facturador fac) throws Exception {
        String sql = "UPDATE FACTURADOR set ESTFAC = 'I' where RUCFAC=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, fac.getRUCFAC());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en eliminar Dao " + fac.getRUCFAC());
        } catch (Exception e) {
            System.out.println("Error en eliminarD" + e.getMessage());
        }
    }
    
    public void restaurar(Facturador fac) throws Exception {
        String sql = "UPDATE FACTURADOR set ESTFAC = 'A' where RUCFAC=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, fac.getRUCFAC());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en Restaurar Dao " + fac.getRUCFAC());
        } catch (Exception e) {
            System.out.println("Error en restaurarD" + e.getMessage());
        }
    }
    

    public List listar(int tipo) throws Exception {
        List<Facturador> listado = null;
        Facturador facs;
        convertidorS con;
        String SQL = "";
        switch (tipo) {
            case 1:
                SQL = "SELECT * FROM VFacturadorA";
                break;
            case 2:
                SQL = "SELECT * FROM VFacturadorI";
                break;
            case 3:
                SQL = "SELECT * FROM VFacturador";
                break;
        }

        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                facs = new Facturador();
                con = new convertidorS();
                facs.setIDFAC(rs.getString("IDFAC"));
                facs.setRUCFAC(rs.getString("RUCFAC"));
                facs.setRAZSOCFAC(rs.getString("RAZSOCFAC"));
                facs.setCELFAC(rs.getString("CELFAC"));
                facs.setCORRFAC(rs.getString("CORRFAC"));
                facs.setCODUBI(rs.getString("DISFAC"));
                facs.setCODUBI(con.convertirCadena(facs.getCODUBI()));
                facs.setDIRFAC(rs.getString("DIRFAC"));
                facs.setESTFAC(rs.getString("ESTFAC"));
                listado.add(facs);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarFacturadoresD" + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public List<String> autocompleteUbigeo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select  CONCAT(CONCAT(CONCAT(u.NOMDIS,', '),CONCAT(u.NOMPRO,', ')),u.NOMDEP) AS UBIGEODESC from UBIGEO u WHERE u.NOMDIS LIKE ? AND ROWNUM <= 5";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, "%" + consulta.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(con.convertirCadena(rs.getString("UBIGEODESC")));
                
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteUbigeoDao" + e.getMessage());
        }
        return lista;
    }

    public String enviarUbigeo(String ubigeo) throws SQLException, Exception {
        String sql = "select CONCAT(CONCAT(CONCAT(u.NOMDEP,' '),CONCAT(u.NOMPRO,' ')),NOMDIS) AS DISTRITO from UBIGEO u WHERE CODUBI = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1,ubigeo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ubigeo = rs.getString("DISTRITO");
                if(ubigeo.contains("Z")){
                    ubigeo = ubigeo.replaceAll("Z","S");
                }
                rs.close();
            } 
            

        } catch (Exception e) {
            System.out.println("Error en EnviarUbigeoDao " + e.getMessage());
        }
        return ubigeo;
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
    
    public String TraerDistrito(String ubigeo) throws SQLException, Exception {
        String sql = "select CONCAT(CONCAT(CONCAT(u.NOMDIS,', '),CONCAT(u.NOMPRO,', ')),NOMDEP) AS DISTRITO from UBIGEO u WHERE CODUBI = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1,ubigeo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ubigeo = rs.getString("DISTRITO");
                rs.close();
            } 
            

        } catch (Exception e) {
            System.out.println("Error en traerDistritoDao" + e.getMessage());
        }
        return ubigeo;
    }

    public int validarfacturador(Facturador fac, int caso) throws Exception, Throwable {
        String ruc = fac.getRUCFAC();
        String razon = fac.getRAZSOCFAC();
        String celular = fac.getCELFAC();
        String correo = fac.getCORRFAC();
        String direccion = fac.getDIRFAC();
        String SQL1 = "SELECT RUCFAC FROM FACTURADOR WHERE RUCFAC='" + ruc + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                caso = 1;    
                rs.close();  
                System.out.println("Este es caso 1" + caso);
            } else {
                String SQL2 = "SELECT RUCFAC FROM FACTURADOR WHERE RAZSOCFAC='" + razon + "'";
                rs = st.executeQuery(SQL2);

            }
            if (rs.next()) {
                caso = 2;
                rs.close();
                System.out.println("Este es caso 2" + caso);
            } else {
                String SQL3 = "SELECT RUCFAC FROM FACTURADOR WHERE CELFAC='" + celular + "'";
                rs = st.executeQuery(SQL3);

            }
            if (rs.next()) {
                caso = 3;
                rs.close();
                System.out.println("Este es caso 3" + caso);
            } else {
                String SQL4 = "SELECT RUCFAC FROM FACTURADOR WHERE CORRFAC='" + correo + "'";
                rs = st.executeQuery(SQL4);

            }
            if (rs.next()) {
                caso = 4;
                rs.close();
                System.out.println("Este es caso 4" + caso);
            } else {
                String SQL5 = "SELECT RUCFAC FROM FACTURADOR WHERE DIRFAC='" + direccion + "'";
                rs = st.executeQuery(SQL5);

            }
            if (rs.next()) {
                caso = 5;

                System.out.println("Este es caso 5" + caso);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            
        }
        System.out.println("Este va a ser el retorno po" + caso);
        return caso;
    }


    @Override
    public List<Facturador> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

