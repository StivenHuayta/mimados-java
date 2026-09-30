package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.CitaServicio;

import java.sql.SQLException;
import java.util.List;

public interface CitaServicioBO {

    void insertar(CitaServicio citaServicio)
            throws SQLException, NegocioException;

    void modificar(CitaServicio citaServicio)
            throws SQLException, NegocioException;

    List<CitaServicio> listarPorCita(int idCita)
            throws SQLException, NegocioException;
}