package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.AtencionBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.AtencionDAO;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.dao.impl.AtencionDAOImpl;
import pe.pucp.progra3.mimados.dao.impl.CitaDAOImpl;
import pe.pucp.progra3.mimados.model.entity.Atencion;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.enums.Estado;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AtencionBOImpl implements AtencionBO {

    private final AtencionDAO atencionDAO;
    private final CitaDAO citaDAO;

    public AtencionBOImpl() {
        this.atencionDAO = new AtencionDAOImpl();
        this.citaDAO = new CitaDAOImpl();
    }

    @Override
    public void insertar(Atencion atencion) {
        validarAtencion(atencion);
        try {
            Cita cita = atencion.getCita();
            if (cita.getId() > 0) {
                Cita citaExistente = citaDAO.mostrarCita(cita.getId());
                if (citaExistente == null) {
                    throw new NegocioException("La cita asociada no existe.");
                }
                if (citaExistente.getEstado() != Estado.PROGRAMADA &&
                        citaExistente.getEstado() != Estado.CONFIRMADA) {
                    throw new NegocioException(
                            "La cita no puede atenderse en su estado actual (" + citaExistente.getEstado() + ").");
                }
            } else {
                LocalDateTime ahora = LocalDateTime.now();
                cita.setEmpleado(atencion.getEmpleado());
                cita.setFechaHoraInicio(ahora);
                cita.setFechaHoraFin(ahora);
                if (cita.getMotivo() == null || cita.getMotivo().trim().isEmpty()) {
                    cita.setMotivo("Atención directa sin cita previa");
                }
                citaDAO.insertarCita(cita);
            }
            atencionDAO.insertarAtencion(atencion);
            citaDAO.atenderCita(cita.getId());
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar la atención.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Atencion atencion) {
        if (atencion == null) {
            throw new NegocioException("La atención no puede ser nula.");
        }
        validarId(atencion.getId());
        validarDatosAtencion(atencion);
        try {
            atencionDAO.actualizarAtencion(atencion);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar la atención.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Atencion obtenerPorId(int id) {
        validarId(id);
        try {
            return atencionDAO.mostrarAtencion(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener la atención.", e);
        }
    }

    private void validarAtencion(Atencion atencion) {
        if (atencion == null) {
            throw new NegocioException("La atención no puede ser nula.");
        }
        if (atencion.getCita() == null) {
            throw new NegocioException("La atención debe estar asociada a una cita o a una mascota.");
        }
        if (atencion.getCita().getId() <= 0) {
            Mascota mascota = atencion.getCita().getMascota();
            if (mascota == null || mascota.getId() <= 0) {
                throw new NegocioException("Debe especificar una cita previa o una mascota válida.");
            }
        }
        if (atencion.getEmpleado() == null || atencion.getEmpleado().getUsuario() == null) {
            throw new NegocioException("La atención debe tener un empleado.");
        }
        validarId(atencion.getEmpleado().getUsuario().getId());
        validarDatosAtencion(atencion);
    }

    private void validarDatosAtencion(Atencion atencion) {
        if (atencion.getPesoActual() != null && atencion.getPesoActual() < 0) {
            throw new NegocioException("El peso actual no puede ser negativo.");
        }
        if (atencion.getSintomas() != null && atencion.getSintomas().length() > 255) {
            throw new NegocioException("Los síntomas no pueden superar los 255 caracteres.");
        }
        if (atencion.getDiagnostico() != null && atencion.getDiagnostico().length() > 255) {
            throw new NegocioException("El diagnóstico no puede superar los 255 caracteres.");
        }
        if (atencion.getTratamientoRecetado() != null && atencion.getTratamientoRecetado().length() > 500) {
            throw new NegocioException("El tratamiento recetado no puede superar los 500 caracteres.");
        }
        if (atencion.getObservaciones() != null && atencion.getObservaciones().length() > 255) {
            throw new NegocioException("Las observaciones no pueden superar los 255 caracteres.");
        }
        if (atencion.getMontoTotal() == null) {
            throw new NegocioException("El monto total es obligatorio.");
        }
        if (atencion.getMontoTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El monto total no puede ser negativo.");
        }
    }

    private void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID debe ser mayor que cero.");
        }
    }
}
