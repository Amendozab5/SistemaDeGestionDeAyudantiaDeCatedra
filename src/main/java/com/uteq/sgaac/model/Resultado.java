package com.uteq.sgaac.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "resultado")
@Data
@NoArgsConstructor
public class Resultado {

    @Id
    @Column(name = "id_postulacion")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_postulacion")
    private Postulacion postulacion;

    @Column(name = "puntaje_total_final", precision = 6, scale = 2)
    private BigDecimal puntajeTotalFinal;

    @Column(name = "estado_final", nullable = false)
    private String estadoFinal;

    @Version
    private Long version;

    public Resultado(Postulacion postulacion) {
        this.postulacion = postulacion;
        this.id = postulacion.getIdPostulacion();
    }
}
