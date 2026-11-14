
package pe.pucp.progra3.mimados.bo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DisponibilidadBO {

    List<LocalTime> consultarDisponibilidad(
            int idEmpleado,
            LocalDate fecha,
            int duracionMinutos
    );
}
