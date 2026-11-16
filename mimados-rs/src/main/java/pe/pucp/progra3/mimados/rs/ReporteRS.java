package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.ReporteServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.ReporteServicioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("ReporteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteRS {

    public static class ApiResponse implements Serializable {
        private final boolean success;
        private final String message;
        private final Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

    private final ReporteServicioBO reporteServicioBO = new ReporteServicioBOImpl();

    @GET
    @Path("listarServiciosSolicitados")
    public Response listarServiciosMasSolicitados(@QueryParam("fechaInicio") String fechaInicioStr,
                                                  @QueryParam("fechaFin") String fechaFinStr) {
        try {
            Date fechaInicio = parsearFecha(fechaInicioStr, "fechaInicio");
            Date fechaFin = parsearFecha(fechaFinStr, "fechaFin");

            List<ReporteServiciosSolicitadosDTO> servicios =
                    reporteServicioBO.listarServiciosMasSolicitados(fechaInicio, fechaFin);

            return Response.ok(new ApiResponse(true, "Listado correcto", servicios)).build();
        } catch (NegocioException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    private static Date parsearFecha(String valor, String nombreCampo) throws NegocioException {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(LocalDate.parse(valor.trim()));
        } catch (DateTimeParseException e) {
            throw new NegocioException("Formato de fecha inválido para " + nombreCampo + " (use yyyy-MM-dd).");
        }
    }
}
