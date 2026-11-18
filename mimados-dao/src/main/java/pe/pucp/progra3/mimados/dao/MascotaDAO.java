package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.AtencionHistorialDTO;
import pe.pucp.progra3.mimados.model.dto.response.PacienteVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.entity.Mascota;

import java.sql.SQLException;
import java.util.List;

public interface MascotaDAO {

    public void insertarMascota(Mascota mascota) throws SQLException;
    public List<Mascota> listarMascotas() throws SQLException;
    public Mascota mostrarMascota(int id) throws SQLException;
    public List<Mascota> mostrarMascotasDueno(int idCliente) throws SQLException;
    public void actualizarMascotaNombre(int id, String nuevoNombre) throws SQLException;
    public void eliminarMascota(int id) throws SQLException;

    public List<RegistroMedicoDTO> listarHistorialPorMascota(int mascotaId) throws SQLException;

    int contarPacientesVeterinario(String searchText) throws SQLException;
    List<PacienteVeterinarioDTO> listarPacientesVeterinarioPaginado(
            String searchText, int pageNumber, int recordsPerPage) throws SQLException;
    List<AtencionHistorialDTO> listarHistorialClinicoVeterinario(int mascotaId) throws SQLException;
}
