package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.enums.DiaSemana;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public interface HorarioLaboralDAO {

    public List<HorarioLaboral> listarHorarios() throws SQLException;
    public List<HorarioLaboral> horariosPorEmpleado(int idEmpleado) throws SQLException;

    public void insertarHorario(HorarioLaboral horario) throws SQLException;
    public void eliminarHorarioLaboral(int idHorario) throws SQLException;


    public void actualizarHorarioLaboral(int idHorario, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) throws SQLException;



}
