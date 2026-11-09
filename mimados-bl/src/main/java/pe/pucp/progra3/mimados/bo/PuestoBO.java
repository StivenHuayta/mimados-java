package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Puesto;

import java.sql.SQLException;
import java.util.List;

public interface PuestoBO {

    void insertar(Puesto puesto)
            throws SQLException, NegocioException;

    void modificar(Puesto puesto)
            throws SQLException, NegocioException;

    List<Puesto> listarTodos()
            throws SQLException;

    Puesto obtenerPorId(int id)
            throws SQLException, NegocioException;
}