package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioBO {

    void insertar(Usuario usuario)
            throws SQLException, NegocioException;

    void modificar(Usuario usuario)
            throws SQLException, NegocioException;

    List<Usuario> listarTodos()
            throws SQLException;

    Usuario obtenerPorId(int id)
            throws SQLException, NegocioException;
}