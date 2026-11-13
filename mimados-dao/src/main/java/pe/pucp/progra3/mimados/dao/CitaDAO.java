package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Cita;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

public interface CitaDAO {

    public void insertar_cita(Cita cita) throws SQLException;
    public List<Cita> listar_citas_por_mascota(int idMascota) throws SQLException;
    public Cita mostrar_cita(int id) throws SQLException;
    public void cancelar_cita(int idCita) throws SQLException;
    public void atender_cita(int idCita) throws SQLException;
    public void actualizar_cita(Cita cita) throws SQLException;
    List<Cita> listar_citas_por_empleado_fecha(
            int idEmpleado, LocalDate fecha) throws SQLException;
}
