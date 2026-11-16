package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.EmpleadoPuesto;

import java.time.LocalDate;

public interface EmpleadoPuestoBO {

    void insertar(EmpleadoPuesto empleadoPuesto);

    void iniciar(int idEmpleadoPuesto);

    void eliminar(int idEmpleadoPuesto, LocalDate fechaFin);
}