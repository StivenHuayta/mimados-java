package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Puesto;

import java.sql.SQLException;
import java.util.List;

public interface PuestoDAO {

    public void insertarPuesto(Puesto puesto) throws SQLException;
    public List<Puesto> listarPuestos() throws SQLException;
    public Puesto mostrarPuesto(int idPuesto) throws SQLException;
    public void actualizarPuesto(Puesto puesto) throws SQLException;

}
