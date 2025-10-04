package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByFacultad_Id(Long id);
}
