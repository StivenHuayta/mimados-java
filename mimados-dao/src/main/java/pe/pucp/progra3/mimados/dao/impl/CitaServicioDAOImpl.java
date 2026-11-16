package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.CitaServicioDAO;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.CitaServicio;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitaServicioDAOImpl implements CitaServicioDAO {

    @Override
    public void insertarCitaServicio(CitaServicio citaServicio) throws SQLException {
        /*
        CREATE PROCEDURE insertar_cita_servicio (
            IN p_cita_id INT,
            IN p_sede_servicio_id INT,
            IN p_precio_aplicado DECIMAL(10,2),
            IN p_notas VARCHAR(255)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_cita_servicio(?, ?, ?, ?)}")) {
            cs.setInt(1, citaServicio.getCita().getId());
            cs.setInt(2, citaServicio.getSedeServicio().getId());
            cs.setBigDecimal(3, citaServicio.getPrecioAplicado());
            cs.setString(4, citaServicio.getNotas());

            cs.execute();
        }
    }

    @Override
    public List<CitaServicio> listarServiciosPorCita(int idCita) throws SQLException {
        /*
        CREATE PROCEDURE listar_servicios_por_cita (IN id INT)
        SELECT s.servicio_id, s.nombre, s.descripcion, s.precio_referencial, s.duracion_minutos, s.activo, cs.sede_servicio_id, cs.precio_aplicado, cs.notas
        FROM cita_servicio cs, sede_servicio sd, servicio s
        WHERE id=cs.cita_id and cs.sede_servicio_id=sd.sede_servicio_id and sd.servicio_id=s.servicio_id;
        */
        List<CitaServicio> lista = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_servicios_por_cita(?)}")) {

            cs.setInt(1, idCita);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(idCita);

                    Servicio servicio = new Servicio(
                            rs.getInt("servicio_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio_referencial"),
                            rs.getInt("duracion_minutos"),
                            rs.getBoolean("activo")
                    );

                    SedeServicio sedeServicio = new SedeServicio();
                    sedeServicio.setId(rs.getInt("sede_servicio_id"));
                    sedeServicio.setServicio(servicio);

                    CitaServicio item = new CitaServicio(
                            cita,
                            sedeServicio,
                            rs.getBigDecimal("precio_aplicado"),
                            rs.getString("notas")
                    );
                    lista.add(item);
                }
            }
        }
        return lista;
    }

    @Override
    public void actualizarCitaServicio(CitaServicio citaServicio) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_cita_servicio (
            IN p_cita_id INT,
            IN p_sede_servicio_id INT,
            IN p_precio_aplicado DECIMAL(10,2),
            IN p_notas VARCHAR(255)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_cita_servicio(?, ?, ?, ?)}")) {
            cs.setInt(1, citaServicio.getCita().getId());
            cs.setInt(2, citaServicio.getSedeServicio().getId());
            cs.setBigDecimal(3, citaServicio.getPrecioAplicado());
            cs.setString(4, citaServicio.getNotas());

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el registro cita_servicio para cita ID "
                        + citaServicio.getCita().getId() + " y sede_servicio ID " + citaServicio.getSedeServicio().getId());
            }
        }
    }
}
