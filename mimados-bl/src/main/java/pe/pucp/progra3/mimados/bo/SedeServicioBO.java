package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.SedeServicio;
import pe.pucp.progra3.mimados.model.Servicio;

import java.sql.SQLException;
import java.util.List;

public interface SedeServicioBO {

    void insertar(SedeServicio sedeServicio)
            throws SQLException, NegocioException;

    void eliminar(int idSedeServicio)
            throws SQLException, NegocioException;

    List<Servicio> listarPorSede(int idSede)
            throws SQLException, NegocioException;
}