package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.util.List;

public interface SedeServicioBO {

    void insertar(SedeServicio sedeServicio);

    void eliminar(int idSedeServicio);

    List<SedeServicioDTO> listarPorSede(int idSede);

    List<Servicio> listar_servicios_disponibles(int idSede);
}