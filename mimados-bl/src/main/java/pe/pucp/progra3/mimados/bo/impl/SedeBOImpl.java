package pe.pucp.progra3.mimados.bo.impl;

import pe.pucp.progra3.mimados.DBManager.TransactionContext;
import pe.pucp.progra3.mimados.bo.SedeBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.dao.SedeDAO;
import pe.pucp.progra3.mimados.dao.imp.SedeDAOimp;
import pe.pucp.progra3.mimados.model.Sede;

import java.sql.SQLException;
import java.util.List;

public class SedeBOImpl implements SedeBO {

    private final SedeDAO sedeDAO;

    public SedeBOImpl() {
        this.sedeDAO = new SedeDAOimp();
    }

    @Override
    public void insertar(Sede sede)
            throws SQLException, NegocioException {

        validarSede(sede);

        try {
            sedeDAO.agregar_sede(sede);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void modificar(Sede sede)
            throws SQLException, NegocioException {

        if (sede == null) {
            throw new NegocioException(
                    "La sede no puede ser nula."
            );
        }

        validarId(sede.getId());
        validarNombre(sede.getNombre());
        validarDireccion(sede.getDireccion());

        validarTelefono(sede.getTelefono());

        try {
//            sedeDAO.modificar_direccion(
//                    sede.getId(),
//                    sede.getDireccion()
//            );
            sedeDAO.modificar_sede(sede);

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
            sedeDAO.eliminar_sede(id);

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Sede> listarTodos()
            throws SQLException {

        return sedeDAO.listar_sedes();
    }

    @Override
    public Sede obtenerPorId(int id)
            throws SQLException, NegocioException {

        validarId(id);

        return sedeDAO.mostrar_sede(id);
    }

    private void validarSede(Sede sede)
            throws NegocioException {

        if (sede == null) {
            throw new NegocioException(
                    "La sede no puede ser nula."
            );
        }


        validarNombre(sede.getNombre());
        validarDireccion(sede.getDireccion());

        validarTelefono(sede.getTelefono());
    }

    private void validarDireccion(String direccion)
            throws NegocioException {

        if (direccion == null ||
                direccion.isBlank()) {

            throw new NegocioException(
                    "La dirección de la sede es obligatoria."
            );
        }

        if (direccion.length() > 60) {
            throw new NegocioException(
                    "La dirección de la sede no puede superar los 60 caracteres."
            );
        }
    }
    private void validarNombre(String nombre)
            throws NegocioException {

        if (nombre == null ||
                nombre.isBlank()) {

            throw new NegocioException(
                    "El nombre de la sede es obligatorio."
            );
        }

        if (nombre.length() > 60) {
            throw new NegocioException(
                    "El nombre de la sede no puede superar los 60 caracteres."
            );
        }
    }

    private void validarTelefono(String telefono)
            throws NegocioException {

        if (telefono == null ||
                telefono.isBlank()) {

            throw new NegocioException(
                    "El teléfono de la sede es obligatorio."
            );
        }

        if (telefono.length() > 9) {
            throw new NegocioException(
                    "El teléfono de la sede no puede superar los 9 caracteres."
            );
        }
    }


    private void validarId(Integer id)
            throws NegocioException {

        if (id == null || id <= 0) {
            throw new NegocioException(
                    "El ID de la sede debe ser mayor que cero."
            );
        }
    }
}