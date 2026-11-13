package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.sql.SQLException;
import java.util.List;

public interface SedeServicioDAO {

    public void insertar_sede_servicio(SedeServicio sedeServicio) throws SQLException;
    public List<SedeServicioDTO> listar_servicios_por_sede(int idSede) throws SQLException;
    public void eliminar_sede_servicio(int idSedeServicio) throws SQLException;

    public List<Servicio> listar_servicios_disponibles(int idSede)throws SQLException;

}
