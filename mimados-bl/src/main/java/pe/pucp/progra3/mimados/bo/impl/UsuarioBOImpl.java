package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.UsuarioDAO;
import pe.pucp.progra3.mimados.dao.imp.UsuarioDAOimp;
import pe.pucp.progra3.mimados.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioBOImpl implements UsuarioBO {

    private final UsuarioDAO usuarioDAO;

    public UsuarioBOImpl() {
        this.usuarioDAO = new UsuarioDAOimp();
    }

    @Override
    public void insertar(Usuario usuario)
            throws SQLException, NegocioException {

        validarUsuario(usuario);

        try {
            usuarioDAO.insertar_usuario(usuario);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Usuario usuario)
            throws SQLException, NegocioException {

        if (usuario == null) {
            throw new NegocioException(
                    "El usuario no puede ser nulo."
            );
        }

        validarId(usuario.getId());
        validarTelefono(usuario.getTelefono());

        try {
            usuarioDAO.actualizar_telefono_usuario(
                    usuario.getId(),
                    usuario.getTelefono()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Usuario> listarTodos()
            throws SQLException {

        return usuarioDAO.listar_usuarios();
    }

    @Override
    public Usuario obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return usuarioDAO.mostrar_usuario(id);
    }

    private void validarUsuario(Usuario usuario)
            throws NegocioException {

        if (usuario == null) {
            throw new NegocioException(
                    "El usuario no puede ser nulo."
            );
        }

        if (usuario.getDni() == null ||
                usuario.getDni().length() != 8) {

            throw new NegocioException(
                    "El DNI debe tener 8 caracteres."
            );
        }

        if (usuario.getNombres() == null ||
                usuario.getNombres().isBlank()) {

            throw new NegocioException(
                    "Los nombres son obligatorios."
            );
        }

        if (usuario.getNombres().length() > 45) {
            throw new NegocioException(
                    "Los nombres no pueden superar los 45 caracteres."
            );
        }

        if (usuario.getApellidos() == null ||
                usuario.getApellidos().isBlank()) {

            throw new NegocioException(
                    "Los apellidos son obligatorios."
            );
        }

        if (usuario.getApellidos().length() > 45) {
            throw new NegocioException(
                    "Los apellidos no pueden superar los 45 caracteres."
            );
        }

        if (usuario.getEmail() == null ||
                usuario.getEmail().isBlank() ||
                !usuario.getEmail().contains("@")) {

            throw new NegocioException(
                    "El correo electrónico no es válido."
            );
        }

        if (usuario.getEmail().length() > 100) {
            throw new NegocioException(
                    "El correo electrónico no puede superar los 100 caracteres."
            );
        }

        if (usuario.getPasswordHash() == null ||
                usuario.getPasswordHash().isBlank()) {

            throw new NegocioException(
                    "La contraseña es obligatoria."
            );
        }

        if (usuario.getPasswordHash().length() > 225) {
            throw new NegocioException(
                    "La contraseña no puede superar los 225 caracteres."
            );
        }

        validarTelefono(usuario.getTelefono());
    }

    private void validarTelefono(String telefono)
            throws NegocioException {

        if (telefono == null ||
                telefono.isBlank()) {

            throw new NegocioException(
                    "El teléfono es obligatorio."
            );
        }

        if (telefono.length() > 9) {
            throw new NegocioException(
                    "El teléfono no puede superar los 9 caracteres."
            );
        }
    }

    private void validarId(int id)
            throws NegocioException {

        if (id <= 0) {
            throw new NegocioException(
                    "El ID del usuario debe ser mayor que cero."
            );
        }
    }
}