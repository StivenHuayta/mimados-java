package pe.pucp.progra3.mimados.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioReferencial;
    private Integer duracionMinutos;
    private Boolean activo;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<PuestoServicio> puestoServicios;

    public Servicio(Integer id ,String nombre, String descripcion, BigDecimal precioReferencial, Integer duracionMinutos, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioReferencial = precioReferencial;
        this.duracionMinutos = duracionMinutos;
        this.activo = activo;
    }

    public Servicio() {
        this.puestoServicios = new ArrayList<>();
        this.activo = true;
    }

    public Servicio(Integer id) {
        this.id = id;
    }

    public Servicio(String nombre, String descripcion, BigDecimal precioReferencial, Integer duracionMinutos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioReferencial = precioReferencial;
        this.duracionMinutos = duracionMinutos;
    }

    public Servicio(String nombre, BigDecimal precioReferencial, Integer duracionMinutos) {
        this();
        this.nombre = nombre;
        this.precioReferencial = precioReferencial;
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecioReferencial() {
        return precioReferencial;
    }

    public void setPrecioReferencial(BigDecimal precioReferencial) {
        this.precioReferencial = precioReferencial;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public List<PuestoServicio> getPuestoServicios() {
        return puestoServicios;
    }

    public void setPuestoServicios(List<PuestoServicio> puestoServicios) {
        this.puestoServicios = puestoServicios;
    }
}
