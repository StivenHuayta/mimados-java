package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.model.Cliente;
import pe.pucp.progra3.mimados.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOimp implements ClienteDAO {

    @Override
    public void insertar_cliente(Cliente cliente) throws SQLException {
        /*
        CREATE PROCEDURE insertar_cliente(
            IN p_usuario_id INT,
            IN p_direccion VARCHAR(60),
            IN p_contacto_emergencia VARCHAR(9)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_cliente(?, ?, ?)}")) {
            cs.setInt(1, cliente.getUsuario().getId());
            cs.setString(2, cliente.getDireccion());
            cs.setString(3, cliente.getTelefonoEmergencia());

            cs.execute();
        }
    }

    @Override
    public List<Cliente> listar_clientes() throws SQLException {
        /*
        CREATE PROCEDURE listar_clientes()
        SELECT a.usuario_id, b.dni, b.nombres, b.apellidos, b.email, b.telefono, a.direccion, a.telefono_emergencia, a.activo
        FROM cliente a, usuario b WHERE b.usuario_id = a.usuario_id;
        */
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
    public Cliente mostrar_cliente(int id) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_cliente(IN id INT)
        SELECT b.dni, b.nombres, b.apellidos, b.email, b.telefono, a.direccion, a.telefono_emergencia, a.activo
        FROM cliente a, usuario b WHERE id = b.usuario_id AND b.usuario_id = a.usuario_id;
        */
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
        }
        return null;
    }

    @Override
    public void actualizar_telefono_cliente(int id, String contactoEmergencia) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_telefono_cliente(
            IN p_cliente_id INT,
            IN p_contacto_emergencia VARCHAR(9)
        )
        */
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
    public void eliminar_cliente(int id) throws SQLException {
        /*
        CREATE PROCEDURE eliminar_cliente(
            IN p_cliente_id INT
        )
        */
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
