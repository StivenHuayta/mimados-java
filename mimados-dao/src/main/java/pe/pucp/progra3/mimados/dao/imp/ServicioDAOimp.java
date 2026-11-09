package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.ServicioDAO;
import pe.pucp.progra3.mimados.model.DiaSemana;
import pe.pucp.progra3.mimados.model.HorarioLaboral;
import pe.pucp.progra3.mimados.model.Sede;
import pe.pucp.progra3.mimados.model.Servicio;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAOimp implements ServicioDAO {

    @Override
    public List<Servicio> listar_servicios() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_servicios()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                Servicio servicio = new Servicio(
                        rs.getInt("id"),
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
    public void insertar_servicio(Servicio servicio) throws SQLException {
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
    public void eliminar_servicio(int id_servicio) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_servicio(?)}")){

            cs.setInt(1 , id_servicio);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el servicio con ID " + id_servicio);
            }
        }


    }


    @Override
    public void actualizar_servicio_precio(int id_servicio, BigDecimal precio) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_servicio_precio(?,?)}")) {

            cs.setInt(1 , id_servicio);
            cs.setBigDecimal(2,precio);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la sede con ID " + id_servicio);
            }

        }
    }

    @Override
    public Servicio mostrar_servicio(int id_servicio) throws SQLException {

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs =
                     connection.prepareCall("{CALL mostrar_servicio(?)}")) {

            cs.setInt(1, id_servicio);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {

                    return new Servicio(
                            id_servicio,
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
