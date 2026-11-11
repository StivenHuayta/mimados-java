package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.VeterinarioResumen;
import java.sql.SQLException;
import java.util.List;

public interface VeterinarioBO {

    List<VeterinarioResumen> listarTodos()
            throws SQLException;
}