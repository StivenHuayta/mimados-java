package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReporteServicioBO {
    List<ReporteServiciosSolicitadosDTO> listarServiciosMasSolicitados(Date fechaInicio, Date fechaFin);
}
