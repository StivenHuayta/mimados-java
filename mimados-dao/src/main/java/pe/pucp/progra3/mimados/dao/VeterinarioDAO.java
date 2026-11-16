package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.dto.response.VeterinarioResumen;
import java.sql.SQLException;
import java.util.List;

public interface VeterinarioDAO {

    List<VeterinarioResumen> listarVeterinarios()
            throws SQLException;
}