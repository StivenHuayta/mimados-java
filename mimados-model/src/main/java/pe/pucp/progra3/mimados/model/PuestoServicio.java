package pe.pucp.progra3.mimados.model;

import java.time.LocalDateTime;

public class PuestoServicio {
    private Integer id;
    private Servicio servicio;
    private Puesto puesto;
    private Boolean activo;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public PuestoServicio(Integer id,
                          int id_servicio,
                          int id_puesto,
                          Boolean activo,
                          String usuarioCreacion,
                          String usuarioModificacion,
                          LocalDateTime fechaCreacion,
                          LocalDateTime fechaModificacion) {
        this.id = id;
        this.servicio = new Servicio();
        this.servicio.setId(id_servicio);
        this.puesto = new Puesto();
        this.puesto.setId(id_puesto);
        this.activo = activo;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }


    public PuestoServicio(Integer id, int id_servicio, int id_puesto, Boolean activo) {
        this.id = id;
        this.servicio.setId(id_servicio);
        this.puesto.setId(id_puesto);
        this.activo = activo;
    }




    public PuestoServicio() {
        this.activo = true;
    }

    public PuestoServicio(Servicio servicio, Puesto puesto) {
        this();
        this.servicio = servicio;
        this.puesto = puesto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
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
}
