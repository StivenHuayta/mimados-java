
package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.CitaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.dao.impl.CitaDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.CitaServicioDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.SedeServicioDAOImpl;
import pe.pucp.progra3.mimados.model.dto.response.CitaListadoEmpleadoDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.PageResultDTO;
import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.CitaServicio;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.enums.Estado;
import pe.pucp.progra3.mimados.model.enums.DiaSemana;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class CitaBOImpl implements CitaBO {

    private final CitaDAO citaDAO;

    public CitaBOImpl() {
        this.citaDAO = new CitaDAOImpl();
    }

    @Override
    public void insertar(Cita cita) {

        validarCita(cita);

        try {
            citaDAO.insertarCita(cita);
            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar la cita.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Cita> listarPorMascota(int idMascota) {

        validarId(idMascota);

        try {
            return citaDAO.listarCitasPorMascota(idMascota);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar las citas de la mascota.", e);
        }
    }

    @Override
    public List<CitaListadoEmpleadoDTO> listarTodas() {

        try {
            return citaDAO.listarTodasCitas();

        } catch (SQLException e) {
            throw new NegocioException(
                    "Error al listar todas las citas.", e);
        }
    }

    @Override
    public Cita obtenerPorId(int id) {

        validarId(id);

        try {
            return citaDAO.mostrarCita(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener la cita.", e);
        }
    }

    @Override
    public void cancelar(int idCita) {

        validarId(idCita);

        try {
            citaDAO.cancelarCita(idCita);
            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al cancelar la cita.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void atender(int idCita) {

        validarId(idCita);

        try {
            citaDAO.atenderCita(idCita);
            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al atender la cita.", e);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarCita(Cita cita) {

        if (cita == null) {
            throw new NegocioException(
                    "La cita no puede ser nula."
            );
        }

        if (cita.getEmpleado() == null ||
                cita.getEmpleado().getUsuario() == null) {
            throw new NegocioException(
                    "La cita debe tener un empleado."
            );
        }

        validarId(
                cita.getEmpleado().getUsuario().getId()
        );

        if (cita.getMascota() == null) {
            throw new NegocioException(
                    "La cita debe tener una mascota."
            );
        }

        validarId(cita.getMascota().getId());

        if (cita.getFechaHoraInicio() == null ||
                cita.getFechaHoraFin() == null) {
            throw new NegocioException(
                    "Las fechas de inicio y fin son obligatorias."
            );
        }

        if (!cita.getFechaHoraInicio()
                .isBefore(cita.getFechaHoraFin())) {
            throw new NegocioException(
                    "La fecha y hora de inicio debe ser anterior a la de fin."
            );
        }

        if (cita.getMotivo() != null &&
                cita.getMotivo().length() > 255) {
            throw new NegocioException(
                    "El motivo no puede superar los 255 caracteres."
            );
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }

    @Override
    public void modificar(Cita cita) {

        if (cita == null) {
            throw new NegocioException("La cita no puede ser nula.");
        }

        validarId(cita.getId());
        validarCita(cita);

        Cita existente;
        try {
            existente = citaDAO.mostrarCita(cita.getId());
        } catch (SQLException e) {
            throw new NegocioException("Error al consultar la cita.", e);
        }

        if (existente == null) {
            throw new NegocioException("La cita no existe.");
        }

        if (existente.getEstado() != Estado.PROGRAMADA &&
                existente.getEstado() != Estado.CONFIRMADA) {
            throw new NegocioException(
                    "La cita no puede modificarse en su estado actual."
            );
        }

        if (!Objects.equals(
                existente.getMascota().getId(),
                cita.getMascota().getId())) {
            throw new NegocioException(
                    "No se puede cambiar la mascota de la cita."
            );
        }

        // Comprobar si cambiaron las fechas o el veterinario
        boolean cambioHorario =
                !Objects.equals(
                        existente.getFechaHoraInicio(),
                        cita.getFechaHoraInicio()
                ) ||
                        !Objects.equals(
                                existente.getFechaHoraFin(),
                                cita.getFechaHoraFin()
                        ) ||
                        !Objects.equals(
                                existente.getEmpleado().getUsuario().getId(),
                                cita.getEmpleado().getUsuario().getId()
                        );

        // Validar disponibilidad solo si se modifica el horario
        if (cambioHorario) {

            if (!cita.getFechaHoraInicio().isAfter(LocalDateTime.now())) {
                throw new NegocioException(
                        "La fecha y hora deben ser futuras."
                );
            }

            validarDisponibilidadEdicion(cita);
        }

        try {
            citaDAO.actualizarCita(cita);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar la cita.", e);

        } catch (RuntimeException e) {
            TransactionContext.rollback();
            throw e;

        } finally {
            TransactionContext.close();
        }
    }

    // Valida el horario del veterinario al modificar una cita
    private void validarDisponibilidadEdicion(Cita cita) {

        int empleadoId = cita.getEmpleado().getUsuario().getId();

        LocalDateTime inicio = cita.getFechaHoraInicio();
        LocalDateTime fin = cita.getFechaHoraFin();

        long duracion = Duration.between(inicio, fin).toMinutes();

        // Validar la duracion
        if (duracion <= 0 || duracion > 240 ||
                !inicio.plusMinutes(duracion).equals(fin)) {
            throw new NegocioException(
                    "La duración de la cita no es válida."
            );
        }

        if (!inicio.toLocalDate().equals(fin.toLocalDate())) {
            throw new NegocioException(
                    "La cita debe empezar y terminar el mismo día."
            );
        }

        // Obtener los horarios laborales del veterinario
        List<HorarioLaboral> horarios =
                new HorarioLaboralBOImpl().listarPorEmpleado(empleadoId);

        DiaSemana dia = DiaSemana.values()[
                inicio.getDayOfWeek().getValue() - 1
                ];

        boolean horarioValido = false;

        // Verificar que la cita este dentro de la jornada laboral
        for (HorarioLaboral horario : horarios) {

            if (!Boolean.TRUE.equals(horario.getActivo()) ||
                    horario.getDiaSemana() != dia ||
                    horario.getHoraInicio() == null ||
                    horario.getHoraFin() == null) {
                continue;
            }

            LocalDateTime inicioJornada =
                    inicio.toLocalDate().atTime(horario.getHoraInicio());

            LocalDateTime finJornada =
                    inicio.toLocalDate().atTime(horario.getHoraFin());

            if (inicio.isBefore(inicioJornada) ||
                    fin.isAfter(finJornada)) {
                continue;
            }

            long diferenciaSegundos =
                    Duration.between(inicioJornada, inicio).getSeconds();

            // Respetar los intervalos de duracion de la cita
            if (diferenciaSegundos >= 0 &&
                    diferenciaSegundos % (duracion * 60) == 0) {
                horarioValido = true;
                break;
            }
        }

        if (!horarioValido) {
            throw new NegocioException(
                    "El veterinario no tiene un horario laboral disponible en esa fecha y hora."
            );
        }

        // Consultar las citas que ya tiene el veterinario
        List<Cita> citasOcupadas;

        try {
            citasOcupadas = citaDAO.listarCitasPorEmpleadoFecha(
                    empleadoId,
                    inicio.toLocalDate()
            );
        } catch (SQLException e) {
            throw new NegocioException(
                    "Error al consultar las citas ocupadas.", e
            );
        }

        for (Cita ocupada : citasOcupadas) {

            // No considerar la misma cita que se esta editando
            if (Objects.equals(ocupada.getId(), cita.getId()) ||
                    ocupada.getEstado() == Estado.CANCELADA ||
                    ocupada.getFechaHoraInicio() == null ||
                    ocupada.getFechaHoraFin() == null) {
                continue;
            }

            // Comprobar si ambas citas se cruzan
            boolean seCruzan =
                    inicio.isBefore(ocupada.getFechaHoraFin()) &&
                            fin.isAfter(ocupada.getFechaHoraInicio());

            if (seCruzan) {
                throw new NegocioException(
                        "El veterinario ya tiene otra cita en ese horario."
                );
            }
        }
    }

    @Override
    public void insertarConServicio(
            Cita cita,
            int idSede,
            int idSedeServicio) {

        validarCita(cita);
        validarId(idSede);
        validarId(idSedeServicio);

        try {
            SedeServicioDTO seleccionado = new SedeServicioDAOImpl()
                    .listarServiciosPorSede(idSede).stream()
                    .filter(s -> s.getIdSedeServicio() != null
                            && s.getIdSedeServicio() == idSedeServicio)
                    .findFirst().orElse(null);

            if (seleccionado == null ||
                    !Boolean.TRUE.equals(seleccionado.getActivo())) {
                throw new NegocioException(
                        "El servicio no está activo en la sede seleccionada."
                );
            }

            if (seleccionado.getDuracionMinutos() == null ||
                    seleccionado.getDuracionMinutos() <= 0) {
                throw new NegocioException(
                        "El servicio no tiene duración válida."
                );
            }

            if (seleccionado.getPrecioReferencial() == null) {
                throw new NegocioException(
                        "El servicio no tiene precio referencial definido."
                );
            }

            long minutos = Duration.between(
                    cita.getFechaHoraInicio(),
                    cita.getFechaHoraFin()
            ).toMinutes();

            if (minutos != seleccionado.getDuracionMinutos()) {
                throw new NegocioException(
                        "La duración de la cita no coincide con el servicio."
                );
            }

            if (!cita.getFechaHoraInicio().isAfter(LocalDateTime.now())) {
                throw new NegocioException(
                        "La fecha y hora deben ser futuras."
                );
            }

            // Revalidar disponibilidad antes de registrar
            List<LocalTime> horasLibres = new DisponibilidadBOImpl()
                    .consultarDisponibilidad(
                            cita.getEmpleado().getUsuario().getId(),
                            cita.getFechaHoraInicio().toLocalDate(),
                            seleccionado.getDuracionMinutos()
                    );

            if (!horasLibres.contains(
                    cita.getFechaHoraInicio().toLocalTime())) {
                throw new NegocioException(
                        "El horario seleccionado no está disponible."
                );
            }

            citaDAO.insertarCita(cita);

            if (cita.getId() == null || cita.getId() <= 0) {
                throw new NegocioException(
                        "No se obtuvo el ID de la cita registrada."
                );
            }

            SedeServicio sedeServicio = new SedeServicio();
            sedeServicio.setId(idSedeServicio);

            CitaServicio item = new CitaServicio();
            item.setCita(cita);
            item.setSedeServicio(sedeServicio);
            item.setPrecioAplicado(seleccionado.getPrecioReferencial());
            item.setNotas(null);

            new CitaServicioDAOImpl().insertarCitaServicio(item);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException(
                    "Error al registrar la cita con servicio.", e
            );

        } catch (RuntimeException e) {
            TransactionContext.rollback();
            throw e;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public PageResultDTO<CitaVeterinarioDTO> listarPorVeterinarioPaginado(
            int empleadoId,
            String searchText,
            LocalDate fecha,
            Estado estado,
            int pageNumber,
            int recordsPerPage) {

        validarId(empleadoId);

        int pagina = Math.max(0, pageNumber);
        int tamano = recordsPerPage <= 0 ? 10 : recordsPerPage;

        try {
            int totalCount = citaDAO.contarCitasPorVeterinario(
                    empleadoId,
                    searchText,
                    fecha,
                    estado
            );

            List<CitaVeterinarioDTO> page =
                    citaDAO.listarCitasPorVeterinarioPaginado(
                            empleadoId,
                            searchText,
                            fecha,
                            estado,
                            pagina,
                            tamano
                    );

            return new PageResultDTO<>(totalCount, page);

        } catch (SQLException e) {
            throw new NegocioException(
                    "Error al listar las citas del veterinario.", e
            );
        }
    }
}
