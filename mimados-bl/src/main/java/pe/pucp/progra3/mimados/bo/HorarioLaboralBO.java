package pe.pucp.progra3.mimados.bo;

import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;

import java.util.List;

public interface HorarioLaboralBO {

    void insertar(HorarioLaboral horario);

    void modificar(HorarioLaboral horario);

    void eliminar(int id);

    List<HorarioLaboral> listarTodos();

    List<HorarioLaboral> listarPorEmpleado(int idEmpleado);
}