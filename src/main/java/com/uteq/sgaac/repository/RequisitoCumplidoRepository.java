package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.RequisitoCumplido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitoCumplidoRepository extends JpaRepository<RequisitoCumplido, Long> {
}