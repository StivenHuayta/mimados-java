package pe.pucp.progra3.mimados.dao;
import pe.pucp.progra3.mimados.model.Empleado;
import java.sql.SQLException;
import java.util.List;

public interface EmpleadoDAO {
    void insertar_empleado(Empleado empleado) throws SQLException;
    void eliminar_empleado(int id) throws SQLException;
    List<Empleado> listar_empleados() throws SQLException;
    Empleado mostrar_empleado(int id) throws SQLException;
    
    // NUEVO MÉTODO PARA MODIFICAR
    void actualizar_empleado(Empleado empleado) throws SQLException;
}
