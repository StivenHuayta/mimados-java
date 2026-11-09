package pe.pucp.progra3.mimados.rs.Mascotas;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.MascotaBO;
import pe.pucp.progra3.mimados.bo.impl.MascotaBOImpl;
import pe.pucp.progra3.mimados.model.DTO.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.Mascota;

import java.util.List;

@Path("/mascotas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MascotaWebService {
    private MascotaBO mascotaBO;
    public MascotaWebService(){
        this.mascotaBO  = new MascotaBOImpl() ;
    }

    @GET
    @Path("/cliente/{id}")
    public Response listarMascotasCliente(@PathParam("id") int id){
        try{
            List<Mascota> mascotas = mascotaBO.listarPorDueno(id);
            return Response.ok(mascotas).build();
        } catch (Exception e){
            return Response.serverError().entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id){
        try{
            Mascota mascota = mascotaBO.obtenerPorId(id);
            if (mascota != null) {
                return Response.ok(mascota).build();
            }else{
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @GET
    @Path("/historial/{id}")
    public Response obtenerHistorialPorMascota(@PathParam("id") int mascotaId) {
        try {
            // Llamamos al BO que acabas de actualizar
            List<RegistroMedicoDTO> historial = mascotaBO.listarHistorialPorMascota(mascotaId);

            return Response.ok(historial).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener el historial médico\"}")
                    .build();
        }
    }






    @POST
    public Response insertar(Mascota mascota) {
        try {

            mascotaBO.insertar(mascota);

            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // ACTUALIZAR UNA MASCOTA
    @PUT
    public Response actualizar(Mascota mascota) {
        try {
            mascotaBO.modificar(mascota);

            return Response.ok().build(); // 200 OK
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            mascotaBO.eliminar(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }





}
