package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.EmpleadoBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.dao.imp.EmpleadoDAOimp;
import pe.pucp.progra3.mimados.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoBOImpl implements EmpleadoBO {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoBOImpl() {
        this.empleadoDAO = new EmpleadoDAOimp();
    }

    @Override
    public void insertar(Empleado empleado)
            throws SQLException, NegocioException {

        validarEmpleado(empleado);

        try {
            empleadoDAO.insertar_empleado(empleado);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id)
            throws SQLException, NegocioException {

        validarId(id);

        try {
            empleadoDAO.eliminar_empleado(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Empleado> listarTodos()
            throws SQLException {

        return empleadoDAO.listar_empleados();
    }

    @Override
    public Empleado obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return empleadoDAO.mostrar_empleado(id);
    }

    private void validarEmpleado(Empleado empleado)
            throws NegocioException {

        if (empleado == null) {
            throw new NegocioException(
                    "El empleado no puede ser nulo."
            );
        }

        if (empleado.getUsuario() == null) {
            throw new NegocioException(
                    "El empleado debe tener un usuario."
            );
        }

        validarId(empleado.getUsuario().getId());

        if (empleado.getSede() == null) {
            throw new NegocioException(
                    "El empleado debe tener una sede."
            );
        }

        validarId(empleado.getSede().getId());

        if (empleado.getCodigoCmvp() != null &&
                empleado.getCodigoCmvp().length() > 10) {

            throw new NegocioException(
                    "El código CMVP no puede superar los 10 caracteres."
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