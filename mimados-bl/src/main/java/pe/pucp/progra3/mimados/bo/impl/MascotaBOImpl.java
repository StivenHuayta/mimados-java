package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.MascotaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.MascotaDAO;
import pe.pucp.progra3.mimados.dao.impl.MascotaDAOImpl;
import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.entity.Mascota;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MascotaBOImpl implements MascotaBO {

    private final MascotaDAO mascotaDAO;

    public MascotaBOImpl() {
        this.mascotaDAO = new MascotaDAOImpl();
    }

    @Override
    public void insertar(Mascota mascota) {

        validarMascota(mascota);

        try {
            mascotaDAO.insertarMascota(mascota);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al registrar la mascota.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Mascota mascota) {

        if (mascota == null) {
            throw new NegocioException(
                    "La mascota no puede ser nula."
            );
        }

        validarId(mascota.getId());
        validarNombre(mascota.getNombre());

        try {
            mascotaDAO.actualizarMascotaNombre(
                    mascota.getId(),
                    mascota.getNombre()
            );

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al modificar la mascota.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminar(int id) {

        validarId(id);

        try {
            mascotaDAO.eliminarMascota(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new NegocioException("Error al eliminar la mascota.", e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Mascota> listarTodos() {

        try {
            return mascotaDAO.listarMascotas();
        } catch (SQLException e) {
            throw new NegocioException("Error al listar las mascotas.", e);
        }
    }

    @Override
    public Mascota obtenerPorId(int id) {

        validarId(id);

        try {
            return mascotaDAO.mostrarMascota(id);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener la mascota.", e);
        }
    }

    @Override
    public List<Mascota> listarPorDueno(int idCliente) {

        validarId(idCliente);

        try {
            return mascotaDAO.mostrarMascotasDueno(idCliente);
        } catch (SQLException e) {
            throw new NegocioException("Error al listar las mascotas del dueño.", e);
        }
    }

    @Override
    public List<RegistroMedicoDTO> listarHistorialPorMascota(int mascotaId) {
        // Validación de negocio básica para evitar consultas innecesarias a la BD
        if (mascotaId <= 0) {
            System.out.println("BO Error: El ID de la mascota debe ser mayor a cero.");
            return new ArrayList<>();
        }
        // Llamada a la capa de datos
        try {
            return mascotaDAO.listarHistorialPorMascota(mascotaId);
        } catch (SQLException e) {
            throw new NegocioException("Error al obtener el historial médico de la mascota.", e);
        }
    }




    private void validarMascota(Mascota mascota) {

        if (mascota == null) {
            throw new NegocioException(
                    "La mascota no puede ser nula."
            );
        }

        if (mascota.getCliente() == null ||
                mascota.getCliente().getUsuario() == null) {

            throw new NegocioException(
                    "La mascota debe tener un cliente."
            );
        }

        validarId(
                mascota.getCliente().getUsuario().getId()
        );

        validarNombre(mascota.getNombre());

        if (mascota.getEspecie() == null) {
            throw new NegocioException(
                    "La especie de la mascota es obligatoria."
            );
        }

        if (mascota.getSexo() == null) {
            throw new NegocioException(
                    "El sexo de la mascota es obligatorio."
            );
        }

        if (mascota.getRaza() != null &&
                mascota.getRaza().length() > 45) {

            throw new NegocioException(
                    "La raza no puede superar los 45 caracteres."
            );
        }

        if (mascota.getPesoReferencial() != null &&
                mascota.getPesoReferencial() < 0) {

            throw new NegocioException(
                    "El peso referencial no puede ser negativo."
            );
        }
    }

    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new NegocioException(
                    "El nombre de la mascota es obligatorio."
            );
        }

        if (nombre.length() > 45) {
            throw new NegocioException(
                    "El nombre de la mascota no puede superar los 45 caracteres."
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
}