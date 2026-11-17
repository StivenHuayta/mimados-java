package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.HorarioLaboralBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.HorarioLaboralBOImpl;
import pe.pucp.progra3.mimados.model.enums.DiaSemana;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.HorarioLaboral;
import pe.pucp.progra3.mimados.model.entity.Usuario;
import pe.pucp.progra3.mimados.bo.EmpleadoBO;
import pe.pucp.progra3.mimados.bo.impl.EmpleadoBOImpl;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Path("/horarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HorarioLaboralRS {

    private final HorarioLaboralBO horarioBO = new HorarioLaboralBOImpl();
    private final EmpleadoBO empleadoBO = new EmpleadoBOImpl();

    public static class HorarioRequest implements Serializable {
        private Integer id;
        private Integer empleadoId;
        private String diaSemana; // Ej: "LUNES"
        private String horaInicio; // Ej: "08:00"
        private String horaFin; // Ej: "16:00"
        private Boolean activo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getEmpleadoId() {
            return empleadoId;
        }

        public void setEmpleadoId(Integer empleadoId) {
            this.empleadoId = empleadoId;
        }

        public String getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(String diaSemana) {
            this.diaSemana = diaSemana;
        }

        public String getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        public String getHoraFin() {
            return horaFin;
        }

        public void setHoraFin(String horaFin) {
            this.horaFin = horaFin;
        }

        public Boolean getActivo() {
            return activo;
        }

        public void setActivo(Boolean activo) {
            this.activo = activo;
        }
    }

    public static class ApiResponse implements Serializable {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    @POST
    @Path("registrar")
    public Response registrarHorario(HorarioRequest req) {
        if (req == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "Datos vacíos", null)).build();

        try {
            DiaSemana dia = DiaSemana.valueOf(req.getDiaSemana().toUpperCase());
            LocalTime inicio = LocalTime.parse(req.getHoraInicio());
            LocalTime fin = LocalTime.parse(req.getHoraFin());

            // Buscamos el empleado completo para que el BO tenga su Sede, estado y rol
            Empleado empCompleto = empleadoBO.obtenerPorId(req.getEmpleadoId());
            if (empCompleto == null) {
                empCompleto = new Empleado();
                Usuario u = new Usuario();
                u.setId(req.getEmpleadoId());
                empCompleto.setUsuario(u);
                empCompleto.setActivo(true);
                empCompleto.setEsAdmin(false);
            }

            HorarioLaboral horario = new HorarioLaboral(null, dia, inicio, fin, req.getActivo() != null ? req.getActivo() : true);
            horario.setEmpleado(empCompleto);

            horarioBO.insertar(horario);
            return Response.ok(new ApiResponse(true, "Horario registrado", null)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "Formato de hora o día inválido", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public Response listarPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        try {
            List<HorarioLaboral> lista = horarioBO.listarPorEmpleado(idEmpleado);
            List<HorarioRequest> listaDto = new java.util.ArrayList<>();

            if (lista != null) {
                for (HorarioLaboral h : lista) {
                    HorarioRequest dto = new HorarioRequest();
                    dto.setId(h.getId());
                    dto.setEmpleadoId(idEmpleado);
                    if (h.getDiaSemana() != null) {
                        dto.setDiaSemana(h.getDiaSemana().name());
                    }
                    if (h.getHoraInicio() != null) {
                        dto.setHoraInicio(h.getHoraInicio().toString());
                    }
                    if (h.getHoraFin() != null) {
                        dto.setHoraFin(h.getHoraFin().toString());
                    }
                    dto.setActivo(h.getActivo()); // Si el getter en HorarioLaboral se llama getActivo(), cámbialo aquí
                    listaDto.add(dto);
                }
            }

            return Response.ok(new ApiResponse(true, "Listado de horarios del empleado", listaDto)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("modificar")
    public Response modificarHorario(HorarioRequest req) {
        if (req == null || req.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "ID de horario requerido", null)).build();
        }

        try {
            DiaSemana dia = DiaSemana.valueOf(req.getDiaSemana().toUpperCase());
            LocalTime inicio = LocalTime.parse(req.getHoraInicio());
            LocalTime fin = LocalTime.parse(req.getHoraFin());

            HorarioLaboral horario = new HorarioLaboral(
                    req.getId(),
                    dia,
                    inicio,
                    fin,
                    req.getActivo() != null ? req.getActivo() : true
            );

            horarioBO.modificar(horario);
            return Response.ok(new ApiResponse(true, "Horario modificado correctamente", null)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Formato de hora o día inválido", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("eliminar")
    public Response eliminarHorario(HorarioRequest req) {
        if (req == null || req.getId() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "ID requerido", null)).build();

        try {
            horarioBO.eliminar(req.getId());
            return Response.ok(new ApiResponse(true, "Horario eliminado", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }
}
