package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.ServicioDAO;

import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.math.BigDecimal;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ServicioDAOImpl implements ServicioDAO {

    @Override
    public List<Servicio> listarServicios() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_servicios()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                Servicio servicio = new Servicio(
                        rs.getInt("servicio_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("precio_referencial"),
                        rs.getInt("duracion_minutos"),
                        rs.getBoolean("activo")
                );
                servicios.add(servicio);
            }
        }
        return servicios;
    }

    @Override
    public void insertarServicio(Servicio servicio) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL insertar_servicio(?,?,?,?, ?)}")){

            cs.registerOutParameter(5 , Types.INTEGER);

            cs.setString(1 , servicio.getNombre());
            cs.setString(2, servicio.getDescripcion());
            cs.setBigDecimal(3, servicio.getPrecioReferencial());
            cs.setInt(4 , servicio.getDuracionMinutos());

            cs.execute();

            int id_generado = cs.getInt(5);
            servicio.setId(id_generado);
        }
    }

    @Override
    public void eliminarServicio(int idServicio) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_servicio(?)}")){

            cs.setInt(1, idServicio);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio con ID " + idServicio);
            }
        }


    }


    @Override
    public void actualizarServicioPrecio(int idServicio, BigDecimal precio) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_servicio_precio(?,?)}")) {

            cs.setInt(1, idServicio);
            cs.setBigDecimal(2,precio);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio con ID " + idServicio);
            }

        }
    }

    @Override
    public void actualizarServicio(Servicio servicio) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_servicio(?,?,?,?,?,?)}")) {

            cs.setInt(1 , servicio.getId());
            cs.setString(2,servicio.getNombre());
            cs.setString(3,servicio.getDescripcion());
            cs.setBigDecimal(4,servicio.getPrecioReferencial());
            cs.setInt(5 , servicio.getDuracionMinutos());
            cs.setBoolean(6 , servicio.getActivo());

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio con ID " + servicio.getId());
            }

        }
    }

    @Override
    public Servicio mostrarServicio(int idServicio) throws SQLException {

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs =
                     connection.prepareCall("{CALL mostrar_servicio(?)}")) {

            cs.setInt(1, idServicio);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {

                    return new Servicio(
                            idServicio,
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio_referencial"),
                            rs.getInt("duracion_minutos"),
                            rs.getBoolean("activo")
                    );
                }
            }
        }

        return null;
    }
}
