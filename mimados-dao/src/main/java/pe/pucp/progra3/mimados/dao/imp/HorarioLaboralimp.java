package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.HorarioLaboralDAO;
import pe.pucp.progra3.mimados.model.DiaSemana;
import pe.pucp.progra3.mimados.model.HorarioLaboral;
import pe.pucp.progra3.mimados.model.Sede;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HorarioLaboralimp implements HorarioLaboralDAO {

    @Override
    public List<HorarioLaboral> listar_horarios() throws SQLException {

        List<HorarioLaboral> horarios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_horario_laboral()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                HorarioLaboral horarioLaboral = new HorarioLaboral(
                        rs.getInt("id"),
                        rs.getInt("empleado_id"),
                        rs.getObject("dia_semana" , DiaSemana.class),
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
    public List<HorarioLaboral> horarios_por_empleado(int id_empleado) throws SQLException {

        List<HorarioLaboral> horarios = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_horarios_por_empleado( ? )}");){

            cs.setInt(1, id_empleado);
            ResultSet rs = cs.executeQuery();

            while(rs.next()){

                HorarioLaboral horarioLaboral = new HorarioLaboral(
                        rs.getInt("id"),
                        rs.getObject("dia_semana" , DiaSemana.class),
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
    public void actualizar_horario_laboral(int id_horario, DiaSemana diaSemana, LocalTime horai, LocalTime horaf) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_horario_laboral(?,?,?,?)}")) {

            cs.setInt(1 , id_horario);
            cs.setString(2, diaSemana.name());
            cs.setObject(3, horai);
            cs.setObject(4, horaf);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la sede con ID " + id_horario);
            }

        }





    }


    //ZONA TRANSACCION





    @Override
    public void insertar_horario(HorarioLaboral horario) throws SQLException {
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
    public void eliminar_horario_laboral(int id_horario) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_horario_laboral(?)}")){

            cs.setInt(1 , id_horario);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el horario con ID " + id_horario);
            }
        }


    }

}
