package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Servicio;

import java.sql.SQLException;
import java.util.List;

public interface ServicioBO {

    void insertar(Servicio servicio)
            throws SQLException, NegocioException;

    void modificar(Servicio servicio)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<Servicio> listarTodos()
            throws SQLException;

    Servicio obtenerPorId(int id)
            throws SQLException, NegocioException;
}