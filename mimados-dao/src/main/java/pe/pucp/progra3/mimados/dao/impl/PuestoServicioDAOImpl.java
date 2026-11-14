package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.PuestoServicioDAO;
import pe.pucp.progra3.mimados.model.entity.PuestoServicio;
import pe.pucp.progra3.mimados.model.entity.Sede;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PuestoServicioDAOImpl implements PuestoServicioDAO {

    @Override
    public List<PuestoServicio> listarServicioPuestoGeneral() throws SQLException {
        List<PuestoServicio> serviciosDePuesto = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_puesto_servicio()}");
            ResultSet rs = cs.executeQuery()){
            while(rs.next()){
                PuestoServicio ps = new PuestoServicio(
                        rs.getInt("id"),
                        rs.getInt("servicio_id"),
                        rs.getInt("puesto_id"),
                        rs.getBoolean("activo")
                );
                serviciosDePuesto.add(ps);
            }
        }
        return serviciosDePuesto;
    }

    @Override
    public List<PuestoServicio> listarServiciosPuesto(int idPuesto) throws SQLException {

        List<PuestoServicio> serviciosDePuesto = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_servicios_puesto(?)}");){

            cs.setInt(1, idPuesto);
            try (ResultSet rs = cs.executeQuery()) {
                while(rs.next()){
                    PuestoServicio ps = new PuestoServicio(
                            rs.getInt("id"),
                            rs.getInt("servicio_id"),
                            rs.getInt("puesto_id"),
                            rs.getBoolean("activo"),

                            rs.getString("usuario_creacion"),
                            rs.getString("usuario_ultima_actualizacion"),
                            rs.getObject("fecha_ultima_actualizacion" , LocalDateTime.class),
                            rs.getObject("fecha_creacion" , LocalDateTime.class)


                    );
                    serviciosDePuesto.add(ps);
                }
            }
        }
        return serviciosDePuesto;

    }

    @Override
    public void eliminarPuestoServicio(int idPuestoServicio) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_puesto_servicio(?)}")){

            cs.setInt(1 , Types.INTEGER);

            cs.execute();

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio-puesto con ID " + idPuestoServicio);
            }
        }
    }

    @Override
    public void insertarPuestoServicio(PuestoServicio ps) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL insertar_puesto_servicio(?,?)}")){


            cs.setInt(1 , ps.getServicio().getId());
            cs.setInt(2, ps.getPuesto().getId());

            cs.execute();


//            int id_generado = cs.getInt(4);
//            ps.setId(id_generado);
        }

    }

    @Override
    public PuestoServicio mostrarPuestoServicio(int idPuestoServicio) throws SQLException {

        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL mostrar_puesto_servicio_por_id( ? )}");){

            cs.setInt(1, idPuestoServicio);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new PuestoServicio(
                            rs.getInt("id"),
                            rs.getInt("servicio_id"),
                            rs.getInt("puesto_id"),
                            rs.getBoolean("activo")
                    );
                }
            }
            return null;
        }
    }


    @Override
    public void actualizarPuestoServicio(PuestoServicio ps) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_puesto_servicio(?,?,?,?)}")) {

            cs.setInt(1 , ps.getId());
            cs.setInt(2, ps.getServicio().getId());
            cs.setInt(3, ps.getPuesto().getId());
            cs.setBoolean(4, ps.getActivo());

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el puesto-servicio con ID " + ps.getId());
            }

        }



    }
}
