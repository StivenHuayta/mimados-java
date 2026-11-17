package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Sede;

import java.util.List;

public interface SedeBO {

    void insertar(Sede sede);

    void modificar(Sede sede);

    void eliminar(int id);

    List<Sede> listarTodos(String nombre,String direccion);
    //List<Sede> listarTodosParametros();
    //ahora con parametros

    Sede obtenerPorId(int id);
}