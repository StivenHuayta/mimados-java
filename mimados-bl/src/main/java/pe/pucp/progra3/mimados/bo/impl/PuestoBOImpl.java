package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.PuestoBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.PuestoDAO;
import pe.pucp.progra3.mimados.dao.imp.PuestoDAOimp;
import pe.pucp.progra3.mimados.model.Puesto;

import java.sql.SQLException;
import java.util.List;

public class PuestoBOImpl implements PuestoBO {

    private final PuestoDAO puestoDAO;

    public PuestoBOImpl() {
        this.puestoDAO = new PuestoDAOimp();
    }

    @Override
    public void insertar(Puesto puesto)
            throws SQLException, NegocioException {

        validarPuesto(puesto);

        try {
            puestoDAO.insertar_puesto(puesto);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Puesto puesto)
            throws SQLException, NegocioException {

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
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Puesto> listarTodos()
            throws SQLException {

        return puestoDAO.listar_puestos();
    }

    @Override
    public Puesto obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return puestoDAO.mostrar_puesto(id);
    }

    private void validarPuesto(Puesto puesto)
            throws NegocioException {

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

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del puesto debe ser mayor que cero."
            );
        }
    }
}