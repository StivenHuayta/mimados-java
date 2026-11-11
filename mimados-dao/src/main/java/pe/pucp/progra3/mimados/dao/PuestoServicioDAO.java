package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.PuestoServicio;

import java.sql.SQLException;
import java.util.List;

public interface PuestoServicioDAO {


    public List<PuestoServicio> listar_servicios_puesto(int id_puesto) throws SQLException;

    public List<PuestoServicio> listar_servicio_puesto_general() throws SQLException;


    public void insertar_puesto_servicio(PuestoServicio ps) throws SQLException;
    public void eliminar_puesto_servicio(int id_puesto_servicio) throws SQLException;

    public PuestoServicio mostrar_puesto_servicio(int id_puesto_servicio) throws SQLException;

    public void actualizar_puesto_servicio(PuestoServicio puestoServicio ) throws SQLException;

}
