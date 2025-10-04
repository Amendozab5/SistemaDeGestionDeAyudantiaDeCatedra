package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudPostulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudPostulacionRepository extends JpaRepository<AudPostulacion, Long>, JpaSpecificationExecutor<AudPostulacion> {
}
