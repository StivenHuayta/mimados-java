package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.EmpleadoBO;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.EmpleadoBOImpl;
import pe.pucp.progra3.mimados.bo.impl.UsuarioBOImpl;
import pe.pucp.progra3.mimados.dao.EmpleadoPuestoDAO;
import pe.pucp.progra3.mimados.dao.impl.EmpleadoPuestoDAOImpl;
import pe.pucp.progra3.mimados.model.dto.response.EmpleadoResponseDTO;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.Sede;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoRS {

    private final EmpleadoBO empleadoBO = new EmpleadoBOImpl();
    private final UsuarioBO usuarioBO = new UsuarioBOImpl();
    private final EmpleadoPuestoDAO empleadoPuestoDAO = new EmpleadoPuestoDAOImpl();

    public static class EmpleadoRequest implements Serializable {
        private Integer id;
        private String dni;
        private String nombres;
        private String apellidos;
        private String email;
        private String password;
        private String telefono;
        private Integer sedeId;
        private String codigoCmvp;
        private Boolean esAdmin;
        private Boolean esVeterinario;
        private Boolean activo;

        public EmpleadoRequest() {}

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }
        public String getNombres() { return nombres; }
        public void setNombres(String nombres) { this.nombres = nombres; }
        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public Integer getSedeId() { return sedeId; }
        public void setSedeId(Integer sedeId) { this.sedeId = sedeId; }
        public String getCodigoCmvp() { return codigoCmvp; }
        public void setCodigoCmvp(String codigoCmvp) { this.codigoCmvp = codigoCmvp; }
        public Boolean getEsAdmin() { return esAdmin; }
        public void setEsAdmin(Boolean esAdmin) { this.esAdmin = esAdmin; }
        public Boolean getEsVeterinario() { return esVeterinario; }
        public void setEsVeterinario(Boolean esVeterinario) { this.esVeterinario = esVeterinario; }
        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
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
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }

    @GET
    @Path("buscar-usuario")
    public Response buscarUsuarioPorDni(@QueryParam("dni") String dni) {
        if (dni == null || dni.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Ingrese un DNI para buscar.", null)).build();
        }
        try {
            Usuario u = usuarioBO.buscarPorDni(dni.trim());
            if (u == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse(false, "No se encontró ninguna cuenta registrada con ese DNI.", null)).build();
            }

            Empleado empExistente = null;
            try {
                empExistente = empleadoBO.obtenerPorId(u.getId());
            } catch (NegocioException ignored) {}

            if (empExistente != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ApiResponse(false, "Esta persona ya se encuentra registrada como empleado.", null)).build();
            }

            EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
            dto.setId(u.getId());
            dto.setDni(u.getDni());
            dto.setNombres(u.getNombres());
            dto.setApellidos(u.getApellidos());
            dto.setEmail(u.getEmail());
            dto.setTelefono(u.getTelefono());

            return Response.ok(new ApiResponse(true, "Usuario encontrado", dto)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("registrar")
    public Response registrarEmpleado(EmpleadoRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Datos vacíos", null)).build();
        }

        try {
            Usuario uCreado = null;

            if (req.getId() != null && req.getId() > 0) {
                uCreado = usuarioBO.obtenerPorId(req.getId());
                if (uCreado == null) {
                    throw new NegocioException("No se encontró el usuario seleccionado.");
                }
            } else {
                if (req.getDni() != null && !req.getDni().isBlank()) {
                    uCreado = usuarioBO.buscarPorDni(req.getDni().trim());
                }
                if (uCreado == null && req.getEmail() != null && !req.getEmail().isBlank()) {
                    uCreado = usuarioBO.buscarPorEmail(req.getEmail().trim());
                }
            }

            if (uCreado != null) {
                Empleado empExistente = null;
                try {
                    empExistente = empleadoBO.obtenerPorId(uCreado.getId());
                } catch (NegocioException ignored) {}
                if (empExistente != null) {
                    throw new NegocioException("Esta persona ya se encuentra registrada como empleado.");
                }
            } else {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setDni(req.getDni());
                nuevoUsuario.setNombres(req.getNombres());
                nuevoUsuario.setApellidos(req.getApellidos());
                nuevoUsuario.setEmail(req.getEmail());
                nuevoUsuario.setPasswordHash(req.getPassword());
                nuevoUsuario.setTelefono(req.getTelefono());

                usuarioBO.insertar(nuevoUsuario);

                uCreado = usuarioBO.buscarPorEmail(req.getEmail().trim());
                if (uCreado == null) {
                    throw new NegocioException("Error al recuperar el ID del usuario insertado.");
                }
            }

            Empleado empleado = new Empleado();
            empleado.setUsuario(uCreado);

            Sede sede = new Sede();
            sede.setId(req.getSedeId());
            empleado.setSede(sede);

            boolean esAdmin = Boolean.TRUE.equals(req.getEsAdmin());
            boolean esVet = req.getEsVeterinario() != null
                    ? Boolean.TRUE.equals(req.getEsVeterinario())
                    : (req.getCodigoCmvp() != null && !req.getCodigoCmvp().isBlank());

            empleado.setCodigoCmvp(req.getCodigoCmvp());
            empleado.setEsAdmin(esAdmin);
            empleado.setActivo(true);

            empleadoBO.insertar(empleado);
            if (esAdmin) {
                empleadoBO.modificar(empleado);
            }
            try {
                empleadoPuestoDAO.sincronizarPuestosEmpleado(uCreado.getId(), esAdmin, esVet);
            } catch (SQLException ex) {
                throw new NegocioException("Empleado creado, pero falló la asignación de puestos.", ex);
            }

            return Response.ok(new ApiResponse(true, "Empleado registrado correctamente", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("modificar")
    public Response modificarEmpleado(EmpleadoRequest req) {
        if (req == null || req.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "ID de empleado requerido", null)).build();
        }
        try {
            Empleado empleado = new Empleado();
            Usuario u = new Usuario();
            u.setId(req.getId());
            empleado.setUsuario(u);

            Sede s = new Sede();
            s.setId(req.getSedeId());
            empleado.setSede(s);

            boolean esAdmin = Boolean.TRUE.equals(req.getEsAdmin());
            boolean esVet = req.getEsVeterinario() != null
                    ? Boolean.TRUE.equals(req.getEsVeterinario())
                    : (req.getCodigoCmvp() != null && !req.getCodigoCmvp().isBlank());

            empleado.setCodigoCmvp(req.getCodigoCmvp());
            empleado.setEsAdmin(esAdmin);
            empleado.setActivo(req.getActivo());

            empleadoBO.modificar(empleado);
            try {
                empleadoPuestoDAO.sincronizarPuestosEmpleado(req.getId(), esAdmin, esVet);
            } catch (SQLException ex) {
                throw new NegocioException("Empleado modificado, pero falló la actualización de puestos.", ex);
            }

            return Response.ok(new ApiResponse(true, "Empleado modificado correctamente", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

    @GET
    @Path("listar")
    public Response listarEmpleados() {
        try {
            List<Empleado> lista = empleadoBO.listarTodos();
            List<EmpleadoResponseDTO> listaDto = new ArrayList<>();

            if (lista != null) {
                for (Empleado e : lista) {
                    EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
                    if (e.getUsuario() != null) {
                        Integer uid = null;
                        try { uid = e.getUsuario().getId(); } catch (Exception ignored) {}

                        // Si el DAO de Empleados no trajo el ID del usuario, lo recuperamos por email
                        if ((uid == null || uid == 0) && e.getUsuario().getEmail() != null) {
                            Usuario uCompleto = usuarioBO.buscarPorEmail(e.getUsuario().getEmail());
                            if (uCompleto != null) {
                                try { uid = uCompleto.getId(); } catch (Exception ignored) {}
                            }
                        }

                        dto.setId(uid != null ? uid : 0);
                        dto.setDni(e.getUsuario().getDni());
                        dto.setNombres(e.getUsuario().getNombres());
                        dto.setApellidos(e.getUsuario().getApellidos());
                        dto.setEmail(e.getUsuario().getEmail());
                        dto.setTelefono(e.getUsuario().getTelefono());
                    }
                    if (e.getSede() != null) {
                        try { dto.setSedeId(e.getSede().getId()); } catch (Exception ignored) { dto.setSedeId(1); }
                    }
                    dto.setCodigoCmvp(e.getCodigoCmvp());
                    dto.setEsAdmin(e.getEsAdmin());
                    boolean esVet = false;
                    if (dto.getId() != null && dto.getId() > 0) {
                        try {
                            esVet = empleadoPuestoDAO.tienePuestoVeterinarioActivo(dto.getId());
                        } catch (SQLException ignored) {}
                    }
                    if (!esVet && !Boolean.TRUE.equals(e.getEsAdmin()) && e.getCodigoCmvp() != null && !e.getCodigoCmvp().isBlank()) {
                        esVet = true;
                    }
                    dto.setEsVeterinario(esVet);
                    dto.setActivo(e.getActivo());
                    listaDto.add(dto);
                }
            }

            return Response.ok(new ApiResponse(true, "Listado correcto", listaDto)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        }
    }}