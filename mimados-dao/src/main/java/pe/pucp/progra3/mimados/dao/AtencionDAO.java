package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Atencion;

import java.sql.SQLException;

public interface AtencionDAO {

    public void insertarAtencion(Atencion atencion) throws SQLException;
    public Atencion mostrarAtencion(int idAtencion) throws SQLException;
    public void actualizarAtencion(Atencion atencion) throws SQLException;

}
