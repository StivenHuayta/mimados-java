package pe.pucp.progra3.mimados.rs.Reporte;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.ReporteServicioBO;
import pe.pucp.progra3.mimados.bo.impl.ReporteServicioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;
import pe.pucp.progra3.mimados.model.entity.Sede;
import pe.pucp.progra3.mimados.rs.SedeRS;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Path("ReporteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteRS {

    ReporteServicioBO reporteServicioBO= new ReporteServicioBOImpl();

    @GET
    @Path("listarServiciosSolicitados")
    public Response listarServiciosMasSolicitados(@QueryParam("fechaInicio") Date fechaInicio,
                                                  @QueryParam("fechaFin") Date fechaFin){
        try {
            List<ReporteServiciosSolicitadosDTO> servicios= reporteServicioBO.
                    listarServiciosMasSolicitados(fechaInicio,fechaFin);
            return Response.ok(new SedeRS.ApiResponse(true,"Listado correcto",servicios)).build();
        } catch (SQLException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SedeRS.ApiResponse(false, "Error BD: " + e.getMessage(), null))
                    .build();
        }
    }

}
