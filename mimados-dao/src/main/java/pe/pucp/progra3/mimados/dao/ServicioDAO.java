package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ServicioDAO {

    public List<Servicio> listarServicios() throws SQLException;
    public void insertarServicio(Servicio servicio) throws SQLException;
    public void eliminarServicio(int idServicio) throws SQLException;

    public void actualizarServicioPrecio(int idServicio, BigDecimal precio) throws SQLException;
    public void actualizarServicio(Servicio servicio) throws SQLException;


    public Servicio mostrarServicio(int idServicio) throws SQLException;



}
