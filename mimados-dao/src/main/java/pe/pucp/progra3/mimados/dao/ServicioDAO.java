package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.Servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ServicioDAO {

    public List<Servicio> listar_servicios() throws SQLException;
    public void insertar_servicio(Servicio servicio) throws SQLException;
    public void eliminar_servicio(int id_servicio) throws SQLException;

    public void actualizar_servicio_precio(int id_servicio , BigDecimal precio) throws SQLException;
    public Servicio mostrar_servicio(int id_servicio) throws SQLException;



}
