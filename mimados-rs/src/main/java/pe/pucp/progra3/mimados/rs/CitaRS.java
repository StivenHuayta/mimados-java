
package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.pucp.progra3.mimados.bo.CitaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.CitaBOImpl;

import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path("CitaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaRS {

    private final CitaBO citaBO = new CitaBOImpl();

    // Clase para recibir y devolver los datos de una cita
    public static class CitaRequest implements Serializable {

        private Integer id;
        private Integer empleadoId;
        private Integer mascotaId;
        private String fechaHoraInicio;
        private String fechaHoraFin;
        private String estado;
        private String motivo;

        public CitaRequest() {}

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public Integer getEmpleadoId() { return empleadoId; }
        public void setEmpleadoId(Integer empleadoId) {
            this.empleadoId = empleadoId;
        }

        public Integer getMascotaId() { return mascotaId; }
        public void setMascotaId(Integer mascotaId) {
            this.mascotaId = mascotaId;
        }

        public String getFechaHoraInicio() { return fechaHoraInicio; }
        public void setFechaHoraInicio(String fechaHoraInicio) {
            this.fechaHoraInicio = fechaHoraInicio;
        }

        public String getFechaHoraFin() { return fechaHoraFin; }
        public void setFechaHoraFin(String fechaHoraFin) {
            this.fechaHoraFin = fechaHoraFin;
        }

        public String getEstado() { return estado; }
        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }
    }

    // Clase para las respuestas del servicio
    public static class ApiResponse implements Serializable {

        private boolean success;
        private String message;
        private Object data;

        public ApiResponse() {}

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

    // Registrar una cita
    @POST
    @Path("registrar")
    public Response registrarCita(CitaRequest req) {

        if (req == null ||
                req.getEmpleadoId() == null ||
                req.getMascotaId() == null ||
                req.getEmpleadoId() <= 0 ||
                req.getMascotaId() <= 0 ||
                req.getFechaHoraInicio() == null ||
                req.getFechaHoraInicio().isBlank() ||
                req.getFechaHoraFin() == null ||
                req.getFechaHoraFin().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, "Faltan datos obligatorios", null))
                    .build();
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setId(req.getEmpleadoId());

            Empleado empleado = new Empleado();
            empleado.setUsuario(usuario);

            Mascota mascota = new Mascota();
            mascota.setId(req.getMascotaId());

            Cita cita = new Cita();
            cita.setEmpleado(empleado);
            cita.setMascota(mascota);
            cita.setFechaHoraInicio(
                    LocalDateTime.parse(req.getFechaHoraInicio()));
            cita.setFechaHoraFin(
                    LocalDateTime.parse(req.getFechaHoraFin()));
            cita.setMotivo(req.getMotivo());

            citaBO.insertar(cita);

            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(
                            true,
                            "Cita registrada correctamente",
                            convertir(cita)))
                    .build();

        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, "Formato de fecha inválido", null))
                    .build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

    // Listar citas por mascota
    @GET
    @Path("listarPorMascota/{idMascota}")
    public Response listarPorMascota(
            @PathParam("idMascota") int idMascota) {

        try {
            List<Cita> citas = citaBO.listarPorMascota(idMascota);
            List<CitaRequest> resultado = new ArrayList<>();

            for (Cita cita : citas) {
                resultado.add(convertir(cita));
            }

            return Response.ok(new ApiResponse(
                    true, "Listado de citas", resultado)).build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

    // Obtener cita por ID
    @GET
    @Path("{id}")
    public Response obtenerCita(@PathParam("id") int id) {

        try {
            Cita cita = citaBO.obtenerPorId(id);

            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(
                                false, "Cita no encontrada", null))
                        .build();
            }

            return Response.ok(new ApiResponse(
                    true, "Cita encontrada", convertir(cita))).build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

    // Cancelar una cita
    @DELETE
    @Path("{id}")
    public Response cancelarCita(@PathParam("id") int id) {

        try {
            if (citaBO.obtenerPorId(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(
                                false, "Cita no encontrada", null))
                        .build();
            }

            citaBO.cancelar(id);

            return Response.ok(new ApiResponse(
                            true, "Cita cancelada correctamente", null))
                    .build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

    // Convertir la entidad Cita para enviarla como JSON
    private CitaRequest convertir(Cita cita) {

        CitaRequest dato = new CitaRequest();

        dato.setId(cita.getId());

        if (cita.getEmpleado() != null &&
                cita.getEmpleado().getUsuario() != null) {

            dato.setEmpleadoId(
                    cita.getEmpleado().getUsuario().getId());
        }

        if (cita.getMascota() != null) {
            dato.setMascotaId(cita.getMascota().getId());
        }

        if (cita.getFechaHoraInicio() != null) {
            dato.setFechaHoraInicio(
                    cita.getFechaHoraInicio().toString());
        }

        if (cita.getFechaHoraFin() != null) {
            dato.setFechaHoraFin(
                    cita.getFechaHoraFin().toString());
        }

        if (cita.getEstado() != null) {
            dato.setEstado(cita.getEstado().name());
        }

        dato.setMotivo(cita.getMotivo());

        return dato;
    }


    // Modificar una cita
    @PUT
    @Path("{id}")
    public Response modificarCita(
            @PathParam("id") int id,
            CitaRequest req) {

        if (id <= 0 || req == null ||
                req.getEmpleadoId() == null ||
                req.getEmpleadoId() <= 0 ||
                req.getFechaHoraInicio() == null ||
                req.getFechaHoraInicio().isBlank() ||
                req.getFechaHoraFin() == null ||
                req.getFechaHoraFin().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, "Faltan datos obligatorios", null))
                    .build();
        }

        try {
            Cita cita = citaBO.obtenerPorId(id);

            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(
                                false, "Cita no encontrada", null))
                        .build();
            }

            Usuario usuario = new Usuario();
            usuario.setId(req.getEmpleadoId());

            Empleado empleado = new Empleado();
            empleado.setUsuario(usuario);

            cita.setEmpleado(empleado);
            cita.setFechaHoraInicio(
                    LocalDateTime.parse(req.getFechaHoraInicio()));
            cita.setFechaHoraFin(
                    LocalDateTime.parse(req.getFechaHoraFin()));
            cita.setMotivo(req.getMotivo());

            citaBO.modificar(cita);

            return Response.ok(new ApiResponse(
                            true,
                            "Cita modificada correctamente",
                            convertir(cita)))
                    .build();

        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, "Formato de fecha invalido", null))
                    .build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

}
