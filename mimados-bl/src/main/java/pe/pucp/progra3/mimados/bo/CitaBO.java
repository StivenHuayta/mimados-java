package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.dto.response.CitaListadoEmpleadoDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaReporteDTO;
import pe.pucp.progra3.mimados.model.dto.response.PageResultDTO;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.enums.Estado;

import java.time.LocalDate;
import java.util.List;

public interface CitaBO {

    void insertar(Cita cita);

    List<Cita> listarPorMascota(int idMascota);



    List<CitaListadoEmpleadoDTO> listarTodas();



    Cita obtenerPorId(int id);

    void cancelar(int idCita);

    void atender(int idCita);

    void modificar(Cita cita);

    void insertarConServicio(Cita cita, int idSede, int idSedeServicio);
    PageResultDTO<CitaVeterinarioDTO> listarPorVeterinarioPaginado(
            int empleadoId, String searchText, LocalDate fecha, Estado estado,
            int pageNumber, int recordsPerPage);

    List<CitaReporteDTO> listarCitasReporte(
        LocalDate fechaInicio, LocalDate fechaFin, Estado estado) throws NegocioException;
}
