package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.PuestoServicio;

import java.util.List;

public interface PuestoServicioBO {

    void insertar(PuestoServicio puestoServicio);

    void eliminar(int idPuestoServicio);

    List<PuestoServicio> listarPorPuesto(int idPuesto);

    PuestoServicio obtenerPorId(int id);
}