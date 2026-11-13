package pe.pucp.progra3.mimados.model.dto.response;

import java.io.Serializable;

public class AuthResultado implements Serializable {

    private int usuarioId;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String rol; // "Admin", "Veterinario", "Cliente"
    private boolean esAdmin;
    private boolean activo;
    private String codigoCmvp;
    private Integer sedeId;

    public AuthResultado() {
    }

    public AuthResultado(int usuarioId, String dni, String nombres, String apellidos,
                         String email, String rol, boolean esAdmin, boolean activo) {
        this.usuarioId = usuarioId;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
        this.esAdmin = esAdmin;
        this.activo = activo;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodigoCmvp() {
        return codigoCmvp;
    }

    public void setCodigoCmvp(String codigoCmvp) {
        this.codigoCmvp = codigoCmvp;
    }

    public Integer getSedeId() {
        return sedeId;
    }

    public void setSedeId(Integer sedeId) {
        this.sedeId = sedeId;
    }
}
