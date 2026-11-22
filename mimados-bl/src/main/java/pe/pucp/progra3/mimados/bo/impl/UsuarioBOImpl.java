package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.dao.EmpleadoPuestoDAO;
import pe.pucp.progra3.mimados.dao.UsuarioDAO;
import pe.pucp.progra3.mimados.dao.impl.ClienteDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.EmpleadoDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.EmpleadoPuestoDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.UsuarioDAOImpl;
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
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @Override
    public void insertar(Usuario usuario) {

        validarUsuario(usuario);

        try {
            usuarioDAO.insertarUsuario(usuario);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el usuario.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Usuario usuario) {

        if (usuario == null) {
            throw new NegocioException(
                    "El usuario no puede ser nulo."
            );
        }

        validarId(usuario.getId());
        validarTelefono(usuario.getTelefono());

        try {
            usuarioDAO.actualizarTelefonoUsuario(
                    usuario.getId(),
                    usuario.getTelefono()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el usuario.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Usuario> listarTodos() {

        try {
            return usuarioDAO.listarUsuarios();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los usuarios.", e);
        }
    }

    @Override
    public Usuario obtenerPorId(int id) {

        validarId(id);

        try {
            return usuarioDAO.mostrarUsuario(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el usuario.", e);
        }
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new NegocioException("El email no puede estar vacío.");
        }
        try {
            return usuarioDAO.buscarPorEmail(email.trim());
        } catch (SQLException e) {
            throw new NegocioException("Error al buscar usuario por email.", e);
        }
    }

    @Override
    public Usuario buscarPorDni(String dni) {
        if (dni == null || dni.isBlank()) {
            throw new NegocioException("El DNI no puede estar vacío.");
        }
        try {
            return usuarioDAO.buscarPorDni(dni.trim());
        } catch (SQLException e) {
            throw new NegocioException("Error al buscar usuario por DNI.", e);
        }
    }

    @Override
    public AuthResultado autenticar(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new NegocioException("El correo o usuario es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new NegocioException("La contraseña es obligatoria.");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email.trim());
            if (usuario == null) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().equals(password)) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
            Empleado emp = empleadoDAO.mostrarEmpleado(usuario.getId());
            boolean esEmpActivo = emp != null && Boolean.TRUE.equals(emp.getActivo());
            boolean esAdmin = esEmpActivo && Boolean.TRUE.equals(emp.getEsAdmin());
            boolean esVeterinario = false;
            if (esEmpActivo) {
                EmpleadoPuestoDAO empleadoPuestoDAO = new EmpleadoPuestoDAOImpl();
                boolean tienePuestoVet = empleadoPuestoDAO.tienePuestoVeterinarioActivo(usuario.getId());
                if (esAdmin) {
                    esVeterinario = tienePuestoVet;
                } else {
                    esVeterinario = tienePuestoVet || (emp.getCodigoCmvp() != null && !emp.getCodigoCmvp().isBlank());
                }
            }

            ClienteDAO clienteDAO = new ClienteDAOImpl();
            Cliente cli = clienteDAO.mostrarCliente(usuario.getId());
            boolean esCliente = cli != null && Boolean.TRUE.equals(cli.getActivo());

            if (!esAdmin && !esVeterinario && !esCliente) {
                if (emp != null && !Boolean.TRUE.equals(emp.getActivo()) && cli == null) {
                    throw new NegocioException("El veterinario asociado a este correo se encuentra inactivo.");
                }
                if (cli != null && !Boolean.TRUE.equals(cli.getActivo()) && emp == null) {
                    throw new NegocioException("La cuenta de cliente asociada a este correo se encuentra inactiva.");
                }
                throw new NegocioException("El usuario no tiene un rol activo asignado.");
            }

            String rolPrincipal = esAdmin ? "Admin" : (esVeterinario ? "Veterinario" : "Cliente");
            AuthResultado auth = new AuthResultado(
                    usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                    usuario.getEmail(), rolPrincipal, esAdmin, true
            );
            auth.setEsVeterinario(esVeterinario);
            auth.setEsCliente(esCliente);
            if (esEmpActivo) {
                auth.setCodigoCmvp(emp.getCodigoCmvp());
                auth.setSedeId(emp.getSede() != null ? emp.getSede().getId() : null);
            }
            return auth;
        } catch (SQLException e) {
            throw new NegocioException("Error al autenticar al usuario.", e);
        }
    }

    @Override
    public AuthResultado registrarCliente(String dni, String nombres, String apellidos,
                                          String email, String password, String telefono,
                                          String direccion, String contactoEmergencia) {

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

        try {
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email.trim());

            if (usuarioExistente == null) {
                Usuario porDni = usuarioDAO.buscarPorDni(dni.trim());
                if (porDni != null) {
                    throw new NegocioException("Ya existe una cuenta registrada con este DNI y otro correo electrónico.");
                }
            }

            Usuario usuario;
            if (usuarioExistente != null) {
                Cliente clienteExistente = clienteDAO.mostrarCliente(usuarioExistente.getId());
                if (clienteExistente != null) {
                    throw new NegocioException("Ya existe una cuenta de cliente registrada con este correo electrónico.");
                }
                if (!dni.trim().equals(usuarioExistente.getDni())) {
                    throw new NegocioException("El DNI no coincide con la cuenta existente para este correo.");
                }
                if (usuarioExistente.getPasswordHash() != null
                        && !usuarioExistente.getPasswordHash().equals(password.trim())) {
                    throw new NegocioException("Ya existe una cuenta con este correo; ingrese su contraseña actual para activar su perfil de cliente.");
                }
                usuario = usuarioExistente;
            } else {
                usuario = new Usuario();
                usuario.setDni(dni.trim());
                usuario.setNombres(nombres.trim());
                usuario.setApellidos(apellidos.trim());
                usuario.setEmail(email.trim());
                usuario.setPasswordHash(password.trim());
                usuario.setTelefono(telefono.trim());

                usuarioDAO.insertarUsuario(usuario);

                if (usuario.getId() <= 0) {
                    Usuario uCreado = usuarioDAO.buscarPorEmail(email.trim());
                    if (uCreado != null) {
                        usuario.setId(uCreado.getId());
                    }
                }
            }

            Cliente cliente = new Cliente(
                    usuario,
                    direccion != null ? direccion.trim() : "",
                    contactoEmergencia != null ? contactoEmergencia.trim() : "",
                    true
            );
            clienteDAO.insertarCliente(cliente);

            return new AuthResultado(
                    usuario.getId(), usuario.getDni(), usuario.getNombres(), usuario.getApellidos(),
                    usuario.getEmail(), "Cliente", false, true
            );
        } catch (SQLException e) {
            throw new NegocioException("Error al registrar el cliente.", e);
        }
    }

    @Override
    public boolean esClienteActivo(int idUsuario) {
        validarId(idUsuario);
        try {
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            Cliente cli = clienteDAO.mostrarCliente(idUsuario);
            return cli != null && Boolean.TRUE.equals(cli.getActivo());
        } catch (SQLException e) {
            throw new NegocioException("Error al verificar el perfil de cliente.", e);
        }
    }

    @Override
    public void activarPerfilCliente(int idUsuario, String direccion, String contactoEmergencia) {
        validarId(idUsuario);
        try {
            Usuario usuario = usuarioDAO.mostrarUsuario(idUsuario);
            if (usuario == null) {
                throw new NegocioException("Usuario no encontrado.");
            }
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            Cliente existente = clienteDAO.mostrarCliente(idUsuario);
            if (existente != null) {
                if (Boolean.TRUE.equals(existente.getActivo())) {
                    throw new NegocioException("Ya tienes un perfil de cliente activo.");
                }
                throw new NegocioException("Tu perfil de cliente ya existe pero se encuentra inactivo.");
            }
            Cliente cliente = new Cliente(
                    usuario,
                    direccion != null ? direccion.trim() : "",
                    contactoEmergencia != null ? contactoEmergencia.trim() : "",
                    true
            );
            clienteDAO.insertarCliente(cliente);
        } catch (SQLException e) {
            throw new NegocioException("Error al activar el perfil de cliente.", e);
        }
    }

    private void validarUsuario(Usuario usuario) {

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

    private void validarTelefono(String telefono) {

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

    private void validarId(int id) {

        if (id <= 0) {
            throw new NegocioException(
                    "El ID del usuario debe ser mayor que cero."
            );
        }
    }

    //PARA PERFIL DE USUARIO

    @Override
    public UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            throw new NegocioException("El ID del usuario no es válido.");
        }

        UsuarioPerfilDTO perfil;
        try {
            perfil = usuarioDAO.obtenerPerfil(idUsuario);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el perfil del usuario.", e);
        }

        if (perfil == null) {
            throw new NegocioException("No se encontró el perfil del usuario solicitado.");
        }

        return perfil;
    }

    @Override
    public boolean actualizarPerfil(UsuarioPerfilDTO perfil) {
        if (perfil == null || perfil.getId() == null || perfil.getId() <= 0) {
            throw new NegocioException("Datos de perfil inválidos o ID faltante.");
        }


        if (perfil.getNombres() == null || perfil.getNombres().trim().isEmpty()) {
            throw new NegocioException("El nombre no puede estar vacío.");
        }

        try {
            return usuarioDAO.actualizarPerfil(perfil);
        } catch (SQLException e) {
            throw new NegocioException("Error al actualizar el perfil del usuario.", e);
        }
    }
}
