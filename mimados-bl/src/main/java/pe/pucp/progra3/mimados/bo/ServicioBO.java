package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.util.List;

public interface ServicioBO {

    void insertar(Servicio servicio);

    void modificar(Servicio servicio);

    void eliminar(int id);

    List<Servicio> listarTodos(String nombre,Double preMin,Double preMax,Integer minMin,Integer minMax);

    Servicio obtenerPorId(int id);
}