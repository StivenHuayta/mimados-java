
package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.bo.VeterinarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.VeterinarioDAO;
import pe.pucp.progra3.mimados.dao.impl.VeterinarioDAOImpl;
import pe.pucp.progra3.mimados.model.dto.response.VeterinarioResumen;

import java.sql.SQLException;
import java.util.List;

public class VeterinarioBOImpl implements VeterinarioBO {

    private final VeterinarioDAO veterinarioDAO;

    public VeterinarioBOImpl() {
        this.veterinarioDAO = new VeterinarioDAOImpl();
    }

    @Override
    public List<VeterinarioResumen> listarTodos() {

        try {
            return veterinarioDAO.listarVeterinarios();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los veterinarios.", e);
        }
    }
}
