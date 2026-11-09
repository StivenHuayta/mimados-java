package pe.pucp.progra3.mimados.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.SedeBO;
import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.SedeBOImpl;
import pe.pucp.progra3.mimados.model.Sede;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Path("SedeRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SedeRS {

    public static class SedeRequest implements Serializable {
        private Integer id;
        private String nombre;
        private String direccion;
        private String telefono;
        private Boolean activo;

        public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }

        public String getNombre() {return nombre;}

        public void setNombre(String nombre) {this.nombre = nombre;}

        public String getDireccion() {return direccion;}

        public void setDireccion(String direccion) {this.direccion = direccion;}

        public String getTelefono() {return telefono;}

        public void setTelefono(String telefono) {this.telefono = telefono;}

        public Boolean getActivo() {return activo;}

        public void setActivo(Boolean activo) {this.activo = activo;}
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

    private SedeBO sedeBO= new SedeBOImpl();
    @GET
    @Path("listarSede")
    public Response listarSedes(){
        try {
            List <Sede> sedes=sedeBO.listarTodos();
            return Response.ok(new ApiResponse(true,"Listado correcto",sedes)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerSedePorId(@PathParam("id") int id){
        try{
            Sede sede=sedeBO.obtenerPorId(id);
            return Response.ok(new ApiResponse(true,"Solicitud correcta",sede)).build();
        } catch (SQLException e) {
            //lanza error averiguar
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(false, "Error BD: " + e.getMessage(), null)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(false, e.getMessage(), null)).build();
        }
    }

}
