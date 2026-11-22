package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.AtencionBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.AtencionBOImpl;
import pe.pucp.progra3.mimados.model.entity.Atencion;
import pe.pucp.progra3.mimados.model.entity.Cita;
import pe.pucp.progra3.mimados.model.entity.Empleado;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.io.Serializable;
import java.math.BigDecimal;

@Path("AtencionRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtencionRS {

    private final AtencionBO atencionBO = new AtencionBOImpl();

    public static class AtencionRequest implements Serializable {
        private Integer citaId;
        private Integer mascotaId;
        private Integer empleadoId;
        private String motivo;
        private Double pesoActual;
        private Double temperatura;
        private String sintomas;
        private String diagnostico;
        private String tratamientoRecetado;
        private String observaciones;
        private BigDecimal montoTotal;

        public AtencionRequest() {}

        public Integer getCitaId() { return citaId; }
        public void setCitaId(Integer citaId) { this.citaId = citaId; }

        public Integer getMascotaId() { return mascotaId; }
        public void setMascotaId(Integer mascotaId) { this.mascotaId = mascotaId; }

        public Integer getEmpleadoId() { return empleadoId; }
        public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public Double getPesoActual() { return pesoActual; }
        public void setPesoActual(Double pesoActual) { this.pesoActual = pesoActual; }

        public Double getTemperatura() { return temperatura; }
        public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

        public String getSintomas() { return sintomas; }
        public void setSintomas(String sintomas) { this.sintomas = sintomas; }

        public String getDiagnostico() { return diagnostico; }
        public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

        public String getTratamientoRecetado() { return tratamientoRecetado; }
        public void setTratamientoRecetado(String tratamientoRecetado) { this.tratamientoRecetado = tratamientoRecetado; }

        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

        public BigDecimal getMontoTotal() { return montoTotal; }
        public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
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
    public Response registrarAtencion(AtencionRequest req) {
        boolean tieneCita = req != null && req.getCitaId() != null && req.getCitaId() > 0;
        boolean tieneMascota = req != null && req.getMascotaId() != null && req.getMascotaId() > 0;

        if (req == null ||
                (!tieneCita && !tieneMascota) ||
                req.getEmpleadoId() == null || req.getEmpleadoId() <= 0 ||
                req.getMontoTotal() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, "Faltan datos obligatorios (cita o mascota, veterinario y monto)", null))
                    .build();
        }

        try {
            Cita cita = new Cita();
            if (tieneCita) {
                cita.setId(req.getCitaId());
            } else {
                cita.setId(0);
                Mascota mascota = new Mascota();
                mascota.setId(req.getMascotaId());
                cita.setMascota(mascota);
                cita.setMotivo(req.getMotivo());
            }

            Usuario usuario = new Usuario();
            usuario.setId(req.getEmpleadoId());
            Empleado empleado = new Empleado();
            empleado.setUsuario(usuario);

            Atencion atencion = new Atencion();
            atencion.setCita(cita);
            atencion.setEmpleado(empleado);
            atencion.setPesoActual(req.getPesoActual());
            atencion.setTemperatura(req.getTemperatura());
            atencion.setSintomas(req.getSintomas());
            atencion.setDiagnostico(req.getDiagnostico());
            atencion.setTratamientoRecetado(req.getTratamientoRecetado());
            atencion.setObservaciones(req.getObservaciones());
            atencion.setMontoTotal(req.getMontoTotal());

            atencionBO.insertar(atencion);

            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "Atención clínica registrada correctamente", null))
                    .build();

        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }
}
