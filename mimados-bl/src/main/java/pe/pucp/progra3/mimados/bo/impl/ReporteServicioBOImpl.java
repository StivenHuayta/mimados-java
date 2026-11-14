package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.bo.ReporteServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ReporteServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.ReporteServicioDAOimp;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReporteServicioBOImpl implements ReporteServicioBO {
    private ReporteServicioDAO reporteServicioBO;

    public ReporteServicioBOImpl() {
        reporteServicioBO = new ReporteServicioDAOimp();
    }

    @Override
    public List<ReporteServiciosSolicitadosDTO> listarServiciosMasSolicitados(Date fechaInicio, Date fechaFin)
            throws SQLException {

        return reporteServicioBO.obetenerServiciosMasSolicitados(fechaInicio, fechaFin);
    }


}
