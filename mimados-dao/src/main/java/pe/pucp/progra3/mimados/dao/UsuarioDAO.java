package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {

    public void insertar_usuario(Usuario usuario) throws SQLException;
    public List<Usuario> listar_usuarios() throws SQLException;
    public Usuario mostrar_usuario(int id) throws SQLException;
    public void actualizar_telefono_usuario(int id, String telefono) throws SQLException;
    public Usuario buscar_por_email(String email) throws SQLException;


}
