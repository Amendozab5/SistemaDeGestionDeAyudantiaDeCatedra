package com.uteq.sgaac.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia_estudiante", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_estudiante", "id_asignatura", "fecha_sesion"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_asistencia;

    @ManyToOne
    @JoinColumn(name = "id_ayudante", nullable = false)
    private AyudanteCatedra ayudante;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_asignatura", nullable = false)
    private Asignatura asignatura;

    @Column(name = "fecha_sesion", nullable = false)
    private LocalDateTime fechaSesion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AsistenciaEstadoEnum estado;

    @Column(name = "observaciones")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
