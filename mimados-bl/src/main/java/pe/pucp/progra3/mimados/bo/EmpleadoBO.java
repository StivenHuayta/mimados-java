package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface EmpleadoBO {

    void insertar(Empleado empleado)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<Empleado> listarTodos()
            throws SQLException;

    Empleado obtenerPorId(int id)
            throws SQLException, NegocioException;
}