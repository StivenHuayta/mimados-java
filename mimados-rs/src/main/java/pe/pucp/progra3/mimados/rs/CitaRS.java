package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.CitaBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.CitaBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.CitaListadoEmpleadoDTO;
import pe.pucp.progra3.mimados.model.dto.response.CitaVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.PageResultDTO;
import pe.pucp.progra3.mimados.model.entity.*;
import pe.pucp.progra3.mimados.model.enums.Estado;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path("CitaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaRS {

    private final CitaBO citaBO = new CitaBOImpl();

    public static class CitaRequest implements Serializable {
        private Integer id;
        private Integer empleadoId;
        private Integer mascotaId;
        private String fechaHoraInicio;
        private String fechaHoraFin;
        private String estado;
        private String motivo;
        private Integer sedeId;
        private Integer sedeServicioId;

        public CitaRequest() {}

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public Integer getEmpleadoId() { return empleadoId; }
        public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

        public Integer getMascotaId() { return mascotaId; }
        public void setMascotaId(Integer mascotaId) { this.mascotaId = mascotaId; }

        public String getFechaHoraInicio() { return fechaHoraInicio; }
        public void setFechaHoraInicio(String fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

        public String getFechaHoraFin() { return fechaHoraFin; }
        public void setFechaHoraFin(String fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public Integer getSedeId() { return sedeId; }
        public void setSedeId(Integer sedeId) { this.sedeId = sedeId; }
        public Integer getSedeServicioId() { return sedeServicioId; }
        public void setSedeServicioId(Integer sedeServicioId) { this.sedeServicioId = sedeServicioId; }
    }

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
                    .entity(new ApiResponse(false, "Faltan datos obligatorios", null))
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
            cita.setFechaHoraInicio(LocalDateTime.parse(req.getFechaHoraInicio()));
            cita.setFechaHoraFin(LocalDateTime.parse(req.getFechaHoraFin()));
            cita.setMotivo(req.getMotivo());
            citaBO.insertar(cita);
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Cita registrada correctamente", convertir(cita)))
                    .build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Formato de fecha inválido", null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("listarPorMascota/{idMascota}")
    public Response listarPorMascota(@PathParam("idMascota") int idMascota) {
        try {
            List<Cita> citas = citaBO.listarPorMascota(idMascota);
            List<CitaRequest> resultado = new ArrayList<>();
            for (Cita cita : citas) {
                resultado.add(convertir(cita));
            }
            return Response.ok(new ApiResponse(true, "Listado de citas", resultado)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerCita(@PathParam("id") int id) {
        try {
            Cita cita = citaBO.obtenerPorId(id);
            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(false, "Cita no encontrada", null))
                        .build();
            }
            return Response.ok(new ApiResponse(true, "Cita encontrada", convertir(cita))).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response cancelarCita(@PathParam("id") int id) {
        try {
            if (citaBO.obtenerPorId(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(false, "Cita no encontrada", null))
                        .build();
            }
            citaBO.cancelar(id);
            return Response.ok(new ApiResponse(true, "Cita cancelada correctamente", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    private CitaRequest convertir(Cita cita) {
        CitaRequest dato = new CitaRequest();
        dato.setId(cita.getId());
        if (cita.getEmpleado() != null && cita.getEmpleado().getUsuario() != null) {
            dato.setEmpleadoId(cita.getEmpleado().getUsuario().getId());
        }
        if (cita.getMascota() != null) {
            dato.setMascotaId(cita.getMascota().getId());
        }
        if (cita.getFechaHoraInicio() != null) {
            dato.setFechaHoraInicio(cita.getFechaHoraInicio().toString());
        }
        if (cita.getFechaHoraFin() != null) {
            dato.setFechaHoraFin(cita.getFechaHoraFin().toString());
        }
        if (cita.getEstado() != null) {
            dato.setEstado(cita.getEstado().name());
        }
        dato.setMotivo(cita.getMotivo());
        return dato;
    }

    @PUT
    @Path("{id}")
    public Response modificarCita(@PathParam("id") int id, CitaRequest req) {
        if (id <= 0 || req == null ||
                req.getEmpleadoId() == null ||
                req.getEmpleadoId() <= 0 ||
                req.getFechaHoraInicio() == null ||
                req.getFechaHoraInicio().isBlank() ||
                req.getFechaHoraFin() == null ||
                req.getFechaHoraFin().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Faltan datos obligatorios", null))
                    .build();
        }
        try {
            Cita cita = citaBO.obtenerPorId(id);
            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(false, "Cita no encontrada", null))
                        .build();
            }
            Usuario usuario = new Usuario();
            usuario.setId(req.getEmpleadoId());
            Empleado empleado = new Empleado();
            empleado.setUsuario(usuario);
            cita.setEmpleado(empleado);
            cita.setFechaHoraInicio(LocalDateTime.parse(req.getFechaHoraInicio()));
            cita.setFechaHoraFin(LocalDateTime.parse(req.getFechaHoraFin()));
            cita.setMotivo(req.getMotivo());
            citaBO.modificar(cita);
            return Response.ok(new ApiResponse(true, "Cita modificada correctamente", convertir(cita))).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Formato de fecha invalido", null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("listar")
    public Response listarTodas() {

        try {
            List<CitaListadoEmpleadoDTO> citas = citaBO.listarTodas();

            return Response.ok(new ApiResponse(
                    true,
                    "Listado de todas las citas",
                    citas
            )).build();

        } catch (NegocioException e) {
            return Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(
                            false, e.getMessage(), null))
                    .build();
        }
    }

    @POST
    @Path("registrarEmpleado")
    public Response registrarCitaEmpleado(CitaRequest req) {
        if (req == null || req.getEmpleadoId() == null || req.getEmpleadoId() <= 0
                || req.getMascotaId() == null || req.getMascotaId() <= 0
                || req.getSedeId() == null || req.getSedeId() <= 0
                || req.getSedeServicioId() == null || req.getSedeServicioId() <= 0
                || req.getFechaHoraInicio() == null || req.getFechaHoraInicio().isBlank()
                || req.getFechaHoraFin() == null || req.getFechaHoraFin().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Faltan datos obligatorios", null)).build();
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
            cita.setFechaHoraInicio(LocalDateTime.parse(req.getFechaHoraInicio()));
            cita.setFechaHoraFin(LocalDateTime.parse(req.getFechaHoraFin()));
            cita.setMotivo(req.getMotivo());

            citaBO.insertarConServicio(cita, req.getSedeId(), req.getSedeServicioId());

            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Cita y servicio registrados correctamente",
                            convertir(cita)))
                    .build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Formato de fecha inválido", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @GET
    @Path("veterinario/{empleadoId}")
    public Response listarPorVeterinario(
            @PathParam("empleadoId") int empleadoId,
            @QueryParam("searchText") String searchText,
            @QueryParam("fecha") String fechaStr,
            @QueryParam("estado") String estadoStr,
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("recordsPerPage") @DefaultValue("10") int recordsPerPage) {
        try {
            LocalDate fecha = (fechaStr != null && !fechaStr.isBlank())
                    ? LocalDate.parse(fechaStr.trim())
                    : null;
            Estado estado = (estadoStr != null && !estadoStr.isBlank())
                    ? Estado.valueOf(estadoStr.trim().toUpperCase())
                    : null;
            PageResultDTO<CitaVeterinarioDTO> resultado =
                    citaBO.listarPorVeterinarioPaginado(
                            empleadoId, searchText, fecha, estado, pageNumber, recordsPerPage);
            return Response.ok(new ApiResponse(true, "Listado de citas del veterinario", resultado)).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Formato de fecha inválido (use yyyy-MM-dd)", null))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Estado de cita inválido", null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }
}
