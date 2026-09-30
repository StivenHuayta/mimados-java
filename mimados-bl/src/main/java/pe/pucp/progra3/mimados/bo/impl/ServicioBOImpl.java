package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.ServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ServicioDAO;
import pe.pucp.progra3.mimados.dao.imp.ServicioDAOimp;
import pe.pucp.progra3.mimados.model.Servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ServicioBOImpl implements ServicioBO {

    private final ServicioDAO servicioDAO;

    public ServicioBOImpl() {
        this.servicioDAO = new ServicioDAOimp();
    }

    @Override
    public void insertar(Servicio servicio)
            throws SQLException, NegocioException {

        validarServicio(servicio);

        try {
            servicioDAO.insertar_servicio(servicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Servicio servicio)
            throws SQLException, NegocioException {

        if (servicio == null) {
            throw new NegocioException(
                    "El servicio no puede ser nulo."
            );
        }

        validarId(servicio.getId());
        validarPrecio(servicio.getPrecioReferencial());

        try {
            servicioDAO.actualizar_servicio_precio(
                    servicio.getId(),
                    servicio.getPrecioReferencial()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id)
            throws SQLException, NegocioException {

        validarId(id);

        try {
            servicioDAO.eliminar_servicio(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Servicio> listarTodos()
            throws SQLException {

        return servicioDAO.listar_servicios();
    }

    @Override
    public Servicio obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return servicioDAO.mostrar_servicio(id);
    }

    private void validarServicio(Servicio servicio)
            throws NegocioException {

        if (servicio == null) {
            throw new NegocioException(
                    "El servicio no puede ser nulo."
            );
        }

        if (servicio.getNombre() == null ||
                servicio.getNombre().isBlank()) {

            throw new NegocioException(
                    "El nombre del servicio es obligatorio."
            );
        }

        if (servicio.getNombre().length() > 60) {
            throw new NegocioException(
                    "El nombre del servicio no puede superar los 60 caracteres."
            );
        }

        if (servicio.getDescripcion() != null &&
                servicio.getDescripcion().length() > 255) {

            throw new NegocioException(
                    "La descripción no puede superar los 255 caracteres."
            );
        }

        validarPrecio(servicio.getPrecioReferencial());

        if (servicio.getDuracionMinutos() == null) {
            throw new NegocioException(
                    "La duración del servicio es obligatoria."
            );
        }

        if (servicio.getDuracionMinutos() <= 0) {
            throw new NegocioException(
                    "La duración del servicio debe ser mayor que cero."
            );
        }
    }

    private void validarPrecio(BigDecimal precio)
            throws NegocioException {

        if (precio == null) {
            throw new NegocioException(
                    "El precio del servicio es obligatorio."
            );
        }

        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException(
                    "El precio del servicio no puede ser negativo."
            );
        }
    }

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del servicio debe ser mayor que cero."
            );
        }
    }
}