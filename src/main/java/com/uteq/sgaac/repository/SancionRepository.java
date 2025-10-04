package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SancionRepository extends JpaRepository<Sancion, Long> {

    List<Sancion> findByEstudianteIdEstudianteAndActiva(Long estudianteId, boolean activa);

}
