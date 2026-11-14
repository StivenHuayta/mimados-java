package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.EmpleadoBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.dao.imp.EmpleadoDAOimp;
import pe.pucp.progra3.mimados.model.entity.Empleado;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoBOImpl implements EmpleadoBO {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoBOImpl() {
        this.empleadoDAO = new EmpleadoDAOimp();
    }

    @Override
    public void insertar(Empleado empleado) {

        validarEmpleado(empleado);

        try {
            empleadoDAO.insertar_empleado(empleado);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el empleado.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id) {

        validarId(id);

        try {
            empleadoDAO.eliminar_empleado(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar el empleado.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Empleado> listarTodos() {

        try {
            return empleadoDAO.listar_empleados();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los empleados.", e);
        }
    }

    @Override
    public Empleado obtenerPorId(int id) {

        validarId(id);

        try {
            return empleadoDAO.mostrar_empleado(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el empleado.", e);
        }
    }

    private void validarEmpleado(Empleado empleado) {

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

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }

    @Override
    public void modificar(Empleado empleado) {
        // Reutilizamos tus validaciones existentes
        validarEmpleado(empleado);

        try {
            empleadoDAO.actualizar_empleado(empleado);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el empleado.", e);
        } finally {
            TransactionContext.close();
        }
    }
}
