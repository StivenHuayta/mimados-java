
package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.pucp.progra3.mimados.bo.DisponibilidadBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.DisponibilidadBOImpl;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path("DisponibilidadRS")
@Produces(MediaType.APPLICATION_JSON)
public class DisponibilidadRS {

    private final DisponibilidadBO disponibilidadBO =
            new DisponibilidadBOImpl();

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

    // Consultar horarios disponibles
    @GET
    @Path("consultar")
    public Response consultarDisponibilidad(
            @QueryParam("empleadoId") int empleadoId,
            @QueryParam("fecha") String fecha,
            @QueryParam("duracion") int duracion) {

        if (empleadoId <= 0 ||
                fecha == null ||
                fecha.isBlank() ||
                duracion <= 0) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(
                            false, "Parametros invalidos", null))
                    .build();
        }

        try {
            LocalDate fechaConsulta = LocalDate.parse(fecha);

            List<LocalTime> horarios =
                    disponibilidadBO.consultarDisponibilidad(
                            empleadoId,
                            fechaConsulta,
                            duracion
                    );

            List<String> horasDisponibles = new ArrayList<>();

            for (LocalTime hora : horarios) {
                horasDisponibles.add(hora.toString());
            }

            return Response.ok(new ApiResponse(
                            true,
                            "Horarios disponibles",
                            horasDisponibles))
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

        } catch (SQLException e) {
            return Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(
                            false, "Error al consultar disponibilidad", null))
                    .build();
        }
    }
}
