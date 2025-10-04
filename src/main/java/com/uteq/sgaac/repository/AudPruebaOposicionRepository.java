package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudPruebaOposicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudPruebaOposicionRepository extends JpaRepository<AudPruebaOposicion, Long>, JpaSpecificationExecutor<AudPruebaOposicion> {
}
