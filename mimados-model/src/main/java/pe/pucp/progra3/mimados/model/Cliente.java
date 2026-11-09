package pe.pucp.progra3.mimados.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Usuario usuario;
    private String direccion;
    private String telefonoEmergencia;
    private Boolean activo;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;


    private List<Mascota> mascotas;

    public Cliente() {
        this.mascotas = new ArrayList<>();
        this.activo = true;
    }

    public Cliente(Usuario usuario, String direccion, String telefonoEmergencia, Boolean activo) {
        this();
        this.usuario = usuario;
        this.direccion = direccion;
        this.telefonoEmergencia = telefonoEmergencia;
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }
}
