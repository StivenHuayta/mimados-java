package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.CitaServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.CitaServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.CitaServicioDAOimp;
import pe.pucp.progra3.mimados.model.entity.CitaServicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CitaServicioBOImpl implements CitaServicioBO {

    private final CitaServicioDAO citaServicioDAO;

    public CitaServicioBOImpl() {
        this.citaServicioDAO = new CitaServicioDAOimp();
    }

    @Override
    public void insertar(CitaServicio citaServicio) {

        validarCitaServicio(citaServicio);

        try {
            citaServicioDAO.insertar_cita_servicio(citaServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el servicio de la cita.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(CitaServicio citaServicio) {

        validarCitaServicio(citaServicio);

        try {
            citaServicioDAO.actualizar_cita_servicio(citaServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el servicio de la cita.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<CitaServicio> listarPorCita(int idCita) {

        validarId(idCita);

        try {
            return citaServicioDAO.listar_servicios_por_cita(idCita);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los servicios de la cita.", e);
        }
    }

    private void validarCitaServicio(CitaServicio citaServicio) {

        if (citaServicio == null) {
            throw new NegocioException(
                    "El servicio de la cita no puede ser nulo."
            );
        }

        if (citaServicio.getCita() == null) {
            throw new NegocioException(
                    "Debe indicarse una cita."
            );
        }

        validarId(citaServicio.getCita().getId());

        if (citaServicio.getSedeServicio() == null) {
            throw new NegocioException(
                    "Debe indicarse un servicio de sede."
            );
        }

        validarId(citaServicio.getSedeServicio().getId());

        if (citaServicio.getPrecioAplicado() != null &&
                citaServicio.getPrecioAplicado()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new NegocioException(
                    "El precio aplicado no puede ser negativo."
            );
        }

        if (citaServicio.getNotas() != null &&
                citaServicio.getNotas().length() > 255) {

            throw new NegocioException(
                    "Las notas no pueden superar los 255 caracteres."
            );
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}