package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.SedeServicioDAO;
import pe.pucp.progra3.mimados.model.DTO.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.Sede;
import pe.pucp.progra3.mimados.model.SedeServicio;
import pe.pucp.progra3.mimados.model.Servicio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SedeServicioDAOimp implements SedeServicioDAO {

    @Override
    public void insertar_sede_servicio(SedeServicio sedeServicio) throws SQLException {
        /*
        CREATE PROCEDURE insertar_sede_servicio(
            IN p_servicio_id INT,
            IN p_sede_id INT,
            OUT p_sede_servicio_id INT
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_sede_servicio(?, ?, ?)}")) {
            cs.registerOutParameter(3, Types.INTEGER);

            cs.setInt(1, sedeServicio.getServicio().getId());
            cs.setInt(2, sedeServicio.getSede().getId());

            cs.execute();

            int idGenerado = cs.getInt(3);
            sedeServicio.setId(idGenerado);
        }
    }

    @Override
    public List<SedeServicioDTO> listar_servicios_por_sede(int idSede) throws SQLException {
        /*
        CREATE PROCEDURE listar_servicios_por_sede(IN p_sede_id INT)
        SELECT b.servicio_id, b.nombre, b.descripcion, b.precio_referencial, b.duracion_minutos, a.activo as activo_sede
        FROM sede_servicio a, servicio b WHERE a.servicio_id=b.servicio_id and a.sede_id=p_sede_id;
        */
        List<SedeServicioDTO> servicios = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_servicios_por_sede(?)}")) {

            cs.setInt(1, idSede);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    SedeServicioDTO ser = new SedeServicioDTO(
                            rs.getInt("sede_servicio_id"),
                            idSede,
                            rs.getInt("servicio_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio_referencial"),
                            rs.getInt("duracion_minutos"),
                            rs.getBoolean("activo_sede")
                    );
                    servicios.add(ser);
                }
            }
        }
        return servicios;
    }

    @Override
    public void eliminar_sede_servicio(int idSedeServicio) throws SQLException {
        /*
        CREATE PROCEDURE eliminar_sede_servicio(
            IN p_sede_servicio_id INT
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL eliminar_sede_servicio(?)}")) {
            cs.setInt(1, idSedeServicio);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la relación sede_servicio con ID " + idSedeServicio);
            }
        }
    }
}
