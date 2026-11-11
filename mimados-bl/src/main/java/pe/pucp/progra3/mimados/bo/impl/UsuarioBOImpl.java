package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.dao.UsuarioDAO;
import pe.pucp.progra3.mimados.dao.imp.ClienteDAOimp;
import pe.pucp.progra3.mimados.dao.imp.EmpleadoDAOimp;
import pe.pucp.progra3.mimados.dao.imp.UsuarioDAOimp;
import pe.pucp.progra3.mimados.model.dto.response.AuthResultado;
import pe.pucp.progra3.mimados.model.entity.Cliente;
import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.Usuario;

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

    @Override
    public AuthResultado autenticar(String email, String password) throws SQLException, NegocioException {
        if (email == null || email.isBlank()) {
            throw new NegocioException("El correo o usuario es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new NegocioException("La contraseña es obligatoria.");
        }

        Usuario usuario = usuarioDAO.buscar_por_email(email.trim());
        if (usuario == null) {
            throw new NegocioException("Usuario o contraseña incorrectos.");
        }

        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().equals(password)) {
            throw new NegocioException("Usuario o contraseña incorrectos.");
        }

        EmpleadoDAO empleadoDAO = new EmpleadoDAOimp();
        Empleado emp = empleadoDAO.mostrar_empleado(usuario.getId());
        if (emp != null) {
            // Regla 2: "el admin si es que hay un veterinario con ese correo, con atributo es admin 1 y es activo"
            if (emp.getEsAdmin() && emp.getActivo()) {
                AuthResultado auth = new AuthResultado(
                        usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                        usuario.getEmail(), "Admin", true, true
                );
                auth.setCodigoCmvp(emp.getCodigoCmvp());
                auth.setSedeId(emp.getSede() != null ? emp.getSede().getId() : null);
                return auth;
            }

            // Regla 3: "y veterinario si hay un veterinario activo con ese correo"
            if (emp.getActivo()) {
                AuthResultado auth = new AuthResultado(
                        usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                        usuario.getEmail(), "Veterinario", false, true
                );
                auth.setCodigoCmvp(emp.getCodigoCmvp());
                auth.setSedeId(emp.getSede() != null ? emp.getSede().getId() : null);
                return auth;
            }

            throw new NegocioException("El veterinario asociado a este correo se encuentra inactivo.");
        }

        ClienteDAO clienteDAO = new ClienteDAOimp();
        Cliente cli = clienteDAO.mostrar_cliente(usuario.getId());
        if (cli != null) {
            // Regla 1: "el cliente puede entrar si en la bd hay ese cliente activo"
            if (cli.getActivo()) {
                return new AuthResultado(
                        usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                        usuario.getEmail(), "Cliente", false, true
                );
            }
            throw new NegocioException("La cuenta de cliente asociada a este correo se encuentra inactiva.");
        }

        throw new NegocioException("El usuario no tiene un rol activo asignado.");
    }

    @Override
    public AuthResultado registrarCliente(String dni, String nombres, String apellidos,
                                           String email, String password, String telefono,
                                           String direccion, String contactoEmergencia)
            throws SQLException, NegocioException {

        if (dni == null || dni.trim().length() != 8) {
            throw new NegocioException("El DNI debe tener 8 dígitos.");
        }
        if (nombres == null || nombres.trim().isBlank()) {
            throw new NegocioException("Los nombres son obligatorios.");
        }
        if (apellidos == null || apellidos.trim().isBlank()) {
            throw new NegocioException("Los apellidos son obligatorios.");
        }
        if (email == null || email.trim().isBlank() || !email.contains("@")) {
            throw new NegocioException("El correo electrónico no es válido.");
        }
        if (password == null || password.trim().isBlank()) {
            throw new NegocioException("La contraseña es obligatoria.");
        }
        if (telefono == null || telefono.trim().length() != 9) {
            throw new NegocioException("El teléfono debe tener 9 dígitos.");
        }

        Usuario usuarioExistente = usuarioDAO.buscar_por_email(email.trim());
        if (usuarioExistente != null) {
            throw new NegocioException("Ya existe una cuenta registrada con este correo electrónico.");
        }

        Usuario usuario = new Usuario();
        usuario.setDni(dni.trim());
        usuario.setNombres(nombres.trim());
        usuario.setApellidos(apellidos.trim());
        usuario.setEmail(email.trim());
        usuario.setPasswordHash(password.trim());
        usuario.setTelefono(telefono.trim());

        usuarioDAO.insertar_usuario(usuario);

        if (usuario.getId() <= 0) {
            Usuario uCreado = usuarioDAO.buscar_por_email(email.trim());
            if (uCreado != null) {
                usuario.setId(uCreado.getId());
            }
        }

        ClienteDAO clienteDAO = new ClienteDAOimp();
        Cliente cliente = new Cliente(
                usuario,
                direccion != null ? direccion.trim() : "",
                contactoEmergencia != null ? contactoEmergencia.trim() : "",
                true
        );
        clienteDAO.insertar_cliente(cliente);

        return new AuthResultado(
                usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                usuario.getEmail(), "Cliente", false, true
        );
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

    //PARA PERFIL DE USUARIO

    @Override
    public UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) throws NegocioException, SQLException {
        if (idUsuario == null || idUsuario <= 0) {
            throw new NegocioException("El ID del usuario no es válido.");
        }

        UsuarioPerfilDTO perfil = usuarioDAO.obtenerPerfil(idUsuario);

        if (perfil == null) {
            throw new NegocioException("No se encontró el perfil del usuario solicitado.");
        }

        return perfil;
    }

    @Override
    public boolean actualizarPerfil(UsuarioPerfilDTO perfil) throws NegocioException, SQLException {
        if (perfil == null || perfil.getId() == null || perfil.getId() <= 0) {
            throw new NegocioException("Datos de perfil inválidos o ID faltante.");
        }


        if (perfil.getNombres() == null || perfil.getNombres().trim().isEmpty()) {
            throw new NegocioException("El nombre no puede estar vacío.");
        }

        return usuarioDAO.actualizarPerfil(perfil);
    }
}
