package pe.pucp.progra3.mimados.dao;

import pe.pucp.progra3.mimados.model.VeterinarioResumen;
import java.sql.SQLException;
import java.util.List;

public interface VeterinarioDAO {

    List<VeterinarioResumen> listar_veterinarios()
            throws SQLException;
}