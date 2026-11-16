package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.entity.Cita;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

public interface CitaDAO {

    public void insertarCita(Cita cita) throws SQLException;
    public List<Cita> listarCitasPorMascota(int idMascota) throws SQLException;
    public Cita mostrarCita(int id) throws SQLException;
    public void cancelarCita(int idCita) throws SQLException;
    public void atenderCita(int idCita) throws SQLException;
    public void actualizarCita(Cita cita) throws SQLException;
    List<Cita> listarCitasPorEmpleadoFecha(
            int idEmpleado, LocalDate fecha) throws SQLException;
}
