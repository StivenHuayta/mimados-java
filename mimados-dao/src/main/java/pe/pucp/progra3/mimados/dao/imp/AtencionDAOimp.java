package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.AtencionDAO;
import pe.pucp.progra3.mimados.model.Atencion;
import pe.pucp.progra3.mimados.model.Cita;
import pe.pucp.progra3.mimados.model.Empleado;
import pe.pucp.progra3.mimados.model.Usuario;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

public class AtencionDAOimp implements AtencionDAO {

    @Override
    public void insertar_atencion(Atencion atencion) throws SQLException {
        /*
        CREATE PROCEDURE insertar_atencion(
            IN p_cita_id INT,
            IN p_empleado_id INT,
            IN p_peso_actual DECIMAL(5,2),
            IN p_temperature DECIMAL(4,1),
            IN p_sintomas VARCHAR(255),
            IN p_diagnostico VARCHAR(255),
            IN p_tratamiento_recetado VARCHAR(500),
            IN p_observaciones VARCHAR(255),
            IN p_monto_total DECIMAL(10,2)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_atencion(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            cs.setInt(1, atencion.getCita().getId());
            cs.setInt(2, atencion.getEmpleado().getUsuario().getId());


            if (atencion.getPesoActual() != null) {
                cs.setBigDecimal(3, BigDecimal.valueOf(atencion.getPesoActual()));
            } else {
                cs.setNull(3, Types.DECIMAL);
            }

            if (atencion.getTemperatura() != null) {
                cs.setBigDecimal(4, BigDecimal.valueOf(atencion.getTemperatura()));
            } else {
                cs.setNull(4, Types.DECIMAL);
            }

            cs.setString(5, atencion.getSintomas());
            cs.setString(6, atencion.getDiagnostico());
            cs.setString(7, atencion.getTratamientoRecetado());
            cs.setString(8, atencion.getObservaciones());
            cs.setBigDecimal(9, atencion.getMontoTotal());

            cs.execute();
        }
    }

    @Override
    public Atencion mostrar_atencion(int idAtencion) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_atencion(IN p_atencion_id INT)
        SELECT atencion_id, cita_id, empleado_id, fecha_hora_registro, peso_actual, temperatura,
               sintomas, diagnostico, tratamiento_recetado, observaciones, monto_total
        FROM atencion WHERE atencion_id = p_atencion_id;
        */
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_atencion(?)}")) {

            cs.setInt(1, idAtencion);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("cita_id"));

                    Empleado empleado = new Empleado();
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("empleado_id"));
                    empleado.setUsuario(usuario);

                    Timestamp ts = rs.getTimestamp("fecha_hora_registro");
                    LocalDateTime fechaRegistro = ts != null ? ts.toLocalDateTime() : null;

                    BigDecimal pesoBD = rs.getBigDecimal("peso_actual");
                    Double peso = pesoBD != null ? pesoBD.doubleValue() : null;

                    BigDecimal tempBD = rs.getBigDecimal("temperatura");
                    Double temperatura = tempBD != null ? tempBD.doubleValue() : null;

                    return new Atencion(
                            rs.getInt("atencion_id"),
                            cita,
                            empleado,
                            fechaRegistro,
                            peso,
                            temperatura,
                            rs.getString("sintomas"),
                            rs.getString("diagnostico"),
                            rs.getString("tratamiento_recetado"),
                            rs.getString("observaciones"),
                            rs.getBigDecimal("monto_total")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar_atencion(Atencion atencion) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_atencion(
            IN p_atencion_id INT,
            IN p_peso_actual DECIMAL(5,2),
            IN p_temperature DECIMAL(4,1),
            IN p_sintomas VARCHAR(255),
            IN p_diagnostico VARCHAR(255),
            IN p_tratamiento_recetado VARCHAR(500),
            IN p_observaciones VARCHAR(255),
            IN p_monto_total DECIMAL(10,2)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_atencion(?, ?, ?, ?, ?, ?, ?, ?)}")) {
            cs.setInt(1, atencion.getId());

            if (atencion.getPesoActual() != null) {
                cs.setBigDecimal(2, BigDecimal.valueOf(atencion.getPesoActual()));
            } else {
                cs.setNull(2, Types.DECIMAL);
            }

            if (atencion.getTemperatura() != null) {
                cs.setBigDecimal(3, BigDecimal.valueOf(atencion.getTemperatura()));
            } else {
                cs.setNull(3, Types.DECIMAL);
            }

            cs.setString(4, atencion.getSintomas());
            cs.setString(5, atencion.getDiagnostico());
            cs.setString(6, atencion.getTratamientoRecetado());
            cs.setString(7, atencion.getObservaciones());
            cs.setBigDecimal(8, atencion.getMontoTotal());

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la atención con ID " + atencion.getId());
            }
        }
    }
}
