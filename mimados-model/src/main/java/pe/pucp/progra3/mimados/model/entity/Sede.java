package pe.pucp.progra3.mimados.model.entity;

import java.time.LocalDateTime;

public class Sede {
    private Integer id;
    private String nombre;
    private String direccion;
    private String telefono;
    private Boolean activo;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;


    public Sede(int id, String nombre, String direccion, String telefono, boolean activo, String usuarioCreacion, String usuarioModificacion, LocalDateTime fechaModificacion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Sede(int id, String nombre, String direccion, String telefono, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
    }

    public Sede(String nombre, String direccion, String telefono, boolean activo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
    }

    public Sede(Integer id) {
        this.id = id;
    }

    public Sede() {
        this.activo = true;
    }

    public Sede(String nombre, String direccion, String telefono) {
        this();
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

