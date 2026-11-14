package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.HorarioLaboralDAO;
import pe.pucp.progra3.mimados.model.enums.DiaSemana;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;
import pe.pucp.progra3.mimados.model.entity.Sede;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HorarioLaboralDAOImpl implements HorarioLaboralDAO {

    @Override
    public List<HorarioLaboral> listarHorarios() throws SQLException {

        List<HorarioLaboral> horarios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_horario_laboral()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                HorarioLaboral horarioLaboral = new HorarioLaboral(
                        rs.getInt("horario_id"),
                        rs.getInt("empleado_id"),
                        DiaSemana.values()[rs.getInt("dia_semana") - 1],
                        rs.getObject("hora_inicio", LocalTime.class),
                        rs.getObject("hora_fin", LocalTime.class),
                        rs.getBoolean("activo")

                );
                horarios.add(horarioLaboral);
            }
        }
        return horarios;
    }

    @Override
    public List<HorarioLaboral> horariosPorEmpleado(int idEmpleado) throws SQLException {

        List<HorarioLaboral> horarios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_horarios_por_empleado( ? )}");){

            cs.setInt(1, idEmpleado);
            try (ResultSet rs = cs.executeQuery()) {

                while(rs.next()){

                    HorarioLaboral horarioLaboral = new HorarioLaboral(
                            rs.getInt("horario_id"),
                            DiaSemana.values()[rs.getInt("dia_semana") - 1],
                            rs.getObject("hora_inicio", LocalTime.class),
                            rs.getObject("hora_fin", LocalTime.class),
                            rs.getBoolean("activo")
                    );
                    horarios.add(horarioLaboral);
                }
            }
        }
        return horarios;

    }

    @Override
    public void actualizarHorarioLaboral(int idHorario, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_horario_laboral(?,?,?,?)}")) {

            cs.setInt(1 , idHorario);
            cs.setString(2, diaSemana.name());
            cs.setObject(3, horaInicio);
            cs.setObject(4, horaFin);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el horario con ID " + idHorario);
            }

        }





    }


    //ZONA TRANSACCION





    @Override
    public void insertarHorario(HorarioLaboral horario) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL insertar_horario_laboral(?,?,?,?)}")){



            cs.setInt(1 , horario.getEmpleado().getUsuario().getId());
            cs.setString(2, horario.getDiaSemana().name() );
            cs.setObject(3, horario.getHoraInicio());
            cs.setObject(4, horario.getHoraFin());

            cs.execute();


//            int id_generado = cs.getInt(6);
//            horario.setId(id_generado);
        }
    }


    @Override
    public void eliminarHorarioLaboral(int idHorario) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_horario_laboral(?)}")){

            cs.setInt(1 , idHorario);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el horario con ID " + idHorario);
            }
        }


    }

}
