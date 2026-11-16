package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.UsuarioDAO;
import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void insertarUsuario(Usuario usuario) throws SQLException {
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
    public List<Usuario> listarUsuarios() throws SQLException {
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
    public Usuario mostrarUsuario(int id) throws SQLException {
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
    public void actualizarTelefonoUsuario(int id, String telefono) throws SQLException {
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
    public Usuario buscarPorEmail(String email) throws SQLException {
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

    //PARA PERFIL DE USUARIO
    @Override
    public UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) throws SQLException {
        UsuarioPerfilDTO perfil = null;
        String sql = "{CALL SP_OBTENER_PERFIL_USUARIO(?)}";

        // Asume que usas una clase de conexión tipo DBManager
        try (Connection conn = DBManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    perfil = new UsuarioPerfilDTO();
                    perfil.setId(rs.getInt("id"));
                    perfil.setDni(rs.getString("dni"));
                    perfil.setEmail(rs.getString("email"));
                    perfil.setNombres(rs.getString("nombres"));
                    perfil.setApellidos(rs.getString("apellidos"));
                    perfil.setTelefono(rs.getString("telefono"));
                    perfil.setDireccion(rs.getString("direccion"));
                    perfil.setContactoEmergencia(rs.getString("contactoEmergencia"));
                }
            }
        }
        return perfil;
    }

    @Override
    public boolean actualizarPerfil(UsuarioPerfilDTO perfil) throws SQLException {
        boolean exito = false;
        String sql = "{CALL SP_ACTUALIZAR_PERFIL_USUARIO(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, perfil.getId());
            cs.setString(2, perfil.getNombres());
            cs.setString(3, perfil.getApellidos());
            cs.setString(4, perfil.getTelefono());
            cs.setString(5, perfil.getDireccion());
            cs.setString(6, perfil.getContactoEmergencia());

            // executeUpdate devuelve el número de filas afectadas.
            // Ojo: En un SP con múltiples UPDATEs, a veces devuelve 0 aunque haya sido exitoso.
            // Por seguridad, validamos que no lance excepción y asumimos éxito.
            cs.executeUpdate();
            exito = true;
        }
        return exito;
    }


}
