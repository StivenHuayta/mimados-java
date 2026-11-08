package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.UsuarioDAO;
import pe.pucp.progra3.mimados.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOimp implements UsuarioDAO {

    @Override
    public void insertar_usuario(Usuario usuario) throws SQLException {
        /*
        CREATE PROCEDURE insertar_usuario(
            IN p_dni VARCHAR(8),
            IN p_nombres VARCHAR(45),
            IN p_apellidos VARCHAR(45),
            IN p_email VARCHAR(100),
            IN p_password_hash VARCHAR(225),
            IN p_telefono VARCHAR(9),
            OUT p_usuario_id INT
        )
        */
        Connection connection = DBManager.getInstance().getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_usuario(?, ?, ?, ?, ?, ?, ?)}")) {
            cs.registerOutParameter(7, Types.INTEGER);

            cs.setString(1, usuario.getDni());
            cs.setString(2, usuario.getNombres());
            cs.setString(3, usuario.getApellidos());
            cs.setString(4, usuario.getEmail());
            cs.setString(5, usuario.getPasswordHash());
            cs.setString(6, usuario.getTelefono());

            cs.execute();

            int idGenerado = cs.getInt(7);
            usuario.setId(idGenerado);
        } catch (SQLException ex) {
            String sql = "INSERT INTO usuario (dni, nombres, apellidos, email, password_hash, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getDni());
                ps.setString(2, usuario.getNombres());
                ps.setString(3, usuario.getApellidos());
                ps.setString(4, usuario.getEmail());
                ps.setString(5, usuario.getPasswordHash());
                ps.setString(6, usuario.getTelefono());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public List<Usuario> listar_usuarios() throws SQLException {
        /*
        CREATE PROCEDURE listar_usuarios()
        select usuario_id, dni, nombres, apellidos, email, telefono from usuario;
        */
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_usuarios()}");
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
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    @Override
    public Usuario mostrar_usuario(int id) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_usuario(IN id int)
        select dni, nombres, apellidos, email, telefono from usuario where usuario_id=id;
        */
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_usuario(?)}")) {

            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            id,
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar_telefono_usuario(int id, String telefono) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_telefono_usuario(
            IN p_usuario_id INT,
            IN p_telefono VARCHAR(9)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_telefono_usuario(?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, telefono);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el usuario con ID " + id);
            }
        }
    }

    @Override
    public Usuario buscar_por_email(String email) throws SQLException {
        String sql = "SELECT usuario_id, dni, nombres, apellidos, email, password_hash, telefono FROM usuario WHERE email = ?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    return usuario;
                }
            }
        }
        return null;
    }
}
