package pe.pucp.progra3.mimados.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Atencion {
    private Integer id;
    private Cita cita;
    private Empleado empleado;
    private LocalDateTime fechaHoraRegistro;
    private Double pesoActual;
    private Double temperatura;
    private String sintomas;
    private String diagnostico;
    private String tratamientoRecetado;
    private String observaciones;
    private BigDecimal montoTotal;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Atencion() {}

    public Atencion(Integer id, Cita cita, Empleado empleado, LocalDateTime fechaHoraRegistro, Double pesoActual, Double temperatura, String sintomas, String diagnostico, String tratamientoRecetado, String observaciones, BigDecimal montoTotal) {
        this.id = id;
        this.cita = cita;
        this.empleado = empleado;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.pesoActual = pesoActual;
        this.temperatura = temperatura;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamientoRecetado = tratamientoRecetado;
        this.observaciones = observaciones;
        this.montoTotal = montoTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(Double pesoActual) {
        this.pesoActual = pesoActual;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamientoRecetado() {
        return tratamientoRecetado;
    }

    public void setTratamientoRecetado(String tratamientoRecetado) {
        this.tratamientoRecetado = tratamientoRecetado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
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

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

