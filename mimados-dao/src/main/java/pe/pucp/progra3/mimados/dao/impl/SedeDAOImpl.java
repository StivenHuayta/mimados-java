package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.dao.SedeDAO;
import pe.pucp.progra3.mimados.model.entity.Sede;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SedeDAOImpl implements SedeDAO {

    @Override
    public List<Sede> listarSedes() throws SQLException {

        List<Sede> sedes = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_sedes()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                Sede sede = new Sede(
                        rs.getInt("sede_id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getBoolean("activo")
                );
                sedes.add(sede);
            }
        }
        return sedes;
    }


    @Override
    public Sede mostrarSede(int idSede) throws SQLException {
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL mostrar_sede( ? )}");){

            cs.setInt(1, idSede);
            try (ResultSet rs = cs.executeQuery()) {
                if(rs.next()){
                    Sede sede = new Sede(
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getBoolean("activo")
                    );
                    return sede;
                }
            }
            return null;
        }
    }


    @Override
    public void modificarDireccion(int idSede, String dir) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_direccion(?,?)}")) {

            cs.setInt(1, idSede);
            cs.setString(2, dir);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la sede con ID " + idSede);
            }

        }

    }

    @Override
    public void modificarSede(Sede sede) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_sede(?,?,?,?,?)}")) {

            cs.setInt(1 , sede.getId());
            cs.setString(2,sede.getNombre());
            cs.setString(3,sede.getDireccion());
            cs.setString(4,sede.getTelefono());
            cs.setBoolean(5,sede.getActivo());

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la sede con ID " + sede.getId());
            }

        }
    }

    @Override
    public void agregarSede(Sede sede) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL insertar_sede(?,?,?,?)}")){

            cs.registerOutParameter(4 , Types.INTEGER);

            cs.setString(1 , sede.getNombre());
            cs.setString(2, sede.getDireccion());
            cs.setString(3, sede.getTelefono());

            cs.execute();

            int id_generado = cs.getInt(4);
            sede.setId(id_generado);
        }
    }


    @Override
    public void eliminarSede(int idSede) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_sede(?)}")){

            cs.setInt(1, idSede);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró la sede con ID " + idSede);
            }
        }


    }
}
