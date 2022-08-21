package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Mineral;

public class MineralImpl extends Conexion implements ICRUD<Mineral> {  //se crea una clase hija de Conexion y se implementa una interfaz

    @Override
    public void registrar(Mineral min) throws Exception {
        String sql = "insert into MINERAL (NOMMIN,TIPMIN,SITMIN,ESTMIN) values (?,?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, min.getNOMMIN());
            ps.setString(2, min.getTIPMIN());
            ps.setString(3, min.getSITMIN());
            ps.setString(4, "A");
            ps.executeUpdate();  //devuelve el numero de registros afectados
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error al ingresar MineralD" + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    @Override
    public void modificar(Mineral min) throws Exception {
        String sql = "update MINERAL set NOMMIN = ?, TIPMIN = ? , SITMIN=? WHERE IDMIN=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, min.getNOMMIN());
            ps.setString(2, min.getTIPMIN());
            ps.setString(3, min.getSITMIN());
            ps.setString(4, min.getIDMIN());
            ps.executeUpdate();  //devuelve el numero de registros afectados
            ps.close(); //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error al Modificar MineralD" + e.getMessage());
        }
    }

    @Override
    public void eliminar(Mineral min) throws Exception {
        String sql = "UPDATE MINERAL SET ESTMIN ='I' WHERE IDMIN = ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, min.getIDMIN());
            ps.executeUpdate();  //devuelve el numero de registros afectador
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error en eliminarD" + e.getMessage());
        }
    }

    public List listar(int tipo) throws Exception {
        List<Mineral> listado = null;
        Mineral mins;
        String SQL = "";
        switch (tipo) {
            case 1:
                SQL = "SELECT * FROM VMineralA";
                break;
            case 2:
                SQL = "SELECT * FROM VMineralI";
                break;
            case 3:
                SQL = "SELECT * FROM VMineral";
                break;
        }

        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                mins = new Mineral();
                mins.setIDMIN(rs.getString("IDMIN"));
                mins.setNOMMIN(rs.getString("NOMMIN"));
                mins.setTIPMIN(rs.getString("TIPMIN"));
                mins.setSITMIN(rs.getString("SITMIN"));
                mins.setESTMIN(rs.getString("ESTMIN"));
                listado.add(mins);
            }
            rs.close();  //cierra los resltados de la consulta
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarMineralesD" + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public int validarmineral(Mineral min, int caso) throws Exception, Throwable {
        String nombre = min.getNOMMIN();
        String tipo = min.getTIPMIN();
        String situacion = min.getSITMIN();
        String SQL = "SELECT IDMIN FROM MINERAL WHERE NOMMIN = '" + nombre + "' AND TIPMIN='"+ tipo+ "' AND SITMIN= '" + situacion +"'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            rs = st.executeQuery(SQL);
            if(rs.next()){
            caso = 1;
            rs.close();
            st.close();
        }
            else{
                caso  = 0;
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en validar Mineral " + e.getMessage());
        }
        return caso;
    }

    public void restaurar(Mineral min) throws Exception {
        String sql = "UPDATE MINERAL set ESTMIN = 'A' where IDMIN=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, min.getIDMIN());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en Restaurar Dao " + min.getIDMIN());
        } catch (Exception e) {
            System.out.println("Error en restaurarD" + e.getMessage());
        }
    }

    @Override
    public List<Mineral> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}