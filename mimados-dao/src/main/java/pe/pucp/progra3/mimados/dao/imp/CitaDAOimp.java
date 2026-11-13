
package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.enums.Estado;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOimp implements CitaDAO {

    @Override
    public void insertar_cita(Cita cita) throws SQLException {

        /*
         * Procedimiento: insertar_cita
         * Registra una cita y devuelve su ID generado.
         */

        Connection connection = TransactionContext.getConnection();

        String sql = "{CALL insertar_cita(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {

            cs.registerOutParameter(6, Types.INTEGER);

            cs.setInt(1, cita.getEmpleado().getUsuario().getId());
            cs.setInt(2, cita.getMascota().getId());
            cs.setObject(3, cita.getFechaHoraInicio());
            cs.setObject(4, cita.getFechaHoraFin());
            cs.setString(5, cita.getMotivo());

            cs.execute();

            int idGenerado = cs.getInt(6);
            cita.setId(idGenerado);
        }
    }

    @Override
    public List<Cita> listar_citas_por_mascota(int idMascota)
            throws SQLException {

        /*
         * Procedimiento: listar_citas_por_mascota
         * Recupera las citas asociadas a una mascota.
         */

        List<Cita> citas = new ArrayList<>();

        String sql = "{CALL listar_citas_por_mascota(?)}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, idMascota);

            try (ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {

                    Empleado empleado = new Empleado();
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("empleado_id"));
                    empleado.setUsuario(usuario);

                    Mascota mascota = new Mascota();
                    mascota.setId(idMascota);

                    LocalDateTime inicio = rs.getObject(
                            "fecha_hora_inicio", LocalDateTime.class);

                    LocalDateTime fin = rs.getObject(
                            "fecha_hora_fin", LocalDateTime.class);

                    String estadoStr = rs.getString("estado");

                    Estado estado = estadoStr != null
                            ? Estado.valueOf(estadoStr.toUpperCase())
                            : null;

                    Cita cita = new Cita(
                            rs.getInt("cita_id"),
                            empleado,
                            mascota,
                            inicio,
                            fin,
                            estado,
                            rs.getString("motivo")
                    );

                    citas.add(cita);
                }
            }
        }

        return citas;
    }

    @Override
    public Cita mostrar_cita(int id) throws SQLException {

        /*
         * Procedimiento: mostrar_cita
         * Recupera una cita mediante su identificador.
         */

        String sql = "{CALL mostrar_cita(?)}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, id);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {

                    Empleado empleado = new Empleado();
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("empleado_id"));
                    empleado.setUsuario(usuario);

                    Mascota mascota = new Mascota();
                    mascota.setId(rs.getInt("mascota_id"));

                    LocalDateTime inicio = rs.getObject(
                            "fecha_hora_inicio", LocalDateTime.class);

                    LocalDateTime fin = rs.getObject(
                            "fecha_hora_fin", LocalDateTime.class);

                    String estadoStr = rs.getString("estado");

                    Estado estado = estadoStr != null
                            ? Estado.valueOf(estadoStr.toUpperCase())
                            : null;

                    return new Cita(
                            id,
                            empleado,
                            mascota,
                            inicio,
                            fin,
                            estado,
                            rs.getString("motivo")
                    );
                }
            }
        }

        return null;
    }

    @Override
    public void cancelar_cita(int idCita) throws SQLException {

        /*
         * Procedimiento: cancelar_cita
         * Cambia el estado de una cita a CANCELADA.
         */

        Connection connection = TransactionContext.getConnection();

        String sql = "{CALL cancelar_cita(?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, idCita);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println(
                        "Advertencia: No se encontró la cita con ID "
                                + idCita
                );
            }
        }
    }

    @Override
    public void atender_cita(int idCita) throws SQLException {

        /*
         * Procedimiento: atender_cita
         * Actualiza el estado de una cita atendida.
         */

        Connection connection = TransactionContext.getConnection();

        String sql = "{CALL atender_cita(?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, idCita);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println(
                        "Advertencia: No se encontró la cita con ID "
                                + idCita
                );
            }
        }
    }

    @Override
    public void actualizar_cita(Cita cita) throws SQLException {

        /*
         * Procedimiento: actualizar_cita
         * Modifica el empleado, fechas y motivo de una cita.
         */

        Connection connection = TransactionContext.getConnection();

        String sql = "{CALL actualizar_cita(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, cita.getId());

            cs.setInt(2,
                    cita.getEmpleado().getUsuario().getId());

            cs.setObject(3, cita.getFechaHoraInicio());

            cs.setObject(4, cita.getFechaHoraFin());

            cs.setString(5, cita.getMotivo());

            cs.execute();
        }
    }

    @Override
    public List<Cita> listar_citas_por_empleado_fecha(
            int idEmpleado, LocalDate fecha) throws SQLException {

        /*
         * Procedimiento: listar_citas_por_empleado_fecha
         * Recupera las citas ocupadas de un empleado en una fecha.
         */

        List<Cita> citas = new ArrayList<>();

        String sql = "{CALL listar_citas_por_empleado_fecha(?, ?)}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, idEmpleado);
            cs.setDate(2, java.sql.Date.valueOf(fecha));

            try (ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {

                    Cita cita = new Cita();

                    cita.setId(rs.getInt("cita_id"));

                    LocalDateTime inicio = rs.getObject(
                            "fecha_hora_inicio", LocalDateTime.class);

                    LocalDateTime fin = rs.getObject(
                            "fecha_hora_fin", LocalDateTime.class);

                    cita.setFechaHoraInicio(inicio);
                    cita.setFechaHoraFin(fin);

                    String estadoStr = rs.getString("estado");

                    if (estadoStr != null) {
                        cita.setEstado(
                                Estado.valueOf(estadoStr.toUpperCase())
                        );
                    }

                    citas.add(cita);
                }
            }
        }

        return citas;
    }
}
