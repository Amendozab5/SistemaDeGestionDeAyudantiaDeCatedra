package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Coordinador;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    Optional<Coordinador> findByUsuario(usuario_sistema usuario);
}
