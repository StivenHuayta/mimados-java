package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.PuestoServicioDAO;
import pe.pucp.progra3.mimados.model.PuestoServicio;
import pe.pucp.progra3.mimados.model.Sede;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PuestoServicioDAOimp implements PuestoServicioDAO {

    @Override
    public List<PuestoServicio> listar_servicios_puesto(int id_puesto) throws SQLException {

        List<PuestoServicio> servicios_de_puesto = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_servicios_puesto(?)}");){

            cs.setInt( 1 , id_puesto );
            ResultSet rs = cs.executeQuery();
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
                servicios_de_puesto.add(ps);
            }
        }
        return servicios_de_puesto;

    }

    @Override
    public void eliminar_puesto_servicio(int id_puesto_servicio) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_puesto_servicio(?)}")){

            cs.setInt(1 , Types.INTEGER);

            cs.execute();

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio-puesto con ID " + id_puesto_servicio);
            }
        }
    }

    @Override
    public void insertar_puesto_servicio(PuestoServicio ps) throws SQLException {

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
    public PuestoServicio mostrar_puesto_servicio(int id_puesto_servicio) throws SQLException {

        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL mostrar_puesto_servicio( ? )}");){

            cs.setInt(1 , id_puesto_servicio);
            ResultSet rs = cs.executeQuery();

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
            return ps;
        }




    }
}
