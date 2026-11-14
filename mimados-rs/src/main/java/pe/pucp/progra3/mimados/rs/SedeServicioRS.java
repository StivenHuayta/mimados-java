package pe.pucp.progra3.mimados.rs;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.SedeServicioBO;

import pe.pucp.progra3.mimados.bo.exception.NegocioException;
import pe.pucp.progra3.mimados.bo.impl.SedeServicioBOImpl;
import pe.pucp.progra3.mimados.bo.impl.ServicioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.SedeServicioDTO;
import pe.pucp.progra3.mimados.model.entity.Sede;
import pe.pucp.progra3.mimados.model.entity.SedeServicio;
import pe.pucp.progra3.mimados.model.entity.Servicio;

import java.io.Serializable;

import java.util.List;

@Path("SedeServicioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SedeServicioRS {

    public static class SedeServicioRequest implements Serializable {
        private Integer id;
        private Integer id_sede;
        private Integer id_servicio;

        public Integer getId() {return id;}

        public void setId(Integer id) {this.id = id;}

        public Integer getId_sede() {return id_sede;}

        public void setId_sede(Integer id_sede) {this.id_sede = id_sede;}

        public Integer getId_servicio() {return id_servicio;}

        public void setId_servicio(Integer id_servicio) {this.id_servicio = id_servicio;}
    }

    private SedeServicioBO sedeServicioBO= new SedeServicioBOImpl();


    @POST
    @Path("registrar")
    public Response registrarSedeServicio(SedeServicioRequest req){
        try{
            Sede sede=new Sede(req.getId_sede());
            Servicio servicio= new Servicio(req.getId_servicio());
            SedeServicio nueva=new SedeServicio(sede,servicio);
            sedeServicioBO.insertar(nueva);
            return Response.ok(new ServicioRS.ApiResponse(true, "SedeServicio registrado correctamente", nueva)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ServicioRS.ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @GET
    @Path("listarSedeServicios/{id}")
    public Response listarServicios(@PathParam("id") int id){

        try {
            List<SedeServicioDTO> servicios=sedeServicioBO.listarPorSede(id);
            return Response.ok(new ServicioRS.ApiResponse(true,"Listado correcto",servicios)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ServicioRS.ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }
    @GET
    @Path("listarServiciosDisponibles/{id}")
    public Response listarServiciosDisponibles(@PathParam("id") int id){

        try {
            List<Servicio> servicios=sedeServicioBO.listarServiciosDisponibles(id);
            return Response.ok(new ServicioRS.ApiResponse(true,"Listado correcto",servicios)).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ServicioRS.ApiResponse(false, e.getMessage(), null))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarSedeServicio(@PathParam("id") int id){
        try{
            sedeServicioBO.eliminar(id);
            return Response.noContent().build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ServicioRS.ApiResponse(false,e.getMessage(),null ))
                    .build();
        }
    }
}
