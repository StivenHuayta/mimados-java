package pe.pucp.progra3.mimados.model.DTO;

import pe.pucp.progra3.mimados.model.Sede;
import pe.pucp.progra3.mimados.model.Servicio;

import java.math.BigDecimal;

public class SedeServicioDTO {
    private Integer idSedeServicio;
    private Integer idSede;
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioReferencial;
    private Integer duracionMinutos;
    private Boolean activo;


    public SedeServicioDTO(Integer idSedeServicio, Integer idSede, Integer idServicio, String nombre,
                           String descripcion, BigDecimal precioReferencial, Integer duracionMinutos,
                           Boolean activo) {
        this.idSedeServicio = idSedeServicio;
        this.idSede = idSede;
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioReferencial = precioReferencial;
        this.duracionMinutos = duracionMinutos;
        this.activo = activo;
    }

    public Boolean getActivo() {return activo;}

    public void setActivo(Boolean activo) {this.activo = activo;}

    public Integer getDuracionMinutos() {return duracionMinutos;}

    public void setDuracionMinutos(Integer duracionMinutos) {this.duracionMinutos = duracionMinutos;}

    public BigDecimal getPrecioReferencial() {return precioReferencial;}

    public void setPrecioReferencial(BigDecimal precioReferencial) {this.precioReferencial = precioReferencial;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public Integer getIdServicio() {return idServicio;}

    public void setIdServicio(Integer idServicio) {this.idServicio = idServicio;
    }

    public Integer getIdSede() {return idSede;}

    public void setIdSede(Integer idSede) {this.idSede = idSede;}

    public Integer getIdSedeServicio() {return idSedeServicio;}

    public void setIdSedeServicio(Integer idSedeServicio) {this.idSedeServicio = idSedeServicio;}
}
