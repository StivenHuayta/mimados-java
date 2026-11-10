package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.ServicioBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.ServicioBOImpl;
import pe.pucp.progra3.mimados.model.Sede;
import pe.pucp.progra3.mimados.model.Servicio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
@Path("ServicioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioRS {

    public static class ServiceRequest implements Serializable {
        private Integer id;
        private String nombre;
        private String descripcion;
        private BigDecimal precioReferencial;
        private Integer duracionMinutos;
        private Boolean activo;

        public Boolean getActivo() {return activo;}

        public void setActivo(Boolean activo) {this.activo = activo;}

        public Integer getDuracionMinutos() {return duracionMinutos;}

        public void setDuracionMinutos(Integer duracionMinutos) {this.duracionMinutos = duracionMinutos;}

        public BigDecimal getPrecioReferencial() {return precioReferencial;}

        public void setPrecioReferencial(BigDecimal precioReferencial) {this.precioReferencial = precioReferencial;}

        public String getDescripcion() {return descripcion;}

        public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

        public String getNombre() {return nombre;}

        public void setNombre(String nombre) {this.nombre = nombre;}

        public Integer getId() {return id;}

        public void setId(Integer id) {this.id = id;}
    }

    public static class ApiResponse implements Serializable {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

    private ServicioBO servicioBO= new ServicioBOImpl();


    @POST
    @Path("registrar")
    public Response registrarServicio(ServiceRequest req){
        try{
            Servicio nueva=new Servicio(req.getNombre(), req.getDescripcion(), req.getPrecioReferencial(),req.getDuracionMinutos());
            servicioBO.insertar(nueva);
            return Response.ok(new ApiResponse(true, "Servicio registrad correctamente", nueva)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();

        }
    }


    @GET
    @Path("listarServicios")
    public Response listarServicios(){
        try {
            List<Servicio> servicios=servicioBO.listarTodos();
            return Response.ok(new ApiResponse(true,"Listado correcto",servicios)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerServicioPorId(@PathParam("id") int id){
        try{
            Servicio servicio=servicioBO.obtenerPorId(id);
            return Response.ok(new ApiResponse(true,"Solicitud correcta",servicio)).build();
        } catch (SQLException e) {
            //lanza error averiguar
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarServicio(@PathParam("id") int id){
        try{
            servicioBO.eliminar(id);
            return Response.noContent().build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null))
                    .build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(false,e.getMessage(),null ))
                    .build();
        }
    }
    @PUT
    public Response actualizar(Servicio servicio) {
        try {
            servicioBO.modificar(servicio);

            return Response.ok().build(); // 200 OK
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
