package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.SedeServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.SedeServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.SedeServicioDAOimp;
import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.sql.SQLException;
import java.util.List;

public class SedeServicioBOImpl implements SedeServicioBO {

    private final SedeServicioDAO sedeServicioDAO;

    public SedeServicioBOImpl() {
        this.sedeServicioDAO = new SedeServicioDAOimp();
    }

    @Override
    public void insertar(SedeServicio sedeServicio) {

        validarSedeServicio(sedeServicio);

        int idSede = sedeServicio.getSede().getId();
        int idServicio = sedeServicio.getServicio().getId();

        validarNoDuplicado(idSede, idServicio);

        try {
            sedeServicioDAO.insertar_sede_servicio(sedeServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al asociar el servicio a la sede.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int idSedeServicio) {

        validarId(idSedeServicio);

        try {
            sedeServicioDAO.eliminar_sede_servicio(idSedeServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar el servicio de la sede.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<SedeServicioDTO> listarPorSede(int idSede) {

        validarId(idSede);

        try {
            return sedeServicioDAO.listar_servicios_por_sede(idSede);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los servicios de la sede.", e);
        }
    }

    @Override
    public List<Servicio> listar_servicios_disponibles(int idSede) {
        validarId(idSede);

        try {
            return sedeServicioDAO.listar_servicios_disponibles(idSede);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los servicios disponibles de la sede.", e);
        }
    }

    private void validarSedeServicio(SedeServicio sedeServicio) {

        if (sedeServicio == null) {
            throw new NegocioException(
                    "La relación sede-servicio no puede ser nula."
            );
        }

        if (sedeServicio.getSede() == null) {
            throw new NegocioException(
                    "Debe indicarse una sede."
            );
        }

        validarId(sedeServicio.getSede().getId());

        if (sedeServicio.getServicio() == null) {
            throw new NegocioException(
                    "Debe indicarse un servicio."
            );
        }

        validarId(sedeServicio.getServicio().getId());
    }

    private void validarNoDuplicado(int idSede, int idServicio) {

        List<SedeServicioDTO> servicios;
        try {
            servicios = sedeServicioDAO.listar_servicios_por_sede(idSede);
        } catch (SQLException e) {
            throw new NegocioException("Error al validar servicios duplicados en la sede.", e);
        }

        for (SedeServicioDTO servicio : servicios) {

            if (servicio.getIdSedeServicio() != null &&
                    servicio.getIdSedeServicio() == idServicio) {

                throw new NegocioException(
                        "El servicio ya está asociado a la sede."
                );
            }
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