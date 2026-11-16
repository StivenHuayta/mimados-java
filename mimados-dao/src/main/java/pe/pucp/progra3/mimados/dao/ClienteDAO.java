package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {

    public void insertarCliente(Cliente cliente) throws SQLException;
    public List<Cliente> listarClientes() throws SQLException;
    public Cliente mostrarCliente(int id) throws SQLException;
    public void actualizarTelefonoCliente(int id, String contactoEmergencia) throws SQLException;
    public void eliminarCliente(int id) throws SQLException;

}
