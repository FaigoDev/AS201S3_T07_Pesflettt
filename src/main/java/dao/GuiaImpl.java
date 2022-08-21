package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import modelo.Facturador;
import modelo.Guia;
import modelo.Guia_Detalle;
import modelo.PlantaMinera;
import modelo.Viaje;
import services.convertidorS;

public class GuiaImpl extends Conexion implements ICRUD<Guia> {

    @Override
    public void registrar(Guia gui) throws Exception {
        String sql = "INSERT INTO CONTROL_GUIA (CODGUITRA,CODGUIREM,IDFAC,IDPLAMIN,IDVIA,FECGUI,TOTTMHCON,TOTCANSAC,ESTCONGUI) VALUES (?,?,?,?,?,?,?,?,'A')";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, gui.getCODGUITRA());
            ps.setString(2, gui.getCODGUIREM());
            ps.setString(3, gui.getFac().getIDFAC());
            ps.setString(4, gui.getPlan().getIDPLAMIN());
            ps.setInt(5, gui.getViaje().getIDVIA());
            SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(6,form.format(gui.getFECGUI()));
            ps.setDouble(7, gui.getTOTTMHCON());
            ps.setInt(8, gui.getTOTCANSAC());
            ps.execute();  //ejecuta la sentencia SQL precompilada
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("error en RegistrarGuiaD " + e.getMessage());
        }
    }

    @Override
    public void modificar(Guia gui) throws Exception {
        String sql = "UPDATE CONTROL_GUIA SET CODGUITRA=?, CODGUIREM=?, IDFAC =?,IDPLAMIN=?,IDVIA=?,FECGUI =?,TOTTMHCON=?,TOTCANSAC =?  WHERE IDCONGUI = ? ";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, gui.getCODGUITRA());
            ps.setString(2, gui.getCODGUIREM());
            ps.setString(3, gui.getFac().getIDFAC());
            ps.setString(4, gui.getPlan().getIDPLAMIN());
            ps.setInt(5, gui.getViaje().getIDVIA());
            SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(6,form.format(gui.getFECGUI()));
            ps.setDouble(7, gui.getTOTTMHCON());
            ps.setInt(8, gui.getTOTCANSAC());
            ps.setInt(9, gui.getIDCONGUI());
            ps.execute();  //ejecuta la sentencia SQL precompilada
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error en ModificarGuiaD " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Guia gui) throws Exception {
        String sql = "UPDATE CONTROL_GUIA SET ESTCONGUI = 'I' WHERE IDCONGUI = ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, gui.getIDCONGUI());
            ps.executeUpdate();
            ps.close();
            System.out.println("estoy en eliminar Dao " + gui.getIDCONGUI());
        } catch (Exception e) {
            System.out.println("Error en EliminarDao" + e.getMessage());
        } 
    }

    public List listar(int tipo) throws Exception {
        List<Guia> listado = null;
        Guia guis;
        String SQL = "";
        switch (tipo) {
            case 1:
                SQL = "SELECT * FROM VGuiaA";
                break;
            case 2:
                SQL = "SELECT * FROM VGuiaI";
                break;
            case 3:
                SQL = "SELECT * FROM VGuia";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                guis = new Guia();
                guis.setIDCONGUI(rs.getInt("IDCONGUI"));
                guis.setCODGUITRA(rs.getString("CODGUITRA"));
                guis.setCODGUIREM(rs.getString("CODGUIREM"));
                guis.getFac().setRAZSOCFAC(rs.getString("RAZSOCFAC"));
                guis.getPlan().setNOMPLAMIN(rs.getString("NOMPLAMIN"));
                guis.setTOTCANSAC((rs.getInt("TOTCANSAC")));
                guis.setTOTTMHCON((rs.getInt("TOTTMHCON")));
                guis.getViaje().setFECVIA(rs.getDate("FECVIA"));
                SimpleDateFormat forma = new SimpleDateFormat("dd-MMM-yyyy");
                guis.getViaje().setFECVER(forma.format(guis.getViaje().getFECVIA()));
                guis.setFECGUI(rs.getDate("FECGUI"));
                guis.getViaje().setIDVIA(rs.getInt("IDVIA"));
                SimpleDateFormat fec = new SimpleDateFormat("dd-MMM-yyyy");
                guis.setFECGUIVER(fec.format(guis.getFECGUI()));
                guis.setESTCONGUI(rs.getString("ESTCONGUI"));
                
                listado.add(guis);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarGuiasD " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }
    
    
     public Guia agregarFila(int idCONGUI) throws SQLException, Exception {
        Guia gui = null;
        Guia_Detalle guid = null;
        String sql2 = "select IDCONGUI, TOTTMHCON, TOTCANSAC FROM CONTROL_GUIA where IDCONGUI = " + idCONGUI;
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gui = new Guia();
                guid = new Guia_Detalle();
                gui.setIDCONGUI(rs.getInt("IDCONGUI"));
                gui.setTOTTMHCON(rs.getDouble("TOTTMHCON"));
                gui.setTOTCANSAC(rs.getInt("TOTCANSAC"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en el nuevo metodo DetalleDAO " + e.getMessage());
            e.printStackTrace();
        } finally {
            this.cerrar();
            return gui;
        }
    }
    
    public int obtenerUltimoId() {
        try {
            
            PreparedStatement ps1 = this.conectar().prepareStatement("SELECT MAX(IDCONGUI) as IDCONGUI FROM CONTROL_GUIA");
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                return rs.getInt("IDCONGUI");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en obtenerUltimoId" + e.getMessage());
        }
        return -1;
    }
    
    public void registroMultiple(List<Guia_Detalle> listaGuiaDetalle,
            int idgui) throws Exception {
        
        String sql = "INSERT INTO DETALLE_CONTROL  (IDCONGUI,IDMIN,TMHDETCON,CANSAC) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            for (Guia_Detalle guiD : listaGuiaDetalle) {
                String min =  obtenerCodigoMineral(guiD.getMin().getNOMMIN());
                ps.setInt(1, idgui);
                ps.setString(2, min);
                ps.setDouble(3, guiD.getTMHDETCON());
                ps.setInt(4, guiD.getCANSAC());
                
                ps.executeUpdate();
            }
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar multiple " + e.getMessage());
        } finally {
            this.cerrar();//si o si cerrar en caso funcione o no
        }
    }
    
    public List listarDetalle(int id) throws Exception {
        List<Guia_Detalle> listado = null;
        Guia_Detalle guid;
        String SQL = "select IDDETCON,D.TMHDETCON,D.CANSAC,M.IDMIN,(M.NOMMIN) || ', ' || (M.TIPMIN) || ', ' || (M.SITMIN) AS MINERAL,D.IDCONGUI FROM DETALLE_CONTROL D , MINERAL M WHERE IDCONGUI = '"+id+"' AND D.idmin= M.idmin";
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                guid = new Guia_Detalle();
                guid.setIDDETCON(rs.getInt("IDDETCON"));
                guid.setTMHDETCON(rs.getInt("TMHDETCON"));
                guid.setCANSAC(rs.getInt("CANSAC"));
                guid.getMin().setNOMMIN(rs.getString("MINERAL"));
                listado.add(guid);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarGuiaDetalleD " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }
    
    
    //select IDDETCON,D.TMHDETCON,D.CANSAC,M.IDMIN,(M.NOMMIN) || ', ' || (M.TIPMIN) || ', ' || (M.SITMIN) AS MINERAL,D.IDCONGUIFROM DETALLE_CONTROL D , MINERAL M WHERE IDCONGUI = ? AND D.idmin= M.idmin
    public int validarguia(Guia gui, int caso) throws Exception, Throwable {
        String remitente = gui.getCODGUIREM();
        String transportista = gui.getCODGUITRA();
        String SQL1 = "SELECT CODGUIREM FROM CONTROL_GUIA WHERE CODGUIREM='" + remitente + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);
            if (rs.next()) {
                caso = 1;
                rs.close();
            } else {
                String SQL2 = "SELECT CODGUIREM FROM CONTROL_GUIA WHERE CODGUITRA='" + transportista + "'";
                rs = st.executeQuery(SQL2);
            }
            if (rs.next()) {
                caso = 2;
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en validar Guia " + e.getMessage());
        }
        return caso;
    }
    
    public List<String> autocompleteFacturador(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select RAZFAC FROM FACTURADOR WHERE RAZFAC LIKE ? AND ROWNUM <=4";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            consulta = con.convertirCadena(consulta);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("RAZFAC"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteFacturadorD" + e.getMessage());
        }
        return lista;
    }

    public String obtenerCodigoFacturador(String cadena) throws SQLException, Exception {
        convertidorS con =new convertidorS();
        
        String sql = "select IDFAC FROM FACTURADOR WHERE RAZFAC = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            cadena = con.convertirCadena(cadena);
            ps.setString(1, cadena);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDFAC");
            }
            return rs.getString("IDFAC");
        } catch (Exception e) {
            System.out.println("Error en obtenerFacturadorD " + e.getMessage());
            throw e;
        }
    }
    
    public List<String> autocompletePlanta(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select NOMPLAMIN from PLANTA_MINERA WHERE NOMPLAMIN LIKE ? AND ROWNUM <=4";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            consulta = con.convertirCadena(consulta);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("NOMPLAMIN"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompletePlantaMD " + e.getMessage());
        }
        return lista;
    }

    public String obtenerCodigoPlanta(String cadena) throws SQLException, Exception {
        convertidorS con = new convertidorS();
        String sql = "select IDPLAMIN FROM PLANTA_MINERA WHERE NOMPLAMIN = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            cadena = con.convertirCadena(cadena);
            ps.setString(1, cadena);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDPLAMIN");
            }
            return rs.getString("IDPLAMIN");
        } catch (Exception e) {
            System.out.println("Error en obtenerCodigoPlantaMD " + e.getMessage());
            throw e;
        }
    }
    
    public List<String> autocompleteMineral(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        convertidorS con = new convertidorS();
        String sql = "select  CONCAT(CONCAT(CONCAT(m.NOMMIN,', '),CONCAT(m.TIPMIN,', ')),m.SITMIN) AS MINERAL from MINERAL m WHERE m.NOMMIN LIKE ? AND ROWNUM <= 5";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            consulta= con.convertirCadena(consulta);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("MINERAL"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteMineralD " + e.getMessage());
        }
        return lista;
    }
    
    
    
    
    public String obtenerCodigoMineral(String cadena) throws SQLException, Exception {
        String sql = "select IDMIN FROM MINERAL WHERE (NOMMIN) || ', ' || (TIPMIN) || ', ' || (SITMIN) = ?";
        convertidorS con = new convertidorS();
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            cadena = con.convertirCadena(cadena);
            ps.setString(1, cadena);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDMIN");
            }
            return rs.getString("IDMIN");
        } catch (Exception e) {
            System.out.println("Error en obtenerCodigoMineralD " + e.getMessage());
            throw e;
        }
    }
    
    public boolean existe(Guia guia, List<Guia> listaguia) {
        for (Guia guias : listaguia) {
            if (guia.equals(guias)) {
                return true;
            }
        }
        return false;
    }
    
    public void restaurar(Guia guia) throws Exception {
        String sql = "UPDATE CONTROL_GUIA set ESTCONGUI = 'A' where IDCONGUI=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, guia.getIDCONGUI());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en Restaurar Dao " + guia.getIDCONGUI());
        } catch (Exception e) {
            System.out.println("Error en restaurarD" + e.getMessage());
        }
    }
    
    public void traerDatos(Guia gui) throws SQLException, Exception {
        String sql = "SELECT V.IDVIA AS IDVIA,(P.NOMPER) || ', ' || (P.APEPER) AS NOMCON,(VE.MARVEH) || ', ' || (VE.MODVEH) AS NOMVEH,INITCAP(NOMDIS) || ', ' || INITCAP(NOMPRO) || ', ' || INITCAP(NOMDEP) AS DISVIA,V.DIRVIA AS DIRVIA FROM VIAJE V, PERSONA P, VEHICULO VE, UBIGEO U WHERE V.IDPER = P.IDPER AND V.IDVEH = VE.IDVEH AND V.CODUBI = U.CODUBI AND TIPPER = 'C' AND V.IDVIA=?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setInt(1, gui.getViaje().getIDVIA());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                gui.getViaje().setIDCON(rs.getString("NOMCON"));
                gui.getViaje().setIDVEH(rs.getString("NOMVEH"));
                gui.getViaje().setCODUBI(rs.getString("DISVIA"));
                gui.getViaje().setDIRVIA(rs.getString("DIRVIA"));
                System.out.println("Hay datotraer " + gui.getViaje().getIDCON() + gui.getViaje().getIDVEH());
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en traerDatosDao" + e.getMessage());
        }
    }
    
    @Override
    public List<Guia> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}