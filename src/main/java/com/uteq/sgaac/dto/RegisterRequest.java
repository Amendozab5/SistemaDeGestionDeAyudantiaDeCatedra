package com.uteq.sgaac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;

public class RegisterRequest {

    // email
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Pattern(
        regexp = "(?i)^[A-Za-z0-9._%+-]+@uteq\\.edu\\.ec$",
        message = "Solo se permiten correos institucionales @uteq.edu.ec"
    )
    private String email;

    // nombres
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    // apellidos
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    // cedula
    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe tener exactamente 10 dígitos")
    private String cedula;

    // telefono
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^09\\d{8}$", message = "El número de teléfono debe tener 10 dígitos y empezar con 09")
    private String telefono;

    // token_confirmacion → generado automáticamente

    // confirmado → valor por defecto `false`, se asigna internamente

    // Getters y Setters

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
