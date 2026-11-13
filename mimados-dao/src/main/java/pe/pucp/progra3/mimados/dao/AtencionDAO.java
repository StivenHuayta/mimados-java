package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Atencion;

import java.sql.SQLException;

public interface AtencionDAO {

    public void insertar_atencion(Atencion atencion) throws SQLException;
    public Atencion mostrar_atencion(int idAtencion) throws SQLException;
    public void actualizar_atencion(Atencion atencion) throws SQLException;

}
