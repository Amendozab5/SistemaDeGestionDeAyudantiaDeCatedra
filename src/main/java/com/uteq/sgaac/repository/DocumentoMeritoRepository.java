package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.DocumentoMerito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoMeritoRepository extends JpaRepository<DocumentoMerito, Long> {
}
