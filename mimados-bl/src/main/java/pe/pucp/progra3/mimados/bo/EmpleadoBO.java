package pe.pucp.progra3.mimados.bo;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import java.util.List;

public interface EmpleadoBO {
    void insertar(Empleado empleado);
    void eliminar(int id);
    List<Empleado> listarTodos();
    Empleado obtenerPorId(int id);
    
    // NUEVO MÉTODO
    void modificar(Empleado empleado);
}
