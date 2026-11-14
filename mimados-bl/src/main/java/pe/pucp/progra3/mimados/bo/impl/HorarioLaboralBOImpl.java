package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.HorarioLaboralBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.HorarioLaboralDAO;
import pe.pucp.progra3.mimados.dao.imp.HorarioLaboralimp;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;

import java.sql.SQLException;
import java.util.List;

public class HorarioLaboralBOImpl implements HorarioLaboralBO {

    private final HorarioLaboralDAO horarioLaboralDAO;

    public HorarioLaboralBOImpl() {
        this.horarioLaboralDAO = new HorarioLaboralimp();
    }

    @Override
    public void insertar(HorarioLaboral horario) {

        validarHorario(horario);

        int idEmpleado =
                horario.getEmpleado().getUsuario().getId();

        validarCruceHorario(horario, idEmpleado);

        try {
            horarioLaboralDAO.insertar_horario(horario);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el horario laboral.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(HorarioLaboral horario) {

        if (horario == null) {
            throw new NegocioException(
                    "El horario no puede ser nulo."
            );
        }

        validarId(horario.getId());

        validarHoras(horario);

        try {
            horarioLaboralDAO.actualizar_horario_laboral(
                    horario.getId(),
                    horario.getDiaSemana(),
                    horario.getHoraInicio(),
                    horario.getHoraFin()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el horario laboral.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id) {

        validarId(id);

        try {
            horarioLaboralDAO.eliminar_horario_laboral(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar el horario laboral.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<HorarioLaboral> listarTodos() {

        try {
            return horarioLaboralDAO.listar_horarios();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los horarios laborales.", e);
        }
    }

    @Override
    public List<HorarioLaboral> listarPorEmpleado(int idEmpleado) {

        validarId(idEmpleado);

        try {
            return horarioLaboralDAO.horarios_por_empleado(idEmpleado);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los horarios del empleado.", e);
        }
    }

    private void validarHorario(HorarioLaboral horario) {

        if (horario == null) {
            throw new NegocioException(
                    "El horario no puede ser nulo."
            );
        }

        if (horario.getEmpleado() == null ||
                horario.getEmpleado().getUsuario() == null) {

            throw new NegocioException(
                    "El horario debe tener un empleado."
            );
        }

        validarId(
                horario.getEmpleado().getUsuario().getId()
        );

        if (horario.getDiaSemana() == null) {
            throw new NegocioException(
                    "El día de la semana es obligatorio."
            );
        }

        validarHoras(horario);
    }

    private void validarHoras(HorarioLaboral horario) {

        if (horario.getHoraInicio() == null ||
                horario.getHoraFin() == null) {

            throw new NegocioException(
                    "La hora de inicio y fin son obligatorias."
            );
        }

        if (!horario.getHoraInicio()
                .isBefore(horario.getHoraFin())) {

            throw new NegocioException(
                    "La hora de inicio debe ser menor a la hora de fin."
            );
        }
    }

    private void validarCruceHorario(
            HorarioLaboral horario,
            int idEmpleado
    ) {

        List<HorarioLaboral> horariosActuales;
        try {
            horariosActuales = horarioLaboralDAO.horarios_por_empleado(idEmpleado);
        } catch (SQLException e) {
            throw new NegocioException("Error al consultar los horarios del empleado.", e);
        }

        for (HorarioLaboral existente : horariosActuales) {

            boolean mismoDia =
                    existente.getDiaSemana() ==
                            horario.getDiaSemana();

            boolean seCruzan =
                    horario.getHoraInicio()
                            .isBefore(existente.getHoraFin())
                            &&
                            horario.getHoraFin()
                                    .isAfter(existente.getHoraInicio());

            if (Boolean.TRUE.equals(existente.getActivo())
                    && mismoDia
                    && seCruzan) {

                throw new NegocioException(
                        "El horario se cruza con otro horario ya registrado."
                );
            }
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}