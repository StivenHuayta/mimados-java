package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.dto.response.AuthResultado;
import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.model.entity.Usuario;

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

    AuthResultado autenticar(String email, String password)
            throws SQLException, NegocioException;

    AuthResultado registrarCliente(String dni, String nombres, String apellidos,
                                   String email, String password, String telefono,
                                   String direccion, String contactoEmergencia)
            throws SQLException, NegocioException;


    //PARA PERFIL
    UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) throws NegocioException, SQLException;

    boolean actualizarPerfil(UsuarioPerfilDTO perfil) throws NegocioException, SQLException;

}
