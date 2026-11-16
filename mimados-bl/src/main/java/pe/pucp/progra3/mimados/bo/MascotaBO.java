package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.entity.Mascota;

import java.util.List;

public interface MascotaBO {

    void insertar(Mascota mascota);

    void modificar(Mascota mascota);

    void eliminar(int id);

    List<Mascota> listarTodos();

    Mascota obtenerPorId(int id);

    List<Mascota> listarPorDueno(int idCliente);

    List<RegistroMedicoDTO> listarHistorialPorMascota(int mascotaId);

}