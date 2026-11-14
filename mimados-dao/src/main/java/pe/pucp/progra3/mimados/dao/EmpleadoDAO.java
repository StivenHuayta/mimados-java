package pe.pucp.progra3.mimados.dao;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import java.sql.SQLException;
import java.util.List;

public interface EmpleadoDAO {
    void insertarEmpleado(Empleado empleado) throws SQLException;
    void eliminarEmpleado(int id) throws SQLException;
    List<Empleado> listarEmpleados() throws SQLException;
    Empleado mostrarEmpleado(int id) throws SQLException;

    // NUEVO MÉTODO PARA MODIFICAR
    void actualizarEmpleado(Empleado empleado) throws SQLException;
}
