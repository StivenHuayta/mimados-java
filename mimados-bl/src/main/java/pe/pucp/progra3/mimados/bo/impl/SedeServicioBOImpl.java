package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.SedeServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.SedeServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.SedeServicioDAOimp;
import pe.pucp.progra3.mimados.model.SedeServicio;
import pe.pucp.progra3.mimados.model.Servicio;

import java.sql.SQLException;
import java.util.List;

public class SedeServicioBOImpl implements SedeServicioBO {

    private final SedeServicioDAO sedeServicioDAO;

    public SedeServicioBOImpl() {
        this.sedeServicioDAO = new SedeServicioDAOimp();
    }

    @Override
    public void insertar(SedeServicio sedeServicio)
            throws SQLException, NegocioException {

        validarSedeServicio(sedeServicio);

        int idSede = sedeServicio.getSede().getId();
        int idServicio = sedeServicio.getServicio().getId();

        validarNoDuplicado(idSede, idServicio);

        try {
            sedeServicioDAO.insertar_sede_servicio(sedeServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int idSedeServicio)
            throws SQLException, NegocioException {

        validarId(idSedeServicio);

        try {
            sedeServicioDAO.eliminar_sede_servicio(idSedeServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Servicio> listarPorSede(int idSede)
            throws SQLException, NegocioException {

        validarId(idSede);

        return sedeServicioDAO.listar_servicios_por_sede(idSede);
    }

    private void validarSedeServicio(SedeServicio sedeServicio)
            throws NegocioException {

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

    private void validarNoDuplicado(int idSede, int idServicio)
            throws SQLException, NegocioException {

        List<Servicio> servicios =
                sedeServicioDAO.listar_servicios_por_sede(idSede);

        for (Servicio servicio : servicios) {

            if (servicio.getId() != null &&
                    servicio.getId() == idServicio) {

                throw new NegocioException(
                        "El servicio ya está asociado a la sede."
                );
            }
        }
    }

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}