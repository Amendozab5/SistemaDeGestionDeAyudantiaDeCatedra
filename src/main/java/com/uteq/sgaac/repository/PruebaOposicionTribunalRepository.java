package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.model.PruebaOposicion;
import com.uteq.sgaac.model.PruebaOposicionTribunal;
import com.uteq.sgaac.model.PruebaOposicionTribunalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PruebaOposicionTribunalRepository extends JpaRepository<PruebaOposicionTribunal, PruebaOposicionTribunalId> {
    List<PruebaOposicionTribunal> findByDocente(Docente docente);
    long countByPruebaOposicion(PruebaOposicion pruebaOposicion);
}
