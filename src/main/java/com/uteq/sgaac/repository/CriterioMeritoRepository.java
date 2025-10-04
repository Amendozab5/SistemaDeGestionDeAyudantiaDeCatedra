package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.CriterioMerito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriterioMeritoRepository extends JpaRepository<CriterioMerito, Long> {
    List<CriterioMerito> findByActivoTrue();
}
