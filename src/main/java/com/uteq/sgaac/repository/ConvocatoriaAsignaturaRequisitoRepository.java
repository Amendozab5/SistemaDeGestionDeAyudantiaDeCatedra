package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.ConvocatoriaAsignaturaRequisito;
import com.uteq.sgaac.model.ConvocatoriaAsignaturaRequisitoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvocatoriaAsignaturaRequisitoRepository extends JpaRepository<ConvocatoriaAsignaturaRequisito, ConvocatoriaAsignaturaRequisitoId> {
}
