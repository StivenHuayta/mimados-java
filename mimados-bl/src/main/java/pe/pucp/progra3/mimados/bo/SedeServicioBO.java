package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.sql.SQLException;
import java.util.List;

public interface SedeServicioBO {

    void insertar(SedeServicio sedeServicio)
            throws SQLException, NegocioException;

    void eliminar(int idSedeServicio)
            throws SQLException, NegocioException;

    List<SedeServicioDTO> listarPorSede(int idSede)
            throws SQLException, NegocioException;

    List<Servicio> listar_servicios_disponibles(int idSede)
            throws SQLException, NegocioException;
}