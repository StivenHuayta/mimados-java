package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.MascotaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.MascotaDAO;
import pe.pucp.progra3.mimados.dao.imp.MascotaDAOimp;
import pe.pucp.progra3.mimados.model.Mascota;

import java.sql.SQLException;
import java.util.List;

public class MascotaBOImpl implements MascotaBO {

    private final MascotaDAO mascotaDAO;

    public MascotaBOImpl() {
        this.mascotaDAO = new MascotaDAOimp();
    }

    @Override
    public void insertar(Mascota mascota)
            throws SQLException, NegocioException {

        validarMascota(mascota);

        try {
            mascotaDAO.insertar_mascota(mascota);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Mascota mascota)
            throws SQLException, NegocioException {

        if (mascota == null) {
            throw new NegocioException(
                    "La mascota no puede ser nula."
            );
        }

        validarId(mascota.getId());
        validarNombre(mascota.getNombre());

        try {
            mascotaDAO.actualizar_mascota_nombre(
                    mascota.getId(),
                    mascota.getNombre()
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
            mascotaDAO.eliminar_mascota(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Mascota> listarTodos()
            throws SQLException {

        return mascotaDAO.listar_mascotas();
    }

    @Override
    public Mascota obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return mascotaDAO.mostrar_mascota(id);
    }

    @Override
    public List<Mascota> listarPorDueno(int idCliente)
            throws SQLException, NegocioException {

        validarId(idCliente);

        return mascotaDAO.mostrar_mascotas_dueno(idCliente);
    }

    private void validarMascota(Mascota mascota)
            throws NegocioException {

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

    private void validarNombre(String nombre)
            throws NegocioException {

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

    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID debe ser mayor que cero."
            );
        }
    }
}