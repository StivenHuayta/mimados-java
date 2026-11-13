package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.CitaServicio;

import java.sql.SQLException;
import java.util.List;

public interface CitaServicioDAO {

    public void insertar_cita_servicio(CitaServicio citaServicio) throws SQLException;
    public List<CitaServicio> listar_servicios_por_cita(int idCita) throws SQLException;
    public void actualizar_cita_servicio(CitaServicio citaServicio) throws SQLException;

    //public void eliminar_cita_servicio(int cita_id , int sede_servicio_id) throws SQLException;

}
