package pe.pucp.progra3.mimados.model.dto.response;

import java.io.Serializable;

public class EmpleadoResponseDTO implements Serializable {
    private Integer id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private Integer sedeId;
    private String codigoCmvp;
    private Boolean esAdmin;
    private Boolean esVeterinario;
    private Boolean activo;

    public EmpleadoResponseDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getSedeId() { return sedeId; }
    public void setSedeId(Integer sedeId) { this.sedeId = sedeId; }

    public String getCodigoCmvp() { return codigoCmvp; }
    public void setCodigoCmvp(String codigoCmvp) { this.codigoCmvp = codigoCmvp; }

    public Boolean getEsAdmin() { return esAdmin; }
    public void setEsAdmin(Boolean esAdmin) { this.esAdmin = esAdmin; }

    public Boolean getEsVeterinario() { return esVeterinario; }
    public void setEsVeterinario(Boolean esVeterinario) { this.esVeterinario = esVeterinario; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
