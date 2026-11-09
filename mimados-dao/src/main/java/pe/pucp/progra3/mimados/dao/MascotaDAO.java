package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.DTO.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.Mascota;

import java.sql.SQLException;
import java.util.List;

public interface MascotaDAO {

    public void insertar_mascota(Mascota mascota) throws SQLException;
    public List<Mascota> listar_mascotas() throws SQLException;
    public Mascota mostrar_mascota(int id) throws SQLException;
    public List<Mascota> mostrar_mascotas_dueno(int idCliente) throws SQLException;
    public void actualizar_mascota_nombre(int id, String nuevoNombre) throws SQLException;
    public void eliminar_mascota(int id) throws SQLException;

    public List<RegistroMedicoDTO> listarHistorialPorMascota(int mascotaId) throws SQLException;


}
