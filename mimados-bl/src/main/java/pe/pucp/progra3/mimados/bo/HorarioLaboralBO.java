package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.HorarioLaboral;

import java.sql.SQLException;
import java.util.List;

public interface HorarioLaboralBO {

    void insertar(HorarioLaboral horario)
            throws SQLException, NegocioException;

    void modificar(HorarioLaboral horario)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<HorarioLaboral> listarTodos()
            throws SQLException;

    List<HorarioLaboral> listarPorEmpleado(int idEmpleado)
            throws SQLException, NegocioException;
}