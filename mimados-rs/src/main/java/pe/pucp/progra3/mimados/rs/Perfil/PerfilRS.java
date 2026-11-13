package pe.pucp.progra3.mimados.rs.Perfil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.mimados.bo.ClienteBO;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.impl.ClienteBOImpl;
import pe.pucp.progra3.mimados.bo.impl.UsuarioBOImpl;
import pe.pucp.progra3.mimados.model.DTO.UsuarioPerfilDTO;
import pe.pucp.progra3.mimados.rs.UsuarioRS;


@Path("PerfilRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerfilRS {

    private final UsuarioBO usuarioBO = new UsuarioBOImpl();
    private final ClienteBO clienteBO = new ClienteBOImpl();

    @GET
    @Path("{id}")
    public Response obtenerPerfil(@PathParam("id") Integer id) {
        try {
            UsuarioPerfilDTO perfil = usuarioBO.obtenerPerfil(id);
            if (perfil != null) {
                return Response.ok(perfil).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new UsuarioRS.ApiResponse(false, "Perfil no encontrado", null))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new UsuarioRS.ApiResponse(false, "Error al obtener el perfil: " + e.getMessage(), null))
                    .build();
        }
    }

    @PUT
    @Path("actualizar")
    public Response actualizarPerfil(UsuarioPerfilDTO perfilDto) {
        if (perfilDto == null || perfilDto.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new UsuarioRS.ApiResponse(false, "Datos incompletos", null))
                    .build();
        }

        try {
            boolean actualizado = usuarioBO.actualizarPerfil(perfilDto);
            if (actualizado) {
                return Response.ok(new UsuarioRS.ApiResponse(true, "Perfil actualizado", null)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new UsuarioRS.ApiResponse(false, "No se pudo actualizar el perfil", null))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new UsuarioRS.ApiResponse(false, "Error al actualizar: " + e.getMessage(), null))
                    .build();
        }
    }

    @DELETE
    @Path("eliminar/{id}")
    public Response eliminarCuenta(@PathParam("id") Integer id) {
        try {

            clienteBO.eliminar(id);


            return Response.ok(new UsuarioRS.ApiResponse(true, "Cuenta eliminada lógicamente", null)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new UsuarioRS.ApiResponse(false, "Error al eliminar: " + e.getMessage(), null))
                    .build();
        }
    }
}