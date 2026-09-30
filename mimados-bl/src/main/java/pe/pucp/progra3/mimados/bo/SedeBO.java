package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Sede;

import java.sql.SQLException;
import java.util.List;

public interface SedeBO {

    void insertar(Sede sede)
            throws SQLException, NegocioException;

    void modificar(Sede sede)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<Sede> listarTodos()
            throws SQLException;

    Sede obtenerPorId(int id)
            throws SQLException, NegocioException;
}