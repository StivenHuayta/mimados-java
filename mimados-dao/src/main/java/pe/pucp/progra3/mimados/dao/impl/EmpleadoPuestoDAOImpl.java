package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.EmpleadoPuestoDAO;
import pe.pucp.progra3.mimados.model.entity.EmpleadoPuesto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
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
}
