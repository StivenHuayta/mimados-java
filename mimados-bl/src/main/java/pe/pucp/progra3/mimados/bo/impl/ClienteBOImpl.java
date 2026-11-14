package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.ClienteBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.dao.imp.ClienteDAOimp;
import pe.pucp.progra3.mimados.model.entity.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteBOImpl implements ClienteBO {

    private final ClienteDAO clienteDAO;

    public ClienteBOImpl() {
        this.clienteDAO = new ClienteDAOimp();
    }

    @Override
    public void insertar(Cliente cliente) {

        validarCliente(cliente);

        try {
            clienteDAO.insertar_cliente(cliente);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el cliente.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Cliente cliente) {

        if (cliente == null) {
            throw new NegocioException(
                    "El cliente no puede ser nulo."
            );
        }

        if (cliente.getUsuario() == null) {
            throw new NegocioException(
                    "El cliente debe tener un usuario."
            );
        }

        validarId(cliente.getUsuario().getId());
        validarTelefono(cliente.getTelefonoEmergencia());

        try {
            clienteDAO.actualizar_telefono_cliente(
                    cliente.getUsuario().getId(),
                    cliente.getTelefonoEmergencia()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el cliente.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id) {

        validarId(id);

        try {
            clienteDAO.eliminar_cliente(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar el cliente.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Cliente> listarTodos() {

        try {
            return clienteDAO.listar_clientes();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los clientes.", e);
        }
    }

    @Override
    public Cliente obtenerPorId(int id) {

        validarId(id);

        try {
            return clienteDAO.mostrar_cliente(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el cliente.", e);
        }
    }

    private void validarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new NegocioException(
                    "El cliente no puede ser nulo."
            );
        }

        if (cliente.getUsuario() == null) {
            throw new NegocioException(
                    "El cliente debe tener un usuario."
            );
        }

        validarId(cliente.getUsuario().getId());

        if (cliente.getDireccion() != null &&
                cliente.getDireccion().length() > 60) {

            throw new NegocioException(
                    "La dirección no puede superar los 60 caracteres."
            );
        }

        validarTelefono(cliente.getTelefonoEmergencia());
    }

    private void validarTelefono(String telefono) {

        if (telefono != null &&
                telefono.length() > 9) {

            throw new NegocioException(
                    "El teléfono de emergencia no puede superar los 9 caracteres."
            );
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del cliente debe ser mayor que cero."
            );
        }
    }
}