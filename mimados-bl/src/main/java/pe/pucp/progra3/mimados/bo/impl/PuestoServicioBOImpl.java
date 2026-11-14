package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.PuestoServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.PuestoServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.PuestoServicioDAOimp;
import pe.pucp.progra3.mimados.model.entity.PuestoServicio;

import java.sql.SQLException;
import java.util.List;

public class PuestoServicioBOImpl implements PuestoServicioBO {

    private final PuestoServicioDAO puestoServicioDAO;

    public PuestoServicioBOImpl() {
        this.puestoServicioDAO = new PuestoServicioDAOimp();
    }

    @Override
    public void insertar(PuestoServicio puestoServicio) {

        validarPuestoServicio(puestoServicio);

        try {
            puestoServicioDAO.insertar_puesto_servicio(puestoServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar la relación puesto-servicio.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int idPuestoServicio) {

        validarId(idPuestoServicio);

        try {
            puestoServicioDAO.eliminar_puesto_servicio(idPuestoServicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar la relación puesto-servicio.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<PuestoServicio> listarPorPuesto(int idPuesto) {

        validarId(idPuesto);

        try {
            return puestoServicioDAO.listar_servicios_puesto(idPuesto);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los servicios del puesto.", e);
        }
    }

    @Override
    public PuestoServicio obtenerPorId(int id) {

        validarId(id);

        try {
            return puestoServicioDAO.mostrar_puesto_servicio(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener la relación puesto-servicio.", e);
        }
    }

    private void validarPuestoServicio(PuestoServicio puestoServicio) {

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

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}