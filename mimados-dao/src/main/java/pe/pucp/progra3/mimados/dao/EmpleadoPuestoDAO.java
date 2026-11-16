package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.EmpleadoPuesto;

import java.sql.SQLException;
import java.time.LocalDate;

public interface EmpleadoPuestoDAO {

    public void insertarEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) throws SQLException;
    public void iniciarEmpleadoPuesto(int idEmpleadoPuesto) throws SQLException;
    public void eliminarEmpleadoPuesto(int idEmpleadoPuesto, LocalDate fechaFin) throws SQLException;

}
