package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Requisito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisitoRepository extends JpaRepository<Requisito, Long> {
    List<Requisito> findByActivoTrue();
}