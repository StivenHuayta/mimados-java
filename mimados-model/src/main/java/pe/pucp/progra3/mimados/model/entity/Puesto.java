package pe.pucp.progra3.mimados.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Puesto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<PuestoServicio> puestoServicios;

    public Puesto() {
        this.puestoServicios = new ArrayList<>();
    }

    public Puesto(Integer id, String nombre, String descripcion) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Puesto(String nombre, String descripcion) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
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

