package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Mascota;

import java.sql.SQLException;
import java.util.List;

public interface MascotaBO {

    void insertar(Mascota mascota)
            throws SQLException, NegocioException;

    void modificar(Mascota mascota)
            throws SQLException, NegocioException;

    void eliminar(int id)
            throws SQLException, NegocioException;

    List<Mascota> listarTodos()
            throws SQLException;

    Mascota obtenerPorId(int id)
            throws SQLException, NegocioException;

    List<Mascota> listarPorDueno(int idCliente)
            throws SQLException, NegocioException;
}