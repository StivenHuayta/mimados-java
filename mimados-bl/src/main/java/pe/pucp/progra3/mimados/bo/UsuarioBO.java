package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.dto.response.AuthResultado;
import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.util.List;

public interface UsuarioBO {

    void insertar(Usuario usuario);

    void modificar(Usuario usuario);

    List<Usuario> listarTodos();

    Usuario obtenerPorId(int id);

    AuthResultado autenticar(String email, String password);

    AuthResultado registrarCliente(String dni, String nombres, String apellidos,
                                   String email, String password, String telefono,
                                   String direccion, String contactoEmergencia);


    //PARA PERFIL
    UsuarioPerfilDTO obtenerPerfil(Integer idUsuario);

    boolean actualizarPerfil(UsuarioPerfilDTO perfil);

}
