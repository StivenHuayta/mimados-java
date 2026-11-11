package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.EmpleadoPuesto;

import java.sql.SQLException;
import java.time.LocalDate;

public interface EmpleadoPuestoDAO {

    public void insertar_empleado_puesto(EmpleadoPuesto empleadoPuesto) throws SQLException;
    public void iniciar_empleado_puesto(int idEmpleadoPuesto) throws SQLException;
    public void eliminar_empleado_puesto(int idEmpleadoPuesto, LocalDate fechaFin) throws SQLException;

}
