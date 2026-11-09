package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Cita;

import java.sql.SQLException;
import java.util.List;

public interface CitaBO {

    void insertar(Cita cita)
            throws SQLException, NegocioException;

    List<Cita> listarPorMascota(int idMascota)
            throws SQLException, NegocioException;

    Cita obtenerPorId(int id)
            throws SQLException, NegocioException;

    void cancelar(int idCita)
            throws SQLException, NegocioException;

    void atender(int idCita)
            throws SQLException, NegocioException;
}