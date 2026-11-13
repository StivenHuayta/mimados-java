package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.MascotaDAO;
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

public class MascotaDAOimp implements MascotaDAO {

    @Override
    public void insertar_mascota(Mascota mascota) throws SQLException {
        /*
        CREATE PROCEDURE insertar_mascota(
            IN p_id INT, 
            IN p_nombre VARCHAR(45), 
            IN p_especie VARCHAR(45), 
            IN p_raza VARCHAR(45), 
            IN p_sexo VARCHAR(45), 
            IN p_fecha_nacimiento DATE, 
            IN p_peso_referencial DECIMAL(5,2),
            OUT mascota_id INT
        )
        */
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
    public List<Mascota> listar_mascotas() throws SQLException {
        /*
        CREATE PROCEDURE listar_mascotas ()
        SELECT mascota_id, cliente_id, nombre, especie, raza, sexo, fecha_nacimiento, peso_referencial, activo
        FROM mascota;
        */
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
    public Mascota mostrar_mascota(int id) throws SQLException {
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
    public List<Mascota> mostrar_mascotas_dueno(int idCliente) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_mascotas_dueno(IN id int)
        SELECT mascota_id, nombre, especie, raza, sexo, fecha_nacimiento, peso_referencial, activo
        FROM mascota WHERE cliente_id=id;
        */
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
    public void actualizar_mascota_nombre(int id, String nuevoNombre) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_mascota_nombre(IN id int, IN nuevo_nom varchar(60))
        */
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
    public void eliminar_mascota(int id) throws SQLException {
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

        // Sintaxis estándar de JDBC para llamar a un procedimiento almacenado
        String sql = "{CALL sp_obtener_historial_mascota(?)}";

        try (Connection conn = DBManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // Le pasamos el ID de la mascota al procedimiento
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


}
