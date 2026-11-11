
package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.bo.VeterinarioBO;
import pe.pucp.progra3.mimados.dao.VeterinarioDAO;
import pe.pucp.progra3.mimados.dao.imp.VeterinarioDAOimp;
import pe.pucp.progra3.mimados.model.VeterinarioResumen;

import java.sql.SQLException;
import java.util.List;

public class VeterinarioBOImpl implements VeterinarioBO {

    private final VeterinarioDAO veterinarioDAO;

    public VeterinarioBOImpl() {
        this.veterinarioDAO = new VeterinarioDAOimp();
    }

    @Override
    public List<VeterinarioResumen> listarTodos()
            throws SQLException {

        return veterinarioDAO.listar_veterinarios();
    }
}
