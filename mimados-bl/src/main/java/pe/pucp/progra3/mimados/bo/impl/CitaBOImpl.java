package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.CitaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.dao.impl.CitaDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.CitaServicioDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.SedeServicioDAOImpl;
import pe.pucp.progra3.mimados.model.dto.response.CitaListadoEmpleadoDTO;
import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.CitaServicio;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.enums.Estado;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
            throw new NegocioException(
                    "La cita no puede ser nula."
            );
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
            throw new NegocioException(
                    "La cita no existe."
            );
        }

        if (existente.getEstado() != Estado.PROGRAMADA &&
                existente.getEstado() != Estado.CONFIRMADA) {
            throw new NegocioException(
                    "La cita no puede modificarse en su estado actual."
            );
        }

        if (!existente.getMascota().getId()
                .equals(cita.getMascota().getId())) {
            throw new NegocioException(
                    "No se puede cambiar la mascota de la cita."
            );
        }

        try {
            citaDAO.actualizarCita(cita);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar la cita.", e);

        } finally {
            TransactionContext.close();
        }
    }
    @Override
    public void insertarConServicio(Cita cita, int idSede, int idSedeServicio) {
        validarCita(cita);
        validarId(idSede);
        validarId(idSedeServicio);

        try {
            SedeServicioDTO seleccionado = new SedeServicioDAOImpl()
                    .listarServiciosPorSede(idSede).stream()
                    .filter(s -> s.getIdSedeServicio() != null
                            && s.getIdSedeServicio() == idSedeServicio)
                    .findFirst().orElse(null);

            if (seleccionado == null || !Boolean.TRUE.equals(seleccionado.getActivo())) {
                throw new NegocioException("El servicio no está activo en la sede seleccionada.");
            }
            if (seleccionado.getDuracionMinutos() == null || seleccionado.getDuracionMinutos() <= 0) {
                throw new NegocioException("El servicio no tiene duración válida.");
            }
            if (seleccionado.getPrecioReferencial() == null) {
                throw new NegocioException("El servicio no tiene precio referencial definido.");
            }
            long minutos = Duration.between(cita.getFechaHoraInicio(),
                    cita.getFechaHoraFin()).toMinutes();
            if (minutos != seleccionado.getDuracionMinutos()) {
                throw new NegocioException("La duración de la cita no coincide con el servicio.");
            }
            if (!cita.getFechaHoraInicio().isAfter(LocalDateTime.now())) {
                throw new NegocioException("La fecha y hora deben ser futuras.");
            }

            // Revalidación de disponibilidad en Java antes de escribir en MySQL.
            List<LocalTime> horasLibres = new DisponibilidadBOImpl()
                    .consultarDisponibilidad(
                            cita.getEmpleado().getUsuario().getId(),
                            cita.getFechaHoraInicio().toLocalDate(),
                            seleccionado.getDuracionMinutos());
            if (!horasLibres.contains(cita.getFechaHoraInicio().toLocalTime())) {
                throw new NegocioException("El horario seleccionado no está disponible.");
            }

            // Ambas inserciones comparten TransactionContext.
            citaDAO.insertarCita(cita);
            if (cita.getId() == null || cita.getId() <= 0) {
                throw new NegocioException("No se obtuvo el ID de la cita registrada.");
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
            throw new NegocioException("Error al registrar la cita con servicio.", e);
        } catch (RuntimeException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

}