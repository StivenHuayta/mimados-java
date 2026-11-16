
package pe.pucp.progra3.mimados.dao.impl;

import pe.pucp.progra3.mimados.DBManager.DBManager;
import pe.pucp.progra3.mimados.dao.VeterinarioDAO;
import pe.pucp.progra3.mimados.model.dto.response.VeterinarioResumen;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAOImpl implements VeterinarioDAO {

    @Override
    public List<VeterinarioResumen> listarVeterinarios()
            throws SQLException {

        List<VeterinarioResumen> veterinarios = new ArrayList<>();

        String sql = "{CALL listar_veterinarios()}";

        try (Connection connection =
                     DBManager.getInstance().getConnection();
             CallableStatement cs = connection.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {

                VeterinarioResumen veterinario =
                        new VeterinarioResumen(
                                rs.getInt("empleadoId"),
                                rs.getString("nombre"),
                                rs.getInt("sedeId"),
                                rs.getString("sede"),
                                rs.getString("puesto")
                        );

                veterinarios.add(veterinario);
            }
        }

        return veterinarios;
    }
}
