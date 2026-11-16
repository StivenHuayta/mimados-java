package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.ClienteBO;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.ClienteBOImpl;
import pe.pucp.progra3.mimados.bo.impl.UsuarioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.AuthResultado;
import pe.pucp.progra3.mimados.model.dto.response.UsuarioPerfilDTO;

import java.io.Serializable;

@Path("UsuarioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioRS {

    private final UsuarioBO usuarioBO = new UsuarioBOImpl();
    private final ClienteBO clienteBO = new ClienteBOImpl();



    public static class LoginRequest implements Serializable {
        private String email;
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegistroClienteRequest implements Serializable {
        private String dni;
        private String nombres;
        private String apellidos;
        private String email;
        private String password;
        private String telefono;
        private String direccion;
        private String contactoEmergencia;

        public RegistroClienteRequest() {
        }

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
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public String getContactoEmergencia() { return contactoEmergencia; }
        public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }
    }

    public static class ApiResponse implements Serializable {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse() {
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    @POST
    @Path("autenticar")
    public Response autenticar(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Credenciales no proporcionadas", null))
                    .build();
        }

        try {
            AuthResultado resultado = usuarioBO.autenticar(request.getEmail(), request.getPassword());
            return Response.ok(new ApiResponse(true, "Autenticación exitosa", resultado)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @POST
    @Path("login")
    public Response login(LoginRequest request) {
        return autenticar(request);
    }

    @POST
    @Path("registrarCliente")
    public Response registrarCliente(RegistroClienteRequest req) {
        if (req == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Datos de registro no proporcionados", null))
                    .build();
        }

        try {
            AuthResultado resultado = usuarioBO.registrarCliente(
                    req.getDni(), req.getNombres(), req.getApellidos(),
                    req.getEmail(), req.getPassword(), req.getTelefono(),
                    req.getDireccion(), req.getContactoEmergencia()
            );
            return Response.ok(new ApiResponse(true, "Cliente registrado exitosamente", resultado)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("perfil/{id}")
    public Response obtenerPerfil(@PathParam("id") Integer id) {
        try {
            UsuarioPerfilDTO perfil = usuarioBO.obtenerPerfil(id);
            if (perfil != null) {
                return Response.ok(perfil).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(false, "Perfil no encontrado", null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @PUT
    @Path("perfil")
    public Response actualizarPerfil(UsuarioPerfilDTO perfilDto) {
        if (perfilDto == null || perfilDto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Datos incompletos", null))
                    .build();
        }

        try {
            boolean actualizado = usuarioBO.actualizarPerfil(perfilDto);
            if (actualizado) {
                return Response.ok(new ApiResponse(true, "Perfil actualizado", null)).build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "No se pudo actualizar el perfil", null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @DELETE
    @Path("eliminar/{id}")
    public Response eliminarCuenta(@PathParam("id") Integer id) {
        try {
            clienteBO.eliminar(id);
            return Response.ok(new ApiResponse(true, "Cuenta eliminada lógicamente", null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

}
