package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudEvaluacionMeritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudEvaluacionMeritosRepository extends JpaRepository<AudEvaluacionMeritos, Long>, JpaSpecificationExecutor<AudEvaluacionMeritos> {
}
