package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.model.dto.response.CitaListadoEmpleadoDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaReporteDTO;

import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.entity.Usuario;
import pe.pucp.progra3.mimados.model.enums.Estado;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public void insertarCita(Cita cita) throws SQLException {
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
            cita.setId(cs.getInt(6));
        }
    }

    @Override
    public List<Cita> listarCitasPorMascota(int idMascota) throws SQLException {
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
                    LocalDateTime inicio = rs.getObject("fecha_hora_inicio", LocalDateTime.class);
                    LocalDateTime fin = rs.getObject("fecha_hora_fin", LocalDateTime.class);
                    String estadoStr = rs.getString("estado");
                    Estado estado = estadoStr != null ? Estado.valueOf(estadoStr.toUpperCase()) : null;
                    citas.add(new Cita(
                            rs.getInt("cita_id"),
                            empleado,
                            mascota,
                            inicio,
                            fin,
                            estado,
                            rs.getString("motivo")
                    ));
                }
            }
        }
        return citas;
    }

    @Override
    public Cita mostrarCita(int id) throws SQLException {
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
                    LocalDateTime inicio = rs.getObject("fecha_hora_inicio", LocalDateTime.class);
                    LocalDateTime fin = rs.getObject("fecha_hora_fin", LocalDateTime.class);
                    String estadoStr = rs.getString("estado");
                    Estado estado = estadoStr != null ? Estado.valueOf(estadoStr.toUpperCase()) : null;
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
    public void cancelarCita(int idCita) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        String sql = "{CALL cancelar_cita(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, idCita);
            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la cita con ID " + idCita);
            }
        }
    }

    @Override
    public void atenderCita(int idCita) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        String sql = "{CALL atender_cita(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, idCita);
            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la cita con ID " + idCita);
            }
        }
    }

    @Override
    public void actualizarCita(Cita cita) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        String sql = "{CALL actualizar_cita(?, ?, ?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, cita.getId());
            cs.setInt(2, cita.getEmpleado().getUsuario().getId());
            cs.setObject(3, cita.getFechaHoraInicio());
            cs.setObject(4, cita.getFechaHoraFin());
            cs.setString(5, cita.getMotivo());
            cs.execute();
        }
    }

    @Override
    public List<Cita> listarCitasPorEmpleadoFecha(int idEmpleado, LocalDate fecha) throws SQLException {
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
                    cita.setFechaHoraInicio(rs.getObject("fecha_hora_inicio", LocalDateTime.class));
                    cita.setFechaHoraFin(rs.getObject("fecha_hora_fin", LocalDateTime.class));
                    String estadoStr = rs.getString("estado");
                    if (estadoStr != null) {
                        cita.setEstado(Estado.valueOf(estadoStr.toUpperCase()));
                    }
                    citas.add(cita);
                }
            }
        }
        return citas;
    }

    @Override
    public int contarCitasPorVeterinario(
            int empleadoId, String searchText, LocalDate fecha, Estado estado) throws SQLException {
        String sql = "{CALL sp_contar_citas_veterinario(?, ?, ?, ?)}";
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, empleadoId);
            if (searchText != null && !searchText.isBlank()) {
                cs.setString(2, searchText.trim());
            } else {
                cs.setNull(2, Types.VARCHAR);
            }
            if (fecha != null) {
                cs.setDate(3, java.sql.Date.valueOf(fecha));
            } else {
                cs.setNull(3, Types.DATE);
            }
            if (estado != null) {
                cs.setString(4, estado.name());
            } else {
                cs.setNull(4, Types.VARCHAR);
            }
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_count");
                }
            }
        }
        return 0;
    }

    @Override
    public List<CitaVeterinarioDTO> listarCitasPorVeterinarioPaginado(
            int empleadoId, String searchText, LocalDate fecha, Estado estado,
            int pageNumber, int recordsPerPage) throws SQLException {
        List<CitaVeterinarioDTO> citas = new ArrayList<>();
        String sql = "{CALL sp_listar_citas_veterinario_paginado(?, ?, ?, ?, ?, ?)}";
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, empleadoId);
            if (searchText != null && !searchText.isBlank()) {
                cs.setString(2, searchText.trim());
            } else {
                cs.setNull(2, Types.VARCHAR);
            }
            if (fecha != null) {
                cs.setDate(3, java.sql.Date.valueOf(fecha));
            } else {
                cs.setNull(3, Types.DATE);
            }
            if (estado != null) {
                cs.setString(4, estado.name());
            } else {
                cs.setNull(4, Types.VARCHAR);
            }
            cs.setInt(5, recordsPerPage);
            cs.setInt(6, pageNumber * recordsPerPage);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    CitaVeterinarioDTO dto = new CitaVeterinarioDTO();
                    dto.setCitaId(rs.getInt("cita_id"));
                    dto.setMascotaId(rs.getInt("mascota_id"));
                    dto.setNombreMascota(rs.getString("nombre_mascota"));
                    dto.setEspecie(rs.getString("especie"));
                    dto.setClienteId(rs.getInt("cliente_id"));
                    dto.setNombreCliente(rs.getString("nombre_cliente"));
                    LocalDateTime inicio = rs.getObject("fecha_hora_inicio", LocalDateTime.class);
                    LocalDateTime fin = rs.getObject("fecha_hora_fin", LocalDateTime.class);
                    dto.setFechaHoraInicio(inicio != null ? inicio.toString() : null);
                    dto.setFechaHoraFin(fin != null ? fin.toString() : null);
                    dto.setEstado(rs.getString("estado"));
                    dto.setMotivo(rs.getString("motivo"));
                    citas.add(dto);
                }
            }
        }
        return citas;
    }



    @Override
    public List<CitaListadoEmpleadoDTO> listarTodasCitas()
            throws SQLException {

        List<CitaListadoEmpleadoDTO> citas = new ArrayList<>();

        String sql = "{CALL listar_todas_citas()}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {

                CitaListadoEmpleadoDTO cita = new CitaListadoEmpleadoDTO();

                cita.setId(rs.getInt("cita_id"));
                cita.setEmpleadoId(rs.getInt("empleado_id"));
                cita.setMascotaId(rs.getInt("mascota_id"));

                cita.setNombreCliente(rs.getString("nombre_cliente"));
                cita.setNombreMascota(rs.getString("nombre_mascota"));
                cita.setNombreServicio(rs.getString("nombre_servicio"));

                LocalDateTime inicio = rs.getObject(
                        "fecha_hora_inicio", LocalDateTime.class);

                LocalDateTime fin = rs.getObject(
                        "fecha_hora_fin", LocalDateTime.class);

                cita.setFechaHoraInicio(
                        inicio != null ? inicio.toString() : null);

                cita.setFechaHoraFin(
                        fin != null ? fin.toString() : null);

                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));

                citas.add(cita);
            }
        }

        return citas;
    }

    @Override
public List<CitaReporteDTO> listarCitasReporte(
        LocalDate fechaInicio, LocalDate fechaFin, Estado estado) throws SQLException {
    List<CitaReporteDTO> lista = new ArrayList<>();
    String sql = "{CALL sp_listar_citas_reporte(?, ?, ?)}";

    try (Connection connection = DBManager.getInstance().getConnection();
         CallableStatement cs = connection.prepareCall(sql)) {

        if (fechaInicio != null) {
            cs.setDate(1, java.sql.Date.valueOf(fechaInicio));
        } else {
            cs.setNull(1, Types.DATE);
        }

        if (fechaFin != null) {
            cs.setDate(2, java.sql.Date.valueOf(fechaFin));
        } else {
            cs.setNull(2, Types.DATE);
        }

        if (estado != null) {
            cs.setString(3, estado.name());
        } else {
            cs.setNull(3, Types.VARCHAR);
        }

        try (ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                CitaReporteDTO dto = new CitaReporteDTO();
                dto.setCitaId(rs.getInt("cita_id"));
                LocalDateTime inicio = rs.getObject("fecha_hora_inicio", LocalDateTime.class);
                LocalDateTime fin = rs.getObject("fecha_hora_fin", LocalDateTime.class);
                dto.setFechaHoraInicio(inicio != null ? inicio.toString() : null);
                dto.setFechaHoraFin(fin != null ? fin.toString() : null);
                dto.setNombreSede(rs.getString("nombre_sede"));
                dto.setNombreMascota(rs.getString("nombre_mascota"));
                dto.setNombreVeterinario(rs.getString("nombre_veterinario"));
                dto.setNombreCliente(rs.getString("nombre_cliente"));
                dto.setMotivo(rs.getString("motivo"));
                dto.setEstado(rs.getString("estado"));
                lista.add(dto);
            }
        }
    }
    return lista;
}


}
