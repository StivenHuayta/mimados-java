package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.MascotaDAO;
import pe.pucp.progra3.mimados.model.dto.response.AtencionHistorialDTO;
import pe.pucp.progra3.mimados.model.dto.response.PacienteVeterinarioDTO;
import pe.pucp.progra3.mimados.model.entity.Cliente;
import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.enums.Especie;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.enums.Sexo;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAOImpl implements MascotaDAO {

    @Override
    public void insertarMascota(Mascota mascota) throws SQLException {
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_mascota(?, ?, ?, ?, ?, ?, ?, ?)}")) {
            cs.registerOutParameter(8, Types.INTEGER);

            cs.setInt(1, mascota.getCliente().getUsuario().getId());
            cs.setString(2, mascota.getNombre());
            cs.setString(3, mascota.getEspecie() != null ? mascota.getEspecie().name() : null);
            cs.setString(4, mascota.getRaza());
            cs.setString(5, mascota.getSexo() != null ? mascota.getSexo().name() : null);

            if (mascota.getFechaNacimiento() != null) {
                cs.setDate(6, Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                cs.setNull(6, Types.DATE);
            }

            if (mascota.getPesoReferencial() != null) {
                cs.setBigDecimal(7, BigDecimal.valueOf(mascota.getPesoReferencial()));
            } else {
                cs.setNull(7, Types.DECIMAL);
            }

            cs.execute();

            int idGenerado = cs.getInt(8);
            mascota.setId(idGenerado);
        }
    }

    @Override
    public List<Mascota> listarMascotas() throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_mascotas()}");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("cliente_id"));
                cliente.setUsuario(usuario);

                Date sqlDate = rs.getDate("fecha_nacimiento");
                LocalDate fechaNac = sqlDate != null ? sqlDate.toLocalDate() : null;

                BigDecimal pesoBD = rs.getBigDecimal("peso_referencial");
                Double peso = pesoBD != null ? pesoBD.doubleValue() : null;

                String especieStr = rs.getString("especie");
                Especie especie = especieStr != null ? Especie.valueOf(especieStr.toUpperCase()) : null;

                String sexoStr = rs.getString("sexo");
                Sexo sexo = sexoStr != null ? Sexo.valueOf(sexoStr.toUpperCase()) : null;

                Mascota mascota = new Mascota(
                        rs.getInt("mascota_id"),
                        cliente,
                        rs.getString("nombre"),
                        especie,
                        rs.getString("raza"),
                        sexo,
                        fechaNac,
                        peso,
                        rs.getBoolean("activo")
                );
                mascotas.add(mascota);
            }
        }
        return mascotas;
    }

    @Override
    public Mascota mostrarMascota(int id) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_mascota(IN id int)
        SELECT cliente_id, nombre, especie, raza, sexo, fecha_nacimiento, peso_referencial, activo
        FROM mascota WHERE mascota_id=id;
        */
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_mascota(?)}")) {

            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("cliente_id"));
                    cliente.setUsuario(usuario);

                    Date sqlDate = rs.getDate("fecha_nacimiento");
                    LocalDate fechaNac = sqlDate != null ? sqlDate.toLocalDate() : null;

                    BigDecimal pesoBD = rs.getBigDecimal("peso_referencial");
                    Double peso = pesoBD != null ? pesoBD.doubleValue() : null;

                    String especieStr = rs.getString("especie");
                    Especie especie = especieStr != null ? Especie.valueOf(especieStr.toUpperCase()) : null;

                    String sexoStr = rs.getString("sexo");
                    Sexo sexo = sexoStr != null ? Sexo.valueOf(sexoStr.toUpperCase()) : null;

                    return new Mascota(
                            id,
                            cliente,
                            rs.getString("nombre"),
                            especie,
                            rs.getString("raza"),
                            sexo,
                            fechaNac,
                            peso,
                            rs.getBoolean("activo")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Mascota> mostrarMascotasDueno(int idCliente) throws SQLException {

        List<Mascota> mascotas = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_mascotas_dueno(?)}")) {

            cs.setInt(1, idCliente);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    Usuario usuario = new Usuario();
                    usuario.setId(idCliente);
                    cliente.setUsuario(usuario);

                    Date sqlDate = rs.getDate("fecha_nacimiento");
                    LocalDate fechaNac = sqlDate != null ? sqlDate.toLocalDate() : null;

                    BigDecimal pesoBD = rs.getBigDecimal("peso_referencial");
                    Double peso = pesoBD != null ? pesoBD.doubleValue() : null;

                    String especieStr = rs.getString("especie");
                    Especie especie = especieStr != null ? Especie.valueOf(especieStr.toUpperCase()) : null;

                    String sexoStr = rs.getString("sexo");
                    Sexo sexo = sexoStr != null ? Sexo.valueOf(sexoStr.toUpperCase()) : null;

                    Mascota mascota = new Mascota(
                            rs.getInt("mascota_id"),
                            cliente,
                            rs.getString("nombre"),
                            especie,
                            rs.getString("raza"),
                            sexo,
                            fechaNac,
                            peso,
                            rs.getBoolean("activo")
                    );
                    mascotas.add(mascota);
                }
            }
        }
        return mascotas;
    }

    @Override
    public void actualizarMascotaNombre(int id, String nuevoNombre) throws SQLException {

        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_mascota_nombre(?, ?)}")) {
            cs.setInt(1, id);
            cs.setString(2, nuevoNombre);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la mascota con ID " + id);
            }
        }
    }

    @Override
    public void eliminarMascota(int id) throws SQLException {
        /*
        CREATE PROCEDURE eliminar_mascota(IN id int)
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL eliminar_mascota(?)}")) {
            cs.setInt(1, id);

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la mascota con ID " + id);
            }
        }
    }


    @Override
    public List<RegistroMedicoDTO> listarHistorialPorMascota(int mascotaId) {
        List<RegistroMedicoDTO> historial = new ArrayList<>();

        String sql = "{CALL sp_obtener_historial_mascota(?)}";

        try (Connection conn = DBManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, mascotaId);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    RegistroMedicoDTO reg = new RegistroMedicoDTO();

                    reg.setFecha(rs.getString("Fecha"));
                    reg.setVeterinario(rs.getString("Veterinario"));
                    reg.setServicio(rs.getString("Servicio"));
                    reg.setNotas(rs.getString("Notas"));
                    reg.setPeso(rs.getBigDecimal("Peso"));

                    historial.add(reg);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al invocar SP de historial: " + ex.getMessage());
        }

        return historial;
    }

    @Override
    public int contarPacientesVeterinario(String searchText) throws SQLException {
        String sql = "{CALL sp_contar_pacientes_veterinario(?)}";
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            if (searchText != null && !searchText.isBlank()) {
                cs.setString(1, searchText.trim());
            } else {
                cs.setNull(1, Types.VARCHAR);
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
    public List<PacienteVeterinarioDTO> listarPacientesVeterinarioPaginado(
            String searchText, int pageNumber, int recordsPerPage) throws SQLException {

        List<PacienteVeterinarioDTO> pacientes = new ArrayList<>();
        String sql = "{CALL sp_listar_pacientes_veterinario_paginado(?, ?, ?)}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            if (searchText != null && !searchText.isBlank()) {
                cs.setString(1, searchText.trim());
            } else {
                cs.setNull(1, Types.VARCHAR);
            }

            cs.setInt(2, recordsPerPage);
            cs.setInt(3, pageNumber * recordsPerPage);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    PacienteVeterinarioDTO dto = new PacienteVeterinarioDTO();
                    dto.setMascotaId(rs.getInt("mascota_id"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setEspecie(rs.getString("especie"));
                    dto.setRaza(rs.getString("raza"));
                    dto.setSexo(rs.getString("sexo"));

                    Date fechaNac = rs.getDate("fecha_nacimiento");
                    dto.setFechaNacimiento(fechaNac != null ? fechaNac.toLocalDate().toString() : null);

                    BigDecimal pesoBD = rs.getBigDecimal("peso_referencial");
                    dto.setPesoReferencial(pesoBD != null ? pesoBD.doubleValue() : null);

                    dto.setClienteId(rs.getInt("cliente_id"));
                    dto.setNombreCliente(rs.getString("nombre_cliente"));
                    dto.setDniCliente(rs.getString("dni_cliente"));
                    dto.setTelefonoCliente(rs.getString("telefono_cliente"));
                    dto.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
                    dto.setDireccionCliente(rs.getString("direccion_cliente"));
                    pacientes.add(dto);
                }
            }
        }
        return pacientes;
    }

    @Override
    public List<AtencionHistorialDTO> listarHistorialClinicoVeterinario(int mascotaId) throws SQLException {
        List<AtencionHistorialDTO> historial = new ArrayList<>();
        String sql = "{CALL sp_obtener_historial_clinico_veterinario(?)}";

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setInt(1, mascotaId);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    AtencionHistorialDTO dto = new AtencionHistorialDTO();
                    dto.setAtencionId(rs.getInt("atencion_id"));
                    dto.setCitaId(rs.getInt("cita_id"));

                    java.sql.Timestamp ts = rs.getTimestamp("fecha_hora_registro");
                    dto.setFechaHoraRegistro(ts != null ? ts.toLocalDateTime().toString() : null);

                    BigDecimal pesoBD = rs.getBigDecimal("peso_actual");
                    dto.setPesoActual(pesoBD != null ? pesoBD.doubleValue() : null);

                    BigDecimal tempBD = rs.getBigDecimal("temperatura");
                    dto.setTemperatura(tempBD != null ? tempBD.doubleValue() : null);

                    dto.setSintomas(rs.getString("sintomas"));
                    dto.setDiagnostico(rs.getString("diagnostico"));
                    dto.setTratamientoRecetado(rs.getString("tratamiento_recetado"));
                    dto.setObservaciones(rs.getString("observaciones"));
                    dto.setMontoTotal(rs.getBigDecimal("monto_total"));
                    dto.setServicioRealizado(rs.getString("servicio_realizado"));
                    dto.setNombreVeterinario(rs.getString("nombre_veterinario"));
                    dto.setCodigoCmvp(rs.getString("codigo_cmvp"));
                    historial.add(dto);
                }
            }
        }
        return historial;
    }
}
