package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.PlantaMinera;
import services.convertidorS;

public class PlantaMineraImpl extends Conexion implements ICRUD<PlantaMinera> {  //se crea una clase hija de Conexion y se implementa una interfaz

    @Override
    public void registrar(PlantaMinera pla) throws Exception {
        String sql = "insert into Planta_Minera (RUCPLAMIN,NOMPLAMIN,CELPLAMIN,CORPLAMIN,CODUBI,DIRPLAMIN,ESTPLAMIN) values (?,?,?,?,?,?,'A')";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pla.getRUCPLAMIN());
            ps.setString(2, pla.getNOMPLAMIN());
            ps.setString(3, pla.getCELPLAMIN());
            ps.setString(4, pla.getCORPLAMIN());
            ps.setString(5, pla.getCODUBI());
            ps.setString(6, pla.getDIRPLAMIN());
            ps.executeUpdate();  //devuelve el numero de registros afectados
            ps.close();  //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error al Ingresar Planta Minera " + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    @Override
    public void modificar(PlantaMinera plan) throws Exception {
        String sql = "update PLANTA_MINERA set RUCPLAMIN=?, NOMPLAMIN=?,CELPLAMIN=?,CORPLAMIN=?, CODUBI=? ,DIRPLAMIN=? where IDPLAMIN= ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, plan.getRUCPLAMIN());
            ps.setString(2, plan.getNOMPLAMIN());
            ps.setString(3, plan.getCELPLAMIN());
            ps.setString(4, plan.getCORPLAMIN());
            ps.setString(5, plan.getCODUBI());
            ps.setString(6, plan.getDIRPLAMIN());
            ps.setString(7, plan.getIDPLAMIN());
            System.out.println("Estoy en modificar Dao");
            ps.executeUpdate();  //devuelve el numero de registros afectados
            ps.close();  //cierra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error al Modificar Planta Minera: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(PlantaMinera pla) throws Exception {
        String sql = "UPDATE PLANTA_MINERA set ESTPLAMIN = 'I' where IDPLAMIN=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pla.getIDPLAMIN());
            ps.executeUpdate(); //devuelve el numero de registros afectados
            ps.close(); //cierrra la sentencia SQL precompilada
        } catch (Exception e) {
            System.out.println("Error en eliminarD" + e.getMessage());
        }
    }

    public List listar(int tipo) throws Exception {
        List<PlantaMinera> listado = null;
        PlantaMinera plan;
        convertidorS con;
        String SQL = "";
        switch (tipo) {
            case 1:
                SQL = "SELECT * FROM VPlantaMineraA";
                break;
            case 2:
                SQL = "SELECT * FROM VPlantaMineraI";
                break;
            case 3:
                SQL = "SELECT * FROM VPlantaMinera";
                break;
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                plan = new PlantaMinera();
                con = new convertidorS();
                plan.setIDPLAMIN(rs.getString("IDPLAMIN"));
                plan.setRUCPLAMIN(rs.getString("RUCPLAMIN"));
                plan.setNOMPLAMIN(rs.getString("NOMPLAMIN"));
                plan.setCELPLAMIN(rs.getString("CELPLAMIN"));
                plan.setCORPLAMIN(rs.getString("CORPLAMIN"));
                plan.setCODUBI(rs.getString("DISPLAMIN"));
                plan.setCODUBI(con.convertirCadena(plan.getCODUBI()));
                plan.setDIRPLAMIN(rs.getString("DIRPLAMIN"));
                plan.setESTPLAMIN(rs.getString("ESTPLAMIN"));
                listado.add(plan);
            }
            rs.close();  //cierra los resltados de la consulta
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ListarPlantaD " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public int validarplantamin(PlantaMinera plan, int caso) throws Exception, Throwable {
        String ruc = plan.getRUCPLAMIN();
        String nombre = plan.getNOMPLAMIN();
        String celular = plan.getCELPLAMIN();
        String correo = plan.getCORPLAMIN();
        String direccion = plan.getDIRPLAMIN();
        String SQL1 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE RUCPLAMIN='" + ruc + "'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(SQL1);

//                String SQL2 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE NOMPLAMIN='" + nombre + "'";
//
//                String SQL3 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE CORPLAMIN='" + correo + "'";
//
//                String SQL4 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE DIRPLAMIN='" + direccion + "'";
            if (rs.next()) {
                caso = 1;
                rs.close();
                System.out.println("Este es caso 1" + caso);
            } else {
                String SQL2 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE NOMPLAMIN='" + nombre + "'";
                rs = st.executeQuery(SQL2);

            }
            if (rs.next()) {
                caso = 2;
                rs.close();
                System.out.println("Este es caso 2" + caso);
            } else {
                String SQL3 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE CELPLAMIN='" + celular + "'";
                rs = st.executeQuery(SQL3);

            }
            if (rs.next()) {
                caso = 3;
                rs.close();
                System.out.println("Este es caso 3" + caso);
            } else {
                String SQL4 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE CORPLAMIN='" + correo + "'";
                rs = st.executeQuery(SQL4);

            }
            if (rs.next()) {
                caso = 4;
                rs.close();
                System.out.println("Este es caso 4" + caso);
            } else {
                String SQL5 = "SELECT RUCPLAMIN FROM PLANTA_MINERA WHERE DIRPLAMIN='" + direccion + "'";
                rs = st.executeQuery(SQL5);

            }
            if (rs.next()) {
                caso = 5;

                System.out.println("Este es caso 5" + caso);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en validar Planta Minera " + e.getMessage());
        }
        return caso;
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
            throw e;
        }
    }

    public String enviarUbigeo(String ubigeo) throws SQLException, Exception {
        String sql = "select CONCAT(CONCAT(CONCAT(u.NOMDEP,' '),CONCAT(u.NOMPRO,' ')),NOMDIS) AS DISTRITO from UBIGEO u WHERE CODUBI = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, ubigeo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ubigeo = rs.getString("DISTRITO");
                if (ubigeo.contains("Z")) {
                    ubigeo = ubigeo.replaceAll("Z", "S");
                }
                rs.close();
            }

        } catch (Exception e) {
            System.out.println("Error en EnviarUbigeoDao " + e.getMessage());
        }
        return ubigeo;
    }

    public String TraerDistrito(String ubigeo) throws SQLException, Exception {
        String sql = "select CONCAT(CONCAT(CONCAT(u.NOMDIS,', '),CONCAT(u.NOMPRO,', ')),NOMDEP) AS DISTRITO from UBIGEO u WHERE CODUBI = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, ubigeo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ubigeo = rs.getString("DISTRITO");
                rs.close();
            }

        } catch (Exception e) {
            System.out.println("Error en traerDistritoDao" + e.getMessage());
        }
        return ubigeo;
    }

    public void restaurar(PlantaMinera plan) throws Exception {
        String sql = "UPDATE PLANTA_MINERA set ESTPLAMIN = 'A' where RUCPLAMIN=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, plan.getRUCPLAMIN());
            ps.executeUpdate();
            ps.close();
            System.out.println("Estoy en Restaurar Dao " + plan.getRUCPLAMIN());
        } catch (Exception e) {
            System.out.println("Error en restaurarD" + e.getMessage());
        }
    }

    @Override
    public List<PlantaMinera> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
