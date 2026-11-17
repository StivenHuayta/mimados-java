package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.ServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.ServicioDAO;
import pe.pucp.progra3.mimados.dao.impl.ServicioDAOImpl;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ServicioBOImpl implements ServicioBO {

    private final ServicioDAO servicioDAO;

    public ServicioBOImpl() {
        this.servicioDAO = new ServicioDAOImpl();
    }

    @Override
    public void insertar(Servicio servicio) {

        validarServicio(servicio);

        try {
            servicioDAO.insertarServicio(servicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar el servicio.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Servicio servicio) {

        if (servicio == null) {
            throw new NegocioException(
                    "El servicio no puede ser nulo."
            );
        }

        validarId(servicio.getId());

        validarServicio(servicio);


        try {
//            servicioDAO.actualizarServicio_precio(
//                    servicio.getId(),
//                    servicio.getPrecioReferencial()
//            );
            servicioDAO.actualizarServicio(servicio);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar el servicio.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id) {

        validarId(id);

        try {
            servicioDAO.eliminarServicio(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar el servicio.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Servicio> listarTodos(String nombre,Double preMin,Double preMax,Integer minMin,Integer minMax) {

        try {
            validarRangoPrecio(preMin,preMax);
            validarRangoDuracion(minMin,minMax);
            //agregar validadores
            if(nombre==null || nombre.isBlank()) nombre= "";
            //no se admiten negativos, el valor minimo que pueden tomar es 0
            double pMin = (preMin == null || preMin < 0) ? 0.0 : preMin;
            double pMax = (preMax == null || preMax < 0) ? 0.0 : preMax;
            int dMin = (minMin == null || minMin < 0) ? 0 : minMin;
            int dMax = (minMax == null || minMax < 0) ? 0 : minMax;
            return servicioDAO.listarServicios(nombre, pMin, pMax, dMin, dMax);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar los servicios.", e);
        }
    }

    @Override
    public Servicio obtenerPorId(int id) {

        validarId(id);

        try {
            return servicioDAO.mostrarServicio(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el servicio.", e);
        }
    }

    private void validarServicio(Servicio servicio) {

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

    private void validarPrecio(BigDecimal precio) {

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

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID del servicio debe ser mayor que cero."
            );
        }
    }
    private void validarRangoPrecio(Double precioMin,Double precioMax){
        if (precioMin!=null && precioMax!=null && precioMax > 0 && precioMax<precioMin) {
            throw new NegocioException(
                    "El precio maximo debe ser mayor que el precio minimo."
            );
        }
    }
    private void validarRangoDuracion(Integer duracionMin,Integer duracionMax){
        if (duracionMin!=null && duracionMax!=null && duracionMax > 0 && duracionMax<duracionMin) {
            throw new NegocioException(
                    "La duracion maxima no puede ser menor que la duracion minima."
            );
        }
    }
}