package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aud_roles")
public class AudRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_roles_generator")
    @SequenceGenerator(name = "aud_roles_generator", sequenceName = "aud_roles_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_roles")
    private Integer idRoles;

    @Column(name = "nombres_roles")
    private String nombresRoles;

    @Column(name = "fecha_horareg", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHorareg;

    @Column(length = 50)
    private String usuario;

    @Column(length = 1)
    private String tipo;

    // Getters and Setters

    public Long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Long secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getIdRoles() {
        return idRoles;
    }

    public void setIdRoles(Integer idRoles) {
        this.idRoles = idRoles;
    }

    public String getNombresRoles() {
        return nombresRoles;
    }

    public void setNombresRoles(String nombresRoles) {
        this.nombresRoles = nombresRoles;
    }

    public Date getFechaHorareg() {
        return fechaHorareg;
    }

    public void setFechaHorareg(Date fechaHorareg) {
        this.fechaHorareg = fechaHorareg;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
