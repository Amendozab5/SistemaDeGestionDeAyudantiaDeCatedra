package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Integer> {
}
