package pe.pucp.progra3.mimados.model.DTO;

import java.io.Serializable;

public class UsuarioPerfilDTO implements Serializable {
    private Integer id;
    private String dni;
    private String email;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String contactoEmergencia;

    public UsuarioPerfilDTO() {}

    // Constructor para facilitar el mapeo
    public UsuarioPerfilDTO(Integer id, String dni, String email, String nombres,
                            String apellidos, String telefono, String direccion,
                            String contactoEmergencia) {
        this.id = id;
        this.dni = dni;
        this.email = email;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.contactoEmergencia = contactoEmergencia;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }
}
