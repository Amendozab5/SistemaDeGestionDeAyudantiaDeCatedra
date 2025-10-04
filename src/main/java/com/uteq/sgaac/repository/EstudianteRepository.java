package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuario(usuario_sistema usuario);

    @Query("SELECT AVG(e.promedioGeneral) FROM Estudiante e WHERE e.carrera.id = :carreraId")
    BigDecimal findAveragePromedioGeneralByCarrera(@Param("carreraId") Long carreraId);
}
