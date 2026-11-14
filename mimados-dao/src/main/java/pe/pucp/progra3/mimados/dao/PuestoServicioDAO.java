package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.PuestoServicio;

import java.sql.SQLException;
import java.util.List;

public interface PuestoServicioDAO {


    public List<PuestoServicio> listarServiciosPuesto(int idPuesto) throws SQLException;

    public List<PuestoServicio> listarServicioPuestoGeneral() throws SQLException;


    public void insertarPuestoServicio(PuestoServicio ps) throws SQLException;
    public void eliminarPuestoServicio(int idPuestoServicio) throws SQLException;

    public PuestoServicio mostrarPuestoServicio(int idPuestoServicio) throws SQLException;

    public void actualizarPuestoServicio(PuestoServicio puestoServicio) throws SQLException;

}
