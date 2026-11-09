package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.PuestoServicio;

import java.sql.SQLException;
import java.util.List;

public interface PuestoServicioBO {

    void insertar(PuestoServicio puestoServicio)
            throws SQLException, NegocioException;

    void eliminar(int idPuestoServicio)
            throws SQLException, NegocioException;

    List<PuestoServicio> listarPorPuesto(int idPuesto)
            throws SQLException, NegocioException;
    PuestoServicio obtenerPorId(int id)
            throws SQLException, NegocioException;
}