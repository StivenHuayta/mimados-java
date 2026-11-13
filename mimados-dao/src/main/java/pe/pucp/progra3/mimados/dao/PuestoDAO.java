package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Puesto;

import java.sql.SQLException;
import java.util.List;

public interface PuestoDAO {

    public void insertar_puesto(Puesto puesto) throws SQLException;
    public List<Puesto> listar_puestos() throws SQLException;
    public Puesto mostrar_puesto(int idPuesto) throws SQLException;
    public void actualizar_puesto(Puesto puesto) throws SQLException;

}
