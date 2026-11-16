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
    private final ReporteServicioDAO reporteServicioDAO;

    public ReporteServicioBOImpl() {
        reporteServicioDAO = new ReporteServicioDAOimp();
    }

    @Override
    public List<ReporteServiciosSolicitadosDTO> listarServiciosMasSolicitados(Date fechaInicio, Date fechaFin) {
        if (fechaInicio != null && fechaFin != null && !fechaInicio.before(fechaFin)) {
            throw new NegocioException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        try {
            return reporteServicioDAO.obtenerServiciosMasSolicitados(fechaInicio, fechaFin);
        } catch (Exception ex) {
            throw new NegocioException("Error al listar los servicios más solicitados.");
        }
    }
}
