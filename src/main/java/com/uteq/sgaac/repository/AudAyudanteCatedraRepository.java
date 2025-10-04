package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudAyudanteCatedra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudAyudanteCatedraRepository extends JpaRepository<AudAyudanteCatedra, Long>, JpaSpecificationExecutor<AudAyudanteCatedra> {
}
