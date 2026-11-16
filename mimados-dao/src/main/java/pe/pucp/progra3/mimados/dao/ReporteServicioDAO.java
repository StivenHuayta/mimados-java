package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReporteServicioDAO {
    List<ReporteServiciosSolicitadosDTO> obtenerServiciosMasSolicitados(Date fechaInicio, Date fechaFin)
            throws SQLException;
}
