package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.EmpleadoPuesto;

import java.sql.SQLException;
import java.time.LocalDate;

public interface EmpleadoPuestoBO {

    void insertar(EmpleadoPuesto empleadoPuesto)
            throws SQLException, NegocioException;

    void iniciar(int idEmpleadoPuesto)
            throws SQLException, NegocioException;

    void eliminar(int idEmpleadoPuesto, LocalDate fechaFin)
            throws SQLException, NegocioException;
}