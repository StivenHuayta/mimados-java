package pe.pucp.progra3.mimados.model;

import java.time.LocalDateTime;

public class Usuario {
    private Integer id;
    private Cliente cliente;
    private Empleado empleado;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String passwordHash;
    private String telefono;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Usuario() {
    }

    public Usuario(Integer id, String dni, String nombres, String apellidos, String email, String telefono) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    public Usuario(String dni, String nombres, String apellidos, String email, String passwordHash, String telefono) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
    }

    public Usuario(String dni, String nombres,  String apellidos,  String email,  String accountName, String passwordHash,  String telefono) {

        if (dni == null || dni.length() != 8) {
            throw new IllegalArgumentException(
                    "El DNI debe tener 8 caracteres."
            );
        }

        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException(
                    "Los nombres son obligatorios."
            );
        }

        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException(
                    "Los apellidos son obligatorios."
            );
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException(
                    "El formato del correo es inválido."
            );
        }

        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio."
            );
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }

        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;


    }


    public Usuario(int id, String dni, String nombres, String apellidos, String email,
                   String accountName, String passwordHash, String telefono, boolean estado) {

        this(dni, nombres, apellidos, email, accountName, passwordHash, telefono);
        this.id = id;

    }


    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public void actualizarContacto(String nuevoTelefono,
                                   String nuevoEmail) {

        if (nuevoEmail != null) {
            if (!nuevoEmail.contains("@")) {
                throw new IllegalArgumentException(
                        "El formato del correo es inválido."
                );
            }

            this.email = nuevoEmail;
        }

        if (nuevoTelefono != null && !nuevoTelefono.isBlank()) {
            this.telefono = nuevoTelefono;
        }
    }


    public void cambiarPassword(String nuevoPasswordHash) {
        if (nuevoPasswordHash == null ||
                nuevoPasswordHash.isBlank()) {

            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía."
            );
        }

        this.passwordHash = nuevoPasswordHash;
    }

    public void setDni(String dni) {this.dni = dni;}
    public void setId(int id) { this.id = id;}
    public void setEmail(String email) {this.email = email;}
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public int getId() { return id; }
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getTelefono() { return telefono; }



    public void setNombres(String nombres) {

        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException(
                    "Los nombres son obligatorios."
            );
        }

        this.nombres = nombres;
    }


    public void setApellidos(String apellidos) {

        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException(
                    "Los apellidos son obligatorios."
            );
        }

        this.apellidos = apellidos;
    }
}
