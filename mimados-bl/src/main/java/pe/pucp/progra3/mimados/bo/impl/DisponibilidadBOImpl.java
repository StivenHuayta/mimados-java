
package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.bo.DisponibilidadBO;
import pe.pucp.progra3.mimados.bo.HorarioLaboralBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.dao.impl.CitaDAOImpl;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.enums.DiaSemana;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class DisponibilidadBOImpl implements DisponibilidadBO {

    private final HorarioLaboralBO horarioBO;
    private final CitaDAO citaDAO;

    public DisponibilidadBOImpl() {
        this.horarioBO = new HorarioLaboralBOImpl();
        this.citaDAO = new CitaDAOImpl();
    }

    @Override
    public List<LocalTime> consultarDisponibilidad(
            int idEmpleado,
            LocalDate fecha,
            int duracionMinutos
    ) {

        // Validaciones
        if (idEmpleado <= 0) {
            throw new NegocioException(
                    "El ID del empleado no es valido."
            );
        }

        if (fecha == null) {
            throw new NegocioException(
                    "La fecha es obligatoria."
            );
        }

        if (duracionMinutos <= 0 || duracionMinutos > 240) {
            throw new NegocioException(
                    "La duracion debe estar entre 1 y 240 minutos."
            );
        }

        if (fecha.isBefore(LocalDate.now())) {
            return new ArrayList<>();
        }

        // Consultar horarios laborales del empleado
        List<HorarioLaboral> horarios =
                horarioBO.listarPorEmpleado(idEmpleado);

        // Consultar citas ocupadas en esa fecha
        List<Cita> citasOcupadas;
        try {
            citasOcupadas = citaDAO.listarCitasPorEmpleadoFecha(
                    idEmpleado, fecha
            );
        } catch (SQLException e) {
            throw new NegocioException("Error al consultar la disponibilidad.", e);
        }

        // Obtener el dia de la semana
        DiaSemana dia =
                DiaSemana.values()[
                        fecha.getDayOfWeek().getValue() - 1
                        ];

        // Almacenar horas libres sin duplicados
        TreeSet<LocalTime> horasLibres = new TreeSet<>();

        for (HorarioLaboral horario : horarios) {

            // Verificar que el horario este activo
            if (!Boolean.TRUE.equals(horario.getActivo())) {
                continue;
            }

            // Verificar que corresponda al dia consultado
            if (horario.getDiaSemana() != dia) {
                continue;
            }

            if (horario.getHoraInicio() == null ||
                    horario.getHoraFin() == null) {
                continue;
            }

            LocalDateTime inicioTurno =
                    fecha.atTime(horario.getHoraInicio());

            LocalDateTime finJornada =
                    fecha.atTime(horario.getHoraFin());

            // Recorrer los posibles turnos
            while (!inicioTurno.plusMinutes(duracionMinutos)
                    .isAfter(finJornada)) {

                LocalDateTime finTurno =
                        inicioTurno.plusMinutes(duracionMinutos);

                boolean ocupado = false;

                // Comprobar cruces con citas existentes
                for (Cita cita : citasOcupadas) {

                    if (cita.getFechaHoraInicio() == null ||
                            cita.getFechaHoraFin() == null) {
                        continue;
                    }

                    boolean seCruzan =
                            inicioTurno.isBefore(
                                    cita.getFechaHoraFin()
                            )
                                    &&
                                    finTurno.isAfter(
                                            cita.getFechaHoraInicio()
                                    );

                    if (seCruzan) {
                        ocupado = true;
                        break;
                    }
                }

                // Agregar el turno si esta libre y es futuro
                if (!ocupado &&
                        inicioTurno.isAfter(LocalDateTime.now())) {

                    horasLibres.add(inicioTurno.toLocalTime());
                }

                // Avanzar al siguiente turno
                inicioTurno =
                        inicioTurno.plusMinutes(duracionMinutos);
            }
        }

        return new ArrayList<>(horasLibres);
    }
}
