package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Atencion;

public interface AtencionBO {

    void insertar(Atencion atencion);

    void modificar(Atencion atencion);

    Atencion obtenerPorId(int id);
}