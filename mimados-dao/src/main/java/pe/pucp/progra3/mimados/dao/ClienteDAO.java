package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {

    public void insertar_cliente(Cliente cliente) throws SQLException;
    public List<Cliente> listar_clientes() throws SQLException;
    public Cliente mostrar_cliente(int id) throws SQLException;
    public void actualizar_telefono_cliente(int id, String contactoEmergencia) throws SQLException;
    public void eliminar_cliente(int id) throws SQLException;

}
