package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Postulacion;
import com.uteq.sgaac.model.PostulacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostulacionDocumentoRepository extends JpaRepository<PostulacionDocumento, Long> {
    Optional<PostulacionDocumento> findByPostulacionAndTipoDocumento(Postulacion postulacion, String tipoDocumento);
}
