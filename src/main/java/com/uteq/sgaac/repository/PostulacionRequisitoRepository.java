package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.PostulacionRequisito;
import com.uteq.sgaac.model.PostulacionRequisitoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRequisitoRepository extends JpaRepository<PostulacionRequisito, PostulacionRequisitoId> {
}
