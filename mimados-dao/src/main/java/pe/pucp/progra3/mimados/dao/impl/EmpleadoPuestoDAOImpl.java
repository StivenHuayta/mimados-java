package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.EmpleadoPuestoDAO;
import pe.pucp.progra3.mimados.model.entity.EmpleadoPuesto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

public class EmpleadoPuestoDAOImpl implements EmpleadoPuestoDAO {

    @Override
    public void insertarEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) throws SQLException {
        /*
        CREATE PROCEDURE insertar_empleado_puesto (
            IN p_puesto_id INT,
            IN p_empleado_id INT,
            IN p_activo TINYINT 
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_empleado_puesto(?, ?, ?)}")) {
            cs.setInt(1, empleadoPuesto.getPuesto().getId());
            cs.setInt(2, empleadoPuesto.getEmpleado().getUsuario().getId());
            cs.setInt(3, (empleadoPuesto.getActivo() != null && empleadoPuesto.getActivo()) ? 1 : 0);

            cs.execute();
        }
    }

    @Override
    public void iniciarEmpleadoPuesto(int idEmpleadoPuesto) throws SQLException {
        /*
        CREATE PROCEDURE iniciar_empleado_puesto (
            IN p_empleado_puesto_id INT
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL iniciar_empleado_puesto(?)}")) {
            cs.setInt(1, idEmpleadoPuesto);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el registro empleado_puesto con ID " + idEmpleadoPuesto);
            }
        }
    }

    @Override
    public void eliminarEmpleadoPuesto(int idEmpleadoPuesto, LocalDate fechaFin) throws SQLException {
        /*
        CREATE PROCEDURE eliminar_empleado_puesto (
            IN p_empleado_puesto_id INT,
            IN p_fecha_fin DATE
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL eliminar_empleado_puesto(?, ?)}")) {
            cs.setInt(1, idEmpleadoPuesto);
            if (fechaFin != null) {
                cs.setDate(2, Date.valueOf(fechaFin));
            } else {
                cs.setNull(2, Types.DATE);
            }

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el registro empleado_puesto con ID " + idEmpleadoPuesto);
            }
        }
    }

    @Override
    public boolean tienePuestoVeterinarioActivo(int empleadoId) throws SQLException {
        String sql = "SELECT 1 FROM empleado_puesto ep "
                + "INNER JOIN puesto p ON ep.puesto_id = p.puesto_id "
                + "WHERE ep.empleado_id = ? AND ep.activo = 1 "
                + "AND LOWER(p.nombre) LIKE '%veterinari%' LIMIT 1";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empleadoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void sincronizarPuestosEmpleado(int empleadoId, boolean esAdmin, boolean esVeterinario) throws SQLException {
        String sqlBuscarPuestos = "SELECT puesto_id, LOWER(nombre) AS nombre FROM puesto";
        String sqlUpsert = "INSERT INTO empleado_puesto (puesto_id, empleado_id, activo, usuario_creacion) "
                + "VALUES (?, ?, ?, current_user()) "
                + "ON DUPLICATE KEY UPDATE activo = VALUES(activo), usuario_modificacion = current_user()";

        Integer puestoAdminId = null;
        Integer puestoVetId = null;

        try (Connection connection = DBManager.getInstance().getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sqlBuscarPuestos);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("puesto_id");
                    String nombre = rs.getString("nombre");
                    if (nombre != null) {
                        if (nombre.contains("admin")) {
                            puestoAdminId = id;
                        } else if (nombre.contains("veterinari")) {
                            puestoVetId = id;
                        }
                    }
                }
            }

            try (PreparedStatement psUpsert = connection.prepareStatement(sqlUpsert)) {
                if (puestoAdminId != null) {
                    psUpsert.setInt(1, puestoAdminId);
                    psUpsert.setInt(2, empleadoId);
                    psUpsert.setInt(3, esAdmin ? 1 : 0);
                    psUpsert.executeUpdate();
                }
                if (puestoVetId != null) {
                    psUpsert.setInt(1, puestoVetId);
                    psUpsert.setInt(2, empleadoId);
                    psUpsert.setInt(3, esVeterinario ? 1 : 0);
                    psUpsert.executeUpdate();
                }
            }
        }
    }
}
