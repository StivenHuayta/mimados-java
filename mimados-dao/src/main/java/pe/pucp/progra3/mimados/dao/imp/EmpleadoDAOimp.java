package pe.pucp.progra3.mimados.dao.imp;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.dao.EmpleadoDAO;
import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.model.Empleado;
import pe.pucp.progra3.mimados.model.Sede;
import pe.pucp.progra3.mimados.model.Usuario;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOimp implements EmpleadoDAO {

    @Override
    public List<Empleado> listar_empleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall(" {CALL listar_empleados()}");
            ResultSet rs = cs.executeQuery()){

            while(rs.next()){

                Empleado empleado = new Empleado(
                        rs.getInt("usuario_id"),
                       rs.getString("dni"),
                       rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono"),

                        rs.getInt("sede_id"),
                        rs.getString("codigo_cmvp"),
                        rs.getBoolean("activo"),
                        rs.getBoolean("es_admin")
                );

                empleados.add(empleado);
            }
        }
        return empleados;

    }

    @Override
    public void insertar_empleado(Empleado empleado) throws SQLException {
        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL insertar_empleado(?,?,?)}")){


            cs.setInt(1 , empleado.getSede().getId());
            cs.setInt(2, empleado.getUsuario().getId());
            cs.setString(3, empleado.getCodigoCmvp());

            cs.execute();

        }
    }

    @Override
    public void eliminar_empleado(int id_empleado) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL eliminar_empleado(?)}")){

            cs.setInt(1 , id_empleado);

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el empleado con ID " + id_empleado);
            }
        }

    }


    @Override
    public Empleado mostrar_empleado(int id_empleado) throws SQLException {

        try (Connection connection = DBManager.getInstance().getConnection();
             CallableStatement cs =
                     connection.prepareCall("{CALL mostrar_empleado(?)}")) {

            cs.setInt(1, id_empleado);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {

                    Empleado emp = new Empleado(
                            rs.getString("dni"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getInt("sede_id"),
                            rs.getString("codigo_cmvp"),
                            rs.getBoolean("activo"),
                            rs.getBoolean("es_admin")
                    );

                    emp.getUsuario().setId(id_empleado);

                    return emp;
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar_empleado(Empleado empleado) throws SQLException {

        Connection connection = TransactionContext.getConnection();

        try(CallableStatement cs = connection.prepareCall("{CALL actualizar_empleado(?,?,?,?,?)}")) {

            cs.setInt(1 , empleado.getUsuario().getId());
            cs.setInt(2,empleado.getSede().getId());
            cs.setString(3, empleado.getCodigoCmvp());
            cs.setBoolean(4, empleado.getActivo());
            cs.setBoolean(5 , empleado.getEsAdmin());

            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró el empleado con ID " + empleado.getUsuario().getId());
            }

        }



    }
}
