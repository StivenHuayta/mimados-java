package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.ClienteBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ClienteDAO;
import pe.pucp.progra3.mimados.dao.imp.ClienteDAOimp;
import pe.pucp.progra3.mimados.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteBOImpl implements ClienteBO {

    private final ClienteDAO clienteDAO;

    public ClienteBOImpl() {
        this.clienteDAO = new ClienteDAOimp();
    }

    @Override
    public void insertar(Cliente cliente)
            throws SQLException, NegocioException {

        validarCliente(cliente);

        try {
            clienteDAO.insertar_cliente(cliente);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Cliente cliente)
            throws SQLException, NegocioException {

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
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id)
            throws SQLException, NegocioException {

        validarId(id);

        try {
            clienteDAO.eliminar_cliente(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Cliente> listarTodos()
            throws SQLException {

        return clienteDAO.listar_clientes();
    }

    @Override
    public Cliente obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return clienteDAO.mostrar_cliente(id);
    }

    private void validarCliente(Cliente cliente)
            throws NegocioException {

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

    private void validarTelefono(String telefono)
            throws NegocioException {

        if (telefono != null &&
                telefono.length() > 9) {

            throw new NegocioException(
                    "El teléfono de emergencia no puede superar los 9 caracteres."
            );
        }
    }

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del cliente debe ser mayor que cero."
            );
        }
    }
}