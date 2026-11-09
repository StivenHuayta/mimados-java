package pe.pucp.progra3.mimados.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CitaServicio {
    private Cita cita;
    private SedeServicio sedeServicio;
    private BigDecimal precioAplicado;
    private String notas;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public CitaServicio() {}


    public CitaServicio(int id_cita,
                        int id_sedeServicio,
                        BigDecimal precioAplicado,
                        String notas,
                        String usuarioCreacion,
                        String usuarioModificacion,
                        LocalDateTime fechaCreacion,
                        LocalDateTime fechaModificacion) {

        this.cita = new Cita();
        this.cita.setId(id_cita);

        this.sedeServicio = new SedeServicio();
        this.sedeServicio.setId(id_sedeServicio);

        this.precioAplicado = precioAplicado;
        this.notas = notas;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public CitaServicio(Cita cita, SedeServicio sedeServicio, BigDecimal precioAplicado, String notas) {
        this.cita = cita;
        this.sedeServicio = sedeServicio;
        this.precioAplicado = precioAplicado;
        this.notas = notas;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public SedeServicio getSedeServicio() {
        return sedeServicio;
    }

    public void setSedeServicio(SedeServicio sedeServicio) {
        this.sedeServicio = sedeServicio;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public void setPrecioAplicado(BigDecimal precioAplicado) {
        this.precioAplicado = precioAplicado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
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
