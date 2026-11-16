package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.dto.response.VeterinarioResumen;
import java.util.List;

public interface VeterinarioBO {

    List<VeterinarioResumen> listarTodos();
}