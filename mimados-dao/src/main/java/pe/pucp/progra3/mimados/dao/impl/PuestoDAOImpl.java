package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.PuestoDAO;
import pe.pucp.progra3.mimados.model.entity.Puesto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PuestoDAOImpl implements PuestoDAO {

    @Override
    public void insertarPuesto(Puesto puesto) throws SQLException {
        /*
        CREATE PROCEDURE insertar_puesto(
            IN p_nombre VARCHAR(45),
            IN p_descripcion VARCHAR(200)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL insertar_puesto(?, ?)}")) {
            cs.setString(1, puesto.getNombre());
            cs.setString(2, puesto.getDescripcion());

            cs.execute();
        }
    }

    @Override
    public List<Puesto> listarPuestos() throws SQLException {
        /*
        CREATE PROCEDURE listar_puestos()
        SELECT puesto_id, nombre, descripcion FROM puesto;
        */
        List<Puesto> puestos = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL listar_puestos()}");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Puesto puesto = new Puesto(
                        rs.getInt("puesto_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                puestos.add(puesto);
            }
        }
        return puestos;
    }

    @Override
    public Puesto mostrarPuesto(int idPuesto) throws SQLException {
        /*
        CREATE PROCEDURE mostrar_puesto(IN p_puesto_id INT)
        SELECT puesto_id, nombre, descripcion FROM puesto WHERE puesto_id = p_puesto_id;
        */
        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall("{CALL mostrar_puesto(?)}")) {

            cs.setInt(1, idPuesto);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new Puesto(
                            rs.getInt("puesto_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void actualizarPuesto(Puesto puesto) throws SQLException {
        /*
        CREATE PROCEDURE actualizar_puesto(
            IN p_puesto_id INT,
            IN p_nombre VARCHAR(45),
            IN p_descripcion VARCHAR(200)
        )
        */
        Connection connection = TransactionContext.getConnection();
        try (CallableStatement cs = connection.prepareCall("{CALL actualizar_puesto(?, ?, ?)}")) {
            cs.setInt(1, puesto.getId());
            cs.setString(2, puesto.getNombre());
            cs.setString(3, puesto.getDescripcion());

            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el puesto con ID " + puesto.getId());
            }
        }
    }
}
