package dao;

import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.Viaje;
import org.primefaces.PrimeFaces;
import services.convertidorS;

public class ViajeImpl extends Conexion implements ICRUD<Viaje> {  //se crea una clase hija de Conexion y se implementa una interfaz

    @Override
    public void registrar(Viaje via) throws Exception {
        String sql = "INSERT INTO VIAJE (IDPER,IDVEH,CODUBI,DIRVIA,GASVIA,FECVIA,ESTVIA) VALUES (?,?,?,?,?,?,'A')";
        System.out.println(via.getFECVIA());
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, via.getIDCON());
            ps.setString(2, via.getIDVEH());
            ps.setString(3, via.getCODUBI());
            ps.setString(4, via.getDIRVIA());
            ps.setDouble(5, via.getGASVIA());
            SimpleDateFormat forma = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(6, forma.format(via.getFECVIA()));
            ps.execute();  //ejecuta la sentencia SQL precompilada
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error en RegistrarDao" + e.getMessage());
        }
    }

    @Override
    public void modificar(Viaje via) throws Exception {
        String sql = "UPDATE VIAJE SET IDPER=?, IDVEH=?, CODUBI =?,DIRVIA=?,GASVIA=?,FECVIA =?  WHERE IDVIA = ? ";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, via.getIDCON());
            ps.setString(2, via.getIDVEH());
            ps.setString(3, via.getCODUBI());
            ps.setString(4, via.getDIRVIA());
            ps.setDouble(5, via.getGASVIA());
            SimpleDateFormat forma = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(6, forma.format(via.getFECVIA()));
            ps.setInt(7, via.getIDVIA());
            ps.execute();  //ejecuta la sentencia SQL precompilada
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("error en ModificarDao" + e.getMessage());
        }

    }

    @Override
    public void eliminar(Viaje via) throws Exception {
        String sql = "UPDATE VIAJE SET ESTVIA = 'I' WHERE IDVIA = ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, via.getIDVIA());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en EliminarDao" + e.getMessage());
        }
    }

    public int validarviaje(Viaje via, int caso) throws Exception, Throwable {
        String SQL = "SELECT IDVIA FROM VIAJE WHERE IDPER=? AND IDVEH=? AND CODUBI=? AND DIRVIA=? AND GASVIA=? AND FECVIA=?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(SQL);
            ps.setString(1, via.getIDCON());
            ps.setString(2, via.getIDVEH());
            ps.setString(3, via.getCODUBI());
            ps.setString(4, via.getDIRVIA());
            ps.setDouble(5, via.getGASVIA());
            SimpleDateFormat forma = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(6, forma.format(via.getFECVIA()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                caso = 1;
            } else {
                caso = 0;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en validarviajedao  " + e.getMessage());
        }
        return caso;
    }

    public List listar(int tipo) throws Exception {
        List<Viaje> listadovia = null;
        Viaje vias;
        convertidorS con;
        String SQL = "";
        switch (tipo) {
            case 1:
                SQL = "SELECT * FROM VViajeA";
                break;
            case 2:
                SQL = "SELECT * FROM VViajeI";
                break;
            case 3:
                SQL = "SELECT * FROM VViaje";
                break;
        }

        try {
            listadovia = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                vias = new Viaje();
                con = new convertidorS();
                vias.setIDVIA(rs.getInt("IDVIA"));
                vias.setIDCON(rs.getString("NOMCON"));
                vias.setIDCON(vias.getIDCON() + ", " + rs.getString("APECON"));
                vias.setIDVEH(rs.getString("MARVEH"));
                vias.setIDVEH(vias.getIDVEH() + ", " + rs.getString("PLAVEH"));
                vias.setCODUBI(rs.getString("DISVIA"));
                vias.setCODUBI(con.convertirCadena(vias.getCODUBI()));
                vias.setDIRVIA(rs.getString("DIRVIA"));
                vias.setGASVIA(rs.getDouble("GASVIA"));
                vias.setFECVIA(rs.getDate("FECVIA"));
                SimpleDateFormat forma = new SimpleDateFormat("dd-MMM-yyyy");
                vias.setFECVER(forma.format(vias.getFECVIA()));
                vias.setESTVIA(rs.getString("ESTVIA"));
                listadovia.add(vias);
            }
            rs.close(); //cierra los resltados de la consulta
            st.close();
        } catch (Exception e) {
            System.out.println("Error en Listar Viajes" + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listadovia;
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
            System.out.println("Error en obtenerCodigoUbigeo " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Complete el destino Correctamente"));
            throw e;
        }
    }

    public List<String> autocompleteConductor(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select  CONCAT(CONCAT(CONCAT(c.NOMPER,', '),''),c.APEPER) AS NOMBRE from PERSONA c WHERE c.NOMPER LIKE ? AND ROWNUM <= 3 AND TIPPER = 'C'";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            consulta = con.convertirCadena(consulta);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("NOMBRE"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteConductor" + e.getMessage());
        }
        return lista;
    }

    public String obtenerCodigoConductor(String consulta) throws SQLException, Exception {
        Viaje via = new Viaje();
        String sql = "select IDPER FROM PERSONA c WHERE CONCAT(CONCAT(CONCAT(c.NOMPER,', '),''),c.APEPER) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDPER");
            }
            return rs.getString("IDPER");
        } catch (Exception e) {
            System.out.println("Error en obtenerCodigoConductor " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Complete el Conductor Correctamente"));
            via.setIDCON("");
            PrimeFaces.current().ajax().update("dlgViaje:dlgdatos:txtConductor");
            throw e;
        }
    }

    public List<String> autocompleteVehiculo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select  CONCAT(CONCAT(CONCAT(v.MARVEH,', '),''),v.PLAVEH) AS VEHICULO from VEHICULO v WHERE v.MARVEH LIKE ? AND ROWNUM <= 3";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            consulta = con.convertirCadena(consulta);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("VEHICULO"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteVehiculo " + e.getMessage());
        }
        return lista;
    }

    public String obtenerCodigoVehiculo(String consulta) throws SQLException, Exception {
        String sql = "select IDVEH FROM VEHICULO v WHERE CONCAT(CONCAT(CONCAT(v.MARVEH,', '),''),v.PLAVEH) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDVEH");
            }
            return rs.getString("IDVEH");
        } catch (Exception e) {
            System.out.println("Error en obtenerCodigoVehiculo " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Complete el Vehículo Correctamente"));
            throw e;
        }
    }

    public void restaurar(Viaje via) throws Exception {
        String sql = "UPDATE VIAJE set ESTVIA = 'A' where IDVIA=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, via.getIDVIA());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en Restaurar Dao " + via.getIDVIA());
        } catch (Exception e) {
            System.out.println("Error en restaurarD" + e.getMessage());
        }
    }
    
    public String obtenerCelular(String idConductor) throws SQLException, Exception {
        String sql = "select CELPER FROM PERSONA p WHERE p.IDPER= ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, idConductor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("CELPER");
            }
            return rs.getString("CELPER");
        } catch (Exception e) {
            System.out.println("Error en obtenerCelular " + e.getMessage());
            throw e;
        }
    }

    

//    public void validarviaje(Viaje via) throws Exception {
//        String conductor = via.getIDCON();
//        String vehiculo = via.getIDVEH();
//        String ubigeo = via.getCODUBI();
//        String fecha = via.getFECVER();
//        double gasto = via.getGASVIA();
//        String direccion = via.getDIRVIA();
//        String SQL = "SELECT IDCON FROM VIAJE WHERE IDCON='" + conductor + "' AND IDVEH='" + vehiculo + "' AND CODUBI='" + ubigeo + "' AND FECVIA='" + fecha + "' AND GASVIA='" + gasto + "' AND DIRVIA='" + direccion +"'";
//        try {
//            Statement st = this.conectar().createStatement();
//            ResultSet rs = st.executeQuery(SQL);
//            if (rs.next()) {
//                System.out.println("Este es codigo sin ejecutar " + via.getIDCON());
//                via.setIDCON(rs.getString("IDCON"));
//                System.out.println("Este es codigo ya ejecutado " + via.getIDCON());
//            } else {
//                via.setIDCON("");
//            }
//            System.out.println("Estoy en dao codigo final flash " + via.getIDCON());
//            rs.close();
//            st.close();
//        } catch (Exception e) {
//            System.out.println("Error en validar VIAJE " + e.getMessage());
//        }
//    }
    @Override
    public List<Viaje> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
