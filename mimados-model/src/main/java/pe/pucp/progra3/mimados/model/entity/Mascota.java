package pe.pucp.progra3.mimados.model.entity;

import pe.pucp.progra3.mimados.model.enums.Especie;
import pe.pucp.progra3.mimados.model.enums.Sexo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mascota {
    private Integer id;
    private Cliente cliente;
    private String nombre;
    private Especie especie;
    private String raza;
    private Sexo sexo;
    private LocalDate fechaNacimiento;
    private Double pesoReferencial;
    private Boolean activo;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<Cita> citas;

    public Mascota() {
        this.citas = new ArrayList<>();
        this.activo = true;
    }

    public Mascota(Integer id, Cliente cliente, String nombre, Especie especie, String raza, Sexo sexo, LocalDate fechaNacimiento, Double pesoReferencial, Boolean activo) {
        this();
        this.id = id;
        this.cliente = cliente;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoReferencial = pesoReferencial;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Double getPesoReferencial() {
        return pesoReferencial;
    }

    public void setPesoReferencial(Double pesoReferencial) {
        this.pesoReferencial = pesoReferencial;
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

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}

