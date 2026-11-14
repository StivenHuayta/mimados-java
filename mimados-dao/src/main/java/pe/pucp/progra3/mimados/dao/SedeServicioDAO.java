package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.sql.SQLException;
import java.util.List;

public interface SedeServicioDAO {

    public void insertarSedeServicio(SedeServicio sedeServicio) throws SQLException;
    public List<SedeServicioDTO> listarServiciosPorSede(int idSede) throws SQLException;
    public void eliminarSedeServicio(int idSedeServicio) throws SQLException;

    public List<Servicio> listarServiciosDisponibles(int idSede) throws SQLException;

}
