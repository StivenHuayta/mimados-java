package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.model.entity.Cliente;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void insertarCliente(Cliente cliente) throws SQLException {
        /*
        CREATE PROCEDURE insertar_cliente(
            IN p_usuario_id INT,
            IN p_direccion VARCHAR(60),
            IN p_contacto_emergencia VARCHAR(9)
        )
        */
        Connection connection = DBManager.getInstance().getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_cliente(?, ?, ?)}")) {
            cs.setInt(1, cliente.getUsuario().getId());
            cs.setString(2, cliente.getDireccion());
            cs.setString(3, cliente.getTelefonoEmergencia());

            cs.execute();
        } catch (SQLException ex) {
            String sql = "INSERT INTO cliente (usuario_id, direccion, telefono_emergencia, activo) VALUES (?, ?, ?, 1)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, cliente.getUsuario().getId());
                ps.setString(2, cliente.getDireccion());
                ps.setString(3, cliente.getTelefonoEmergencia());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_clientes()}");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("dni"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono")
                );

                Cliente cliente = new Cliente(
                        usuario,
                        rs.getString("direccion"),
                        rs.getString("telefono_emergencia"),
                        rs.getBoolean("activo")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public Cliente mostrarCliente(int id) throws SQLException {
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_cliente(?)}")) {

            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            id,
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );

                    return new Cliente(
                            usuario,
                            rs.getString("direccion"),
                            rs.getString("telefono_emergencia"),
                            rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException ex) {
            // Fallback con consulta directa si no existe el procedimiento
            String sql = "SELECT b.dni, b.nombres, b.apellidos, b.email, b.telefono, a.direccion, a.telefono_emergencia, a.activo " +
                         "FROM cliente a INNER JOIN usuario b ON b.usuario_id = a.usuario_id WHERE a.usuario_id = ?";
            try (Connection connection = DBManager.getInstance().getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario usuario = new Usuario(
                                id,
                                rs.getString("dni"),
                                rs.getString("nombres"),
                                rs.getString("apellidos"),
                                rs.getString("email"),
                                rs.getString("telefono")
                        );

                        return new Cliente(
                                usuario,
                                rs.getString("direccion"),
                                rs.getString("telefono_emergencia"),
                                rs.getBoolean("activo")
                        );
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void actualizarTelefonoCliente(int id, String contactoEmergencia) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_telefono_cliente(?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, contactoEmergencia);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el cliente con ID " + id);
            }
        }
    }

    @Override
    public void eliminarCliente(int id) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL eliminar_cliente(?)}")) {
            cs.setInt(1, id);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el cliente con ID " + id);
            }
        }
    }
}
