package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.PuestoServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.PuestoServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.PuestoServicioDAOimp;
import pe.pucp.progra3.mimados.model.PuestoServicio;

import java.sql.SQLException;
import java.util.List;

public class PuestoServicioBOImpl implements PuestoServicioBO {

    private final PuestoServicioDAO puestoServicioDAO;

    public PuestoServicioBOImpl() {
        this.puestoServicioDAO = new PuestoServicioDAOimp();
    }

    @Override
    public void insertar(PuestoServicio puestoServicio)
            throws SQLException, NegocioException {

        validarPuestoServicio(puestoServicio);

        try {
            puestoServicioDAO.insertar_puesto_servicio(puestoServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int idPuestoServicio)
            throws SQLException, NegocioException {

        validarId(idPuestoServicio);

        try {
            puestoServicioDAO.eliminar_puesto_servicio(idPuestoServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<PuestoServicio> listarPorPuesto(int idPuesto)
            throws SQLException, NegocioException {

        validarId(idPuesto);

        return puestoServicioDAO.listar_servicios_puesto(idPuesto);
    }
    @Override
    public PuestoServicio obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return puestoServicioDAO.mostrar_puesto_servicio(id);
    }

    private void validarPuestoServicio(PuestoServicio puestoServicio)
            throws NegocioException {

        if (puestoServicio == null) {
            throw new NegocioException(
                    "La relación puesto-servicio no puede ser nula."
            );
        }

        if (puestoServicio.getServicio() == null) {
            throw new NegocioException(
                    "Debe indicarse un servicio."
            );
        }

        validarId(puestoServicio.getServicio().getId());

        if (puestoServicio.getPuesto() == null) {
            throw new NegocioException(
                    "Debe indicarse un puesto."
            );
        }

        validarId(puestoServicio.getPuesto().getId());
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