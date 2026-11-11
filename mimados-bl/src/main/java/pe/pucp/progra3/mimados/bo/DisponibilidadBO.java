
package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DisponibilidadBO {

    List<LocalTime> consultarDisponibilidad(
            int idEmpleado,
            LocalDate fecha,
            int duracionMinutos
    ) throws SQLException, NegocioException;
}
