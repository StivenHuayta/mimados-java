package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Sede;

import java.sql.SQLException;
import java.util.List;

public interface SedeDAO {

    public List<Sede> listarSedes() throws SQLException;
    public Sede mostrarSede(int idSede) throws SQLException;
    public void modificarDireccion(int id, String dir) throws SQLException;
    public void modificarSede(Sede sede) throws SQLException;


    public void agregarSede(Sede sede) throws SQLException;
    public void eliminarSede(int id) throws SQLException;






}
