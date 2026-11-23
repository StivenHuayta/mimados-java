package pe.pucp.progra3.mimados.rs;


import jakarta.servlet.ServletContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pe.pucp.progra3.mimados.bo.ClienteBO;
import pe.pucp.progra3.mimados.bo.MascotaBO;
import pe.pucp.progra3.mimados.bo.UsuarioBO;
import pe.pucp.progra3.mimados.bo.impl.ClienteBOImpl;
import pe.pucp.progra3.mimados.bo.impl.MascotaBOImpl;
import pe.pucp.progra3.mimados.bo.impl.UsuarioBOImpl;
import pe.pucp.progra3.mimados.model.dto.response.AtencionHistorialDTO;
import pe.pucp.progra3.mimados.model.dto.response.PacienteVeterinarioDTO;
import pe.pucp.progra3.mimados.model.dto.response.PageResultDTO;
import pe.pucp.progra3.mimados.model.dto.response.RegistroMedicoDTO;
import pe.pucp.progra3.mimados.model.entity.Mascota;
import pe.pucp.progra3.mimados.model.entity.Usuario;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/mascotas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MascotaRS {
    private MascotaBO mascotaBO;
    private UsuarioBO usuarioBO;
    public MascotaRS(){
        this.mascotaBO  = new MascotaBOImpl() ;
        this.usuarioBO = new UsuarioBOImpl() ;
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

    @GET
    @Path("/veterinario/pacientes")
    public Response listarPacientesVeterinario(
            @QueryParam("searchText") String searchText,
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("recordsPerPage") @DefaultValue("10") int recordsPerPage) {
        try {
            PageResultDTO<PacienteVeterinarioDTO> resultado =
                    mascotaBO.listarPacientesVeterinarioPaginado(searchText, pageNumber, recordsPerPage);
            return Response.ok(resultado).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al listar pacientes\"}")
                    .build();
        }
    }

    @GET
    @Path("/veterinario/historial/{id}")
    public Response obtenerHistorialClinicoVeterinario(@PathParam("id") int mascotaId) {
        try {
            List<AtencionHistorialDTO> historial = mascotaBO.listarHistorialClinicoVeterinario(mascotaId);
            return Response.ok(historial).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener el historial clínico\"}")
                    .build();
        }
    }


    @Context
    private ServletContext context;

    @GET
    @Path("/historial/{id}/pdf")
    @Produces("application/pdf")
    public Response generarPdfHistorial(@PathParam("id") int mascotaId) {
        try {

            Mascota mascota = mascotaBO.obtenerPorId(mascotaId);
            if(mascota == null){
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Mascota no encontrada").build();
            }

            String propietario = "No registrado";
            String dniPropietario = "-";

            if (mascota.getCliente() != null && mascota.getCliente().getUsuario() != null) {
                int clienteId = mascota.getCliente().getUsuario().getId();

                Usuario u = usuarioBO.obtenerPorId(clienteId);


                if (u != null) {
                    String nombres = u.getNombres() != null ? u.getNombres() : "";
                    String apellidos = u.getApellidos() != null ? u.getApellidos() : "";
                    String nombreCompleto = (nombres + " " + apellidos).trim();
                    if (!nombreCompleto.isEmpty()) {
                        propietario = nombreCompleto;
                    }
                    if (u.getDni() != null && !u.getDni().isBlank()) {
                        dniPropietario = u.getDni();
                    }
                }
            }



            List<RegistroMedicoDTO> historial = mascotaBO.listarHistorialPorMascota(mascotaId);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(historial);

            InputStream reporteStream = this.getClass().getResourceAsStream("/REPORTES/HistorialMascota.jasper");
            if (reporteStream == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Plantilla Jasper no encontrada").build();
            }

            InputStream logoStream = this.getClass().getResourceAsStream("/REPORTES/logo.png");
            if (logoStream == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Logo no encontrado en /REPORTES/").build();
            }

            InputStream tituloStream = this.getClass().getResourceAsStream("/REPORTES/titulo_mimados.png");
            if (tituloStream == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Imagen no encontrada en /REPORTES/").build();
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String nombreMascota = mascota.getNombre() != null ? mascota.getNombre() : "-";
            String especie = mascota.getEspecie() != null ? mascota.getEspecie().name() : "-";
            String raza = (mascota.getRaza() != null && !mascota.getRaza().isBlank()) ? mascota.getRaza() : "No registrada";
            String sexo = mascota.getSexo() != null ? mascota.getSexo().name() : "-";
            String fechaNac = mascota.getFechaNacimiento() != null ? mascota.getFechaNacimiento().format(fmt) : "No registrada";

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("MIMADOS", "Historial Clínico Veterinario");
            parametros.put("TituloImg", tituloStream);

            parametros.put("Logo", logoStream);
            parametros.put("nombreMascota", nombreMascota);
            parametros.put("especie", especie);
            parametros.put("raza", raza);
            parametros.put("sexo", sexo);
            parametros.put("fechaNacimiento", fechaNac);
            parametros.put("propietario", propietario);
            parametros.put("dniPropietario", dniPropietario);



            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteStream, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=\"Historial_Mascota_" + mascotaId + ".pdf\"")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el PDF: " + e.getMessage())
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
