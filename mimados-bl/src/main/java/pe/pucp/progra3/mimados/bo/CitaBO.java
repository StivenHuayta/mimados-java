package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.Cita;

import java.util.List;

public interface CitaBO {

    void insertar(Cita cita);

    List<Cita> listarPorMascota(int idMascota);

    Cita obtenerPorId(int id);

    void cancelar(int idCita);

    void atender(int idCita);

    void modificar(Cita cita);
}