
package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.pucp.progra3.mimados.bo.VeterinarioBO;
import pe.pucp.progra3.mimados.bo.impl.VeterinarioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.VeterinarioResumen;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Path("VeterinarioRS")
@Produces(MediaType.APPLICATION_JSON)
public class VeterinarioRS {

    private final VeterinarioBO veterinarioBO =
            new VeterinarioBOImpl();

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

    // Listar veterinarios
    @GET
    @Path("listar")
    public Response listarVeterinarios() {

        try {
            List<VeterinarioResumen> veterinarios =
                    veterinarioBO.listarTodos();

            return Response.ok(
                    new ApiResponse(
                            true,
                            "Listado de veterinarios",
                            veterinarios
                    )
            ).build();

        } catch (SQLException e) {

            e.printStackTrace();

            return Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(
                            false,
                            "Error al listar veterinarios",
                            null
                    ))
                    .build();
        }
    }
}
