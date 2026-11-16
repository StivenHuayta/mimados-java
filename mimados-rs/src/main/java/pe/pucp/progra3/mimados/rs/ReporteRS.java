package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.pucp.progra3.mimados.bo.ReporteServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.ReporteServicioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.dto.response.ReporteServiciosSolicitadosDTO;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.*;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

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

    @GET
    @Path("/descargarServicioReporte")
    @Produces("application/pdf")
    public Response generarReporteServiciosPDF(@QueryParam("fechaInicio") String fechaInicioStr,
                                               @QueryParam("fechaFin") String fechaFinStr) {
        try {
            Date fechaInicio = parsearFecha(fechaInicioStr, "fechaInicio");
            Date fechaFin = parsearFecha(fechaFinStr, "fechaFin");
            List<ReporteServiciosSolicitadosDTO>  servicios =
                    reporteServicioBO.listarServiciosMasSolicitados(fechaInicio, fechaFin);
            double sum=0;
            for(ReporteServiciosSolicitadosDTO o : servicios)
                sum+=o.getTotal();

            JRBeanCollectionDataSource dataSource= new JRBeanCollectionDataSource(servicios);

            InputStream reporteStream = this.getClass().getResourceAsStream("/REPORTES/ServReporte.jasper");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Plantilla Jasper no encontrada").build();
            }

            Map<String, Object> parametros = new HashMap<>();
            //parametros.put("MIMADOS", "Historial Clínico Veterinario");
            parametros.put("totales", sum);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteStream, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            //probar
//            StreamingOutput stream = output -> {
//                output.write(pdfBytes);
//                output.flush();
//            };

            //inline / attachment
//            return Response.ok(pdfBytes)
////                    .header("Content-Type","application/pdf")
//                    .header("Content-Disposition", "inline; filename=\"ReporteServicios.pdf\"")
//                    .build();
            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=\"reporte.pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("text/plain; charset=UTF-8")
                    .entity("Error al generar el PDF: " + e.getMessage())
                    .build();
        }
    }
}
