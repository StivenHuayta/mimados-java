package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Sede;

import java.sql.SQLException;
import java.util.List;

public interface SedeDAO {

    public List<Sede> listar_sedes() throws SQLException;
    public Sede mostrar_sede(int id_sede) throws SQLException;
    public void modificar_direccion(int id , String dir) throws SQLException;// xd
    public void modificar_sede(Sede sede) throws SQLException;


    public void agregar_sede( Sede sede ) throws SQLException;
    public void eliminar_sede(int id) throws SQLException;






}
