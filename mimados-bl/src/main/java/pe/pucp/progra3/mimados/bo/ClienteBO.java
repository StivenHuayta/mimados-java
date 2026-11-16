package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Cliente;

import java.util.List;

public interface ClienteBO {

    void insertar(Cliente cliente);

    void modificar(Cliente cliente);

    void eliminar(int id);

    List<Cliente> listarTodos();

    Cliente obtenerPorId(int id);
}