package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.Empleado;

import java.sql.SQLException;
import java.util.List;

public interface EmpleadoDAO  {

    public List<Empleado> listar_empleados() throws SQLException;

    public void insertar_empleado(Empleado empleado) throws SQLException;
    public void eliminar_empleado(int id_empleado) throws SQLException;

    public Empleado mostrar_empleado(int id_empleado) throws SQLException;
    //public void actualizar_empleado


}
