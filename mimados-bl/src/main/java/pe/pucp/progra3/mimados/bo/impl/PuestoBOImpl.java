package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.PuestoBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.PuestoDAO;
import pe.pucp.progra3.mimados.dao.imp.PuestoDAOimp;
import pe.pucp.progra3.mimados.model.entity.Puesto;

import java.sql.SQLException;
import java.util.List;

public class PuestoBOImpl implements PuestoBO {

    private final PuestoDAO puestoDAO;

    public PuestoBOImpl() {
        this.puestoDAO = new PuestoDAOimp();
    }

    @Override
    public void insertar(Puesto puesto) {

        validarPuesto(puesto);

        try {
            puestoDAO.insertar_puesto(puesto);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el puesto.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Puesto puesto) {

        if (puesto == null) {
            throw new NegocioException(
                    "El puesto no puede ser nulo."
            );
        }

        validarId(puesto.getId());
        validarPuesto(puesto);

        try {
            puestoDAO.actualizar_puesto(puesto);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el puesto.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Puesto> listarTodos() {

        try {
            return puestoDAO.listar_puestos();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los puestos.", e);
        }
    }

    @Override
    public Puesto obtenerPorId(int id) {

        validarId(id);

        try {
            return puestoDAO.mostrar_puesto(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el puesto.", e);
        }
    }

    private void validarPuesto(Puesto puesto) {

        if (puesto == null) {
            throw new NegocioException(
                    "El puesto no puede ser nulo."
            );
        }

        if (puesto.getNombre() == null ||
                puesto.getNombre().isBlank()) {

            throw new NegocioException(
                    "El nombre del puesto es obligatorio."
            );
        }

        if (puesto.getNombre().length() > 45) {
            throw new NegocioException(
                    "El nombre del puesto no puede superar los 45 caracteres."
            );
        }

        if (puesto.getDescripcion() != null &&
                puesto.getDescripcion().length() > 200) {

            throw new NegocioException(
                    "La descripción no puede superar los 200 caracteres."
            );
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del puesto debe ser mayor que cero."
            );
        }
    }
}