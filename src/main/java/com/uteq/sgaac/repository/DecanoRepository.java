package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Decano;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DecanoRepository extends JpaRepository<Decano, Long> {
    Optional<Decano> findByUsuario(usuario_sistema usuario);
}
