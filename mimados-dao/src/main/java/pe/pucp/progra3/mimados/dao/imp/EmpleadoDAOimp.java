package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOimp implements EmpleadoDAO {

    @Override
    public List<Empleado> listar_empleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_empleados()}");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getString("dni"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getInt("sede_id"),
                        rs.getString("codigo_cmvp"),
                        rs.getBoolean("activo"),
                        rs.getBoolean("es_admin")
                );
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    @Override
    public void insertar_empleado(Empleado empleado) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_empleado(?,?,?)}")) {
            cs.setInt(1, empleado.getSede().getId());
            cs.setInt(2, empleado.getUsuario().getId());
            cs.setString(3, empleado.getCodigoCmvp());

            cs.execute();

        }
    }

    @Override
    public void eliminar_empleado(int id_empleado) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL eliminar_empleado(?)}")) {
            cs.setInt(1, id_empleado);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el empleado con ID " + id_empleado);
            }
        }
    }

    @Override
    public Empleado mostrar_empleado(int id_empleado) throws SQLException {
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_empleado(?)}")) {

            cs.setInt(1, id_empleado);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado(
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getInt("sede_id"),
                            rs.getString("codigo_cmvp"),
                            rs.getBoolean("activo"),
                            rs.getBoolean("es_admin")
                    );

                    emp.getUsuario().setId(id_empleado);
                    return emp;
                }
            }
        } catch (SQLException ex) {
            String sql = "SELECT b.dni, b.nombres, b.apellidos, b.email, b.telefono, a.sede_id, a.codigo_cmvp, a.activo, a.es_admin " +
                    "FROM empleado a INNER JOIN usuario b ON b.usuario_id = a.usuario_id WHERE a.usuario_id = ?";
            try (Connection connection = DBManager.getInstance().getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id_empleado);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Empleado emp = new Empleado(
                                rs.getString("dni"),
                                rs.getString("nombres"),
                                rs.getString("apellidos"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getInt("sede_id"),
                                rs.getString("codigo_cmvp"),
                                rs.getBoolean("activo"),
                                rs.getBoolean("es_admin")
                        );
                        emp.getUsuario().setId(id_empleado);
                        return emp;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar_empleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleado SET sede_id = ?, codigo_cmvp = ?, es_admin = ?, activo = ? " +
                     "WHERE usuario_id = ?";

        Connection conexion = TransactionContext.getConnection();
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, empleado.getSede().getId());
            pstmt.setString(2, empleado.getCodigoCmvp());
            pstmt.setBoolean(3, empleado.getEsAdmin());
            pstmt.setBoolean(4, empleado.getActivo());
            pstmt.setInt(5, empleado.getUsuario().getId());

            pstmt.executeUpdate();
        }
    }
}
