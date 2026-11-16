package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.CitaServicio;

import java.util.List;

public interface CitaServicioBO {

    void insertar(CitaServicio citaServicio);

    void modificar(CitaServicio citaServicio);

    List<CitaServicio> listarPorCita(int idCita);
}