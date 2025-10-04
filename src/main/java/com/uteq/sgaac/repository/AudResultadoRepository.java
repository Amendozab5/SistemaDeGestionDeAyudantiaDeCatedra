package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudResultadoRepository extends JpaRepository<AudResultado, Long>, JpaSpecificationExecutor<AudResultado> {
}
