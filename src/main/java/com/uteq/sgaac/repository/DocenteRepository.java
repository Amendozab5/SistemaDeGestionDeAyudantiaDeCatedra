package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    List<Docente> findByAsignatura_IdAsignatura(Long asignaturaId);

    Optional<Docente> findByUsuario(usuario_sistema usuario);

    List<Docente> findByCarrera(Carrera carrera);
}
