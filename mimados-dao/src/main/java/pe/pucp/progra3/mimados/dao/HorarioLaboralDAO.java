package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.enums.DiaSemana;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public interface HorarioLaboralDAO {

    public List<HorarioLaboral> listar_horarios() throws SQLException;
    public List<HorarioLaboral> horarios_por_empleado(int id_empleado) throws SQLException;

    public void insertar_horario( HorarioLaboral horario) throws SQLException;
    public void eliminar_horario_laboral( int id_horario) throws SQLException;


    public void actualizar_horario_laboral(int id_horario, DiaSemana diaSemana , LocalTime horai , LocalTime horaf) throws  SQLException;



}
