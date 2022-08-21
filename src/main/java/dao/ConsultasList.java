package dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import modelo.Facturador;
import modelo.Guia;
import modelo.Guia_Detalle;
import modelo.Mineral;
import modelo.PlantaMinera;
import modelo.Usuario;
import modelo.Viaje;

public class ConsultasList extends Conexion {

    public List<Guia_Detalle> listarDetGui(int codigoGuia) throws Exception {
        List<Guia_Detalle> detalle = new ArrayList();
        String SQL = "SELECT DC.IDDETCON, M.NOMMIN, M.TIPMIN, M.SITMIN, DC.TMHDETCON, DC.CANSAC FROM CONTROL_GUIA CG, DETALLE_CONTROL  DC, MINERAL M WHERE CG.IDCONGUI = ? AND CG.IDCONGUI= DC.IDCONGUI AND DC.IDMIN = M.IDMIN";
        Guia_Detalle guidet;
        Mineral mins;
        try {
//            CallableStatement ps = this.conectar().prepareCall("{call spDetalleGuia(?)}");
            PreparedStatement ps = this.conectar().prepareCall(SQL);
            ps.setInt(1, codigoGuia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                guidet = new Guia_Detalle();
                guidet.setIDDETCON(rs.getInt("IDDETCON"));
                guidet.getMin().setNOMMIN(rs.getString("NOMMIN"));
                guidet.getMin().setTIPMIN(rs.getString("TIPMIN"));
                guidet.getMin().setSITMIN(rs.getString("SITMIN"));
                guidet.setTMHDETCON(rs.getDouble("TMHDETCON"));
                guidet.setCANSAC(rs.getInt("CANSAC"));
                detalle.add(guidet);
            }
        } catch (Exception e) {
            System.out.println("Error en dao/Detetalle " + e.getMessage());
        }
        return detalle;
    }

    public List listar() throws Exception {
        List<Guia> listado = null;
        Guia guis;
        Viaje vias;
        Facturador facs;
        PlantaMinera plans;
        String SQL = "SELECT * FROM VGuiaA";

        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                guis = new Guia();
                guis.setIDCONGUI(rs.getInt("IDCONGUI"));
                guis.setCODGUITRA(rs.getString("CODGUITRA"));
                guis.setCODGUIREM(rs.getString("CODGUIREM"));
                guis.setTOTTMHCON(rs.getDouble("TOTTMHCON"));
                guis.setTOTCANSAC(rs.getInt("TOTCANSAC"));
                guis.getFac().setRAZSOCFAC(rs.getString("RAZSOCFAC"));
                guis.getPlan().setNOMPLAMIN(rs.getString("NOMPLAMIN"));
                guis.getViaje().setFECVIA(rs.getDate("FECVIA"));
                SimpleDateFormat forma = new SimpleDateFormat("dd-MM-yyyy");
                guis.getViaje().setFECVER(forma.format(guis.getViaje().getFECVIA()));
                guis.setFECGUI(rs.getDate("FECGUI"));
                SimpleDateFormat fec = new SimpleDateFormat("dd-MM-yyyy");
                guis.setFECGUIVER(fec.format(guis.getFECGUI()));
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

    public List<Facturador> datosChartBar() throws Exception {
        List<Facturador> listFacturadores = null;
        Facturador fac;
        String SQL = "SELECT * FROM VDatosGraficoB ORDER BY IDFAC";
        try {
            listFacturadores = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                fac = new Facturador();
                fac.setIDFAC(rs.getString("IDFAC"));
                fac.setRAZSOCFAC(rs.getString("RAZFAC"));
                fac.setCANFAC(rs.getInt("CANFAC"));
                listFacturadores.add(fac);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao/CHART " + e.getMessage());
        }
        return listFacturadores;
    }

    public List<Usuario> datosChartPie() throws Exception {
        List<Usuario> listUsuarios = null;
        Usuario usu;
        String SQL = "SELECT * FROM VDATOSTOPCON";
        try {
            listUsuarios = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                usu = new Usuario();
                usu.setNOMBRE(rs.getString("NOMBRE"));
                usu.setCANVIA(rs.getInt("CANVIA"));
                listUsuarios.add(usu);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao/con-pie" + e.getMessage());
        }
//        finally {
//            this.cerrar();
//        }
        return listUsuarios;
    }

    public List<Usuario> datosChartBar2() throws Exception {
        List<Usuario> listUsuarios = null;
        Usuario usu;
        String conductor = "Miguel Angel, Domingo Manrique";
        String SQL = "SELECT * FROM VDATOSCONVEH WHERE CONDUCTOR = '" + conductor + "'";
        try {
            listUsuarios = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                usu = new Usuario();
                usu.setNOMBRE(rs.getString("CONDUCTOR"));
                usu.setVEHICULO(rs.getString("VEHICULO"));
                usu.setCANUSU(rs.getInt("CANUSO"));
                listUsuarios.add(usu);
            }
            rs.close();
            st.close();
            System.out.println("Estoy en datos del grafico de b 2:");
        } catch (Exception e) {
            System.out.println("Error en dao/CHART " + e.getMessage());
        }
        return listUsuarios;
    }

    public List<Usuario> datosChartPie2() throws Exception {
        List<Usuario> listUsuarios = null;
        Usuario usu;
        String conductor = "Miguel Angel, Domingo Manrique";
        String SQL = "SELECT * FROM VDATOSCONPLM WHERE CONDUCTOR = '" + conductor + "'";
        try {
            listUsuarios = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                usu = new Usuario();
                usu.setPLANTA(rs.getString("PLANTA"));
                usu.setCANVIS(rs.getInt("CANVIS"));
                listUsuarios.add(usu);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao/con-pie 2" + e.getMessage());
        }
        return listUsuarios;
    }

}
