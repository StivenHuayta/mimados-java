package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {

    public void insertarUsuario(Usuario usuario) throws SQLException;
    public List<Usuario> listarUsuarios() throws SQLException;
    public Usuario mostrarUsuario(int id) throws SQLException;
    public void actualizarTelefonoUsuario(int id, String telefono) throws SQLException;
    public Usuario buscarPorEmail(String email) throws SQLException;


    UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) throws SQLException;
    boolean actualizarPerfil(UsuarioPerfilDTO perfil) throws SQLException;
}
