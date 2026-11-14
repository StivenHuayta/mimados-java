package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Puesto;

import java.util.List;

public interface PuestoBO {

    void insertar(Puesto puesto);

    void modificar(Puesto puesto);

    List<Puesto> listarTodos();

    Puesto obtenerPorId(int id);
}