package pe.pucp.progra3.mimados.model.DTO;

import java.math.BigDecimal;
// Usamos String para la fecha para que el JSON viaje formateado y Blazor lo lea fácil
public class RegistroMedicoDTO {

    private String fecha;
    private String veterinario;
    private String servicio;
    private String notas;
    private BigDecimal peso;

    public RegistroMedicoDTO() {}

    // Getters y Setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getVeterinario() { return veterinario; }
    public void setVeterinario(String veterinario) { this.veterinario = veterinario; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
}