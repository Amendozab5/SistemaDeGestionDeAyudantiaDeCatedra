package com.uteq.sgaac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserAdminDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^09\\d{8}$", message = "El número de teléfono debe tener 10 dígitos y empezar con 09")
    private String telefono;
    
    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe tener exactamente 10 dígitos")
    private String cedula;

    @NotBlank(message = "El rol es obligatorio")
    private String rol; // "ADMIN", "DOCENTE", "ESTUDIANTE"

    @NotBlank(message = "El estado es obligatorio")
    private String estado; // "ACTIVA", "PENDIENTE", "SUSPENDIDA"

    private boolean forzarCambioPwd;

    // Getters y Setters
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public boolean isForzarCambioPwd() { return forzarCambioPwd; }
    public void setForzarCambioPwd(boolean forzarCambioPwd) { this.forzarCambioPwd = forzarCambioPwd; }
}
