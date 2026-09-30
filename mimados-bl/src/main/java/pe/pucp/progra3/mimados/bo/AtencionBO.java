package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.model.Atencion;

import java.sql.SQLException;

public interface AtencionBO {

    void insertar(Atencion atencion)
            throws SQLException, NegocioException;

    void modificar(Atencion atencion)
            throws SQLException, NegocioException;

    Atencion obtenerPorId(int id)
            throws SQLException, NegocioException;
}