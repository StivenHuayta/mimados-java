package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.UsuarioBOImpl;
import pe.pucp.progra3.mimados.model.AuthResultado;

import java.io.Serializable;
import java.sql.SQLException;

@Path("/auth")
public class AuthResource {

    private final UsuarioBO usuarioBO = new UsuarioBOImpl();

    public static class LoginRequest implements Serializable {
        private String email;
        private String password;

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

    public static class ApiResponse implements Serializable {
        private boolean success;
        private String message;
        private AuthResultado data;

        public ApiResponse(boolean success, String message, AuthResultado data) {
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

        public AuthResultado getData() {
            return data;
        }

        public void setData(AuthResultado data) {
            this.data = data;
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
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
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error de base de datos: " + e.getMessage(), null))
                    .build();
        }
    }
}
