package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.CitaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.CitaDAO;
import pe.pucp.progra3.mimados.dao.imp.CitaDAOimp;
import pe.pucp.progra3.mimados.model.Cita;

import java.sql.SQLException;
import java.util.List;

public class CitaBOImpl implements CitaBO {

    private final CitaDAO citaDAO;

    public CitaBOImpl() {
        this.citaDAO = new CitaDAOimp();
    }

    @Override
    public void insertar(Cita cita)
            throws SQLException, NegocioException {

        validarCita(cita);

        try {
            citaDAO.insertar_cita(cita);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Cita> listarPorMascota(int idMascota)
            throws SQLException, NegocioException {

        validarId(idMascota);

        return citaDAO.listar_citas_por_mascota(idMascota);
    }

    @Override
    public Cita obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return citaDAO.mostrar_cita(id);
    }

    @Override
    public void cancelar(int idCita)
            throws SQLException, NegocioException {

        validarId(idCita);

        try {
            citaDAO.cancelar_cita(idCita);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void atender(int idCita)
            throws SQLException, NegocioException {

        validarId(idCita);

        try {
            citaDAO.atender_cita(idCita);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    private void validarCita(Cita cita)
            throws NegocioException {

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

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}