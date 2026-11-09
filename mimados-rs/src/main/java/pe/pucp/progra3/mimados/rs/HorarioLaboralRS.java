package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.HorarioLaboralBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.HorarioLaboralBOImpl;
import pe.pucp.progra3.mimados.model.DiaSemana;
import pe.pucp.progra3.mimados.model.Empleado;
import pe.pucp.progra3.mimados.model.HorarioLaboral;
import pe.pucp.progra3.mimados.model.Usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@Path("HorarioLaboralRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HorarioLaboralRS {

    private final HorarioLaboralBO horarioBO = new HorarioLaboralBOImpl();

    public static class HorarioRequest implements Serializable {
        private Integer id;
        private Integer empleadoId;
        private String diaSemana; // Ej: "LUNES"
        private String horaInicio; // Ej: "08:00"
        private String horaFin; // Ej: "16:00"
        private Boolean activo;

        public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
        public Integer getEmpleadoId() { return empleadoId; } public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }
        public String getDiaSemana() { return diaSemana; } public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
        public String getHoraInicio() { return horaInicio; } public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
        public String getHoraFin() { return horaFin; } public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
        public Boolean getActivo() { return activo; } public void setActivo(Boolean activo) { this.activo = activo; }
    }

    public static class ApiResponse implements Serializable {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success; this.message = message; this.data = data;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

    @POST
    @Path("registrar")
    public Response registrarHorario(HorarioRequest req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "Datos vacíos", null)).build();

        try {
            DiaSemana dia = DiaSemana.valueOf(req.getDiaSemana().toUpperCase());
            LocalTime inicio = LocalTime.parse(req.getHoraInicio());
            LocalTime fin = LocalTime.parse(req.getHoraFin());

            HorarioLaboral horario = new HorarioLaboral(null, dia, inicio, fin, req.getActivo() != null ? req.getActivo() : true);
            
            Empleado emp = new Empleado();
            Usuario u = new Usuario();
            u.setId(req.getEmpleadoId());
            emp.setUsuario(u);
            horario.setEmpleado(emp);

            horarioBO.insertar(horario);
            return Response.ok(new ApiResponse(true, "Horario registrado", null)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "Formato de hora o día inválido", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        }
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public Response listarPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        try {
            List<HorarioLaboral> lista = horarioBO.listarPorEmpleado(idEmpleado);
            return Response.ok(new ApiResponse(true, "Listado de horarios del empleado", lista)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("eliminar")
    public Response eliminarHorario(HorarioRequest req) {
        if (req == null || req.getId() == null) return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, "ID requerido", null)).build();
        
        try {
            horarioBO.eliminar(req.getId());
            return Response.ok(new ApiResponse(true, "Horario eliminado", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        }
    }
}
