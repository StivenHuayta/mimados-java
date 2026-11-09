package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.EmpleadoPuestoBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.EmpleadoPuestoDAO;
import pe.pucp.progra3.mimados.dao.imp.EmpleadoPuestoDAOimp;
import pe.pucp.progra3.mimados.model.EmpleadoPuesto;

import java.sql.SQLException;
import java.time.LocalDate;

public class EmpleadoPuestoBOImpl implements EmpleadoPuestoBO {

    private final EmpleadoPuestoDAO empleadoPuestoDAO;

    public EmpleadoPuestoBOImpl() {
        this.empleadoPuestoDAO = new EmpleadoPuestoDAOimp();
    }

    @Override
    public void insertar(EmpleadoPuesto empleadoPuesto)
            throws SQLException, NegocioException {

        validarEmpleadoPuesto(empleadoPuesto);

        try {
            empleadoPuestoDAO.insertar_empleado_puesto(empleadoPuesto);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void iniciar(int idEmpleadoPuesto)
            throws SQLException, NegocioException {

        validarId(idEmpleadoPuesto);

        try {
            empleadoPuestoDAO.iniciar_empleado_puesto(idEmpleadoPuesto);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int idEmpleadoPuesto, LocalDate fechaFin)
            throws SQLException, NegocioException {

        validarId(idEmpleadoPuesto);

        try {
            empleadoPuestoDAO.eliminar_empleado_puesto(
                    idEmpleadoPuesto,
                    fechaFin
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    private void validarEmpleadoPuesto(EmpleadoPuesto empleadoPuesto)
            throws NegocioException {

        if (empleadoPuesto == null) {
            throw new NegocioException(
                    "La relación empleado-puesto no puede ser nula."
            );
        }

        if (empleadoPuesto.getPuesto() == null) {
            throw new NegocioException(
                    "Debe indicarse un puesto."
            );
        }

        validarId(empleadoPuesto.getPuesto().getId());

        if (empleadoPuesto.getEmpleado() == null ||
                empleadoPuesto.getEmpleado().getUsuario() == null) {

            throw new NegocioException(
                    "Debe indicarse un empleado."
            );
        }

        validarId(
                empleadoPuesto.getEmpleado()
                        .getUsuario()
                        .getId()
        );

        if (empleadoPuesto.getActivo() == null) {
            throw new NegocioException(
                    "Debe indicarse el estado de la relación empleado-puesto."
            );
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