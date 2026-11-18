package pe.pucp.progra3.mimados.model.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class AtencionHistorialDTO implements Serializable {

    private int atencionId;
    private int citaId;
    private String fechaHoraRegistro;
    private Double pesoActual;
    private Double temperatura;
    private String sintomas;
    private String diagnostico;
    private String tratamientoRecetado;
    private String observaciones;
    private BigDecimal montoTotal;
    private String servicioRealizado;
    private String nombreVeterinario;
    private String codigoCmvp;

    public AtencionHistorialDTO() {}

    public int getAtencionId() { return atencionId; }
    public void setAtencionId(int atencionId) { this.atencionId = atencionId; }

    public int getCitaId() { return citaId; }
    public void setCitaId(int citaId) { this.citaId = citaId; }

    public String getFechaHoraRegistro() { return fechaHoraRegistro; }
    public void setFechaHoraRegistro(String fechaHoraRegistro) { this.fechaHoraRegistro = fechaHoraRegistro; }

    public Double getPesoActual() { return pesoActual; }
    public void setPesoActual(Double pesoActual) { this.pesoActual = pesoActual; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamientoRecetado() { return tratamientoRecetado; }
    public void setTratamientoRecetado(String tratamientoRecetado) { this.tratamientoRecetado = tratamientoRecetado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getServicioRealizado() { return servicioRealizado; }
    public void setServicioRealizado(String servicioRealizado) { this.servicioRealizado = servicioRealizado; }

    public String getNombreVeterinario() { return nombreVeterinario; }
    public void setNombreVeterinario(String nombreVeterinario) { this.nombreVeterinario = nombreVeterinario; }

    public String getCodigoCmvp() { return codigoCmvp; }
    public void setCodigoCmvp(String codigoCmvp) { this.codigoCmvp = codigoCmvp; }
}
