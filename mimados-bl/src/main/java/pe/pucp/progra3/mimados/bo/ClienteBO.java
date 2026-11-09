package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteBO {

    void insertar(Cliente cliente)
            throws SQLException, NegocioException;

    void modificar(Cliente cliente)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<Cliente> listarTodos()
            throws SQLException;

    Cliente obtenerPorId(int id)
            throws SQLException, NegocioException;
}