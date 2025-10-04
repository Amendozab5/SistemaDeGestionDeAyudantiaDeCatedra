package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<usuario_sistema, Long>, JpaSpecificationExecutor<usuario_sistema> {

    Optional<usuario_sistema> findByUsername(String username);

    Optional<usuario_sistema> findByEmailIgnoreCase(String email);

    Optional<usuario_sistema> findByUsernameAndActivoTrue(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsername(String username);

    boolean existsByCedula(String cedula);

    Optional<usuario_sistema> findByTokenConfirmacion(String token);

    List<usuario_sistema> findByRolNombre(String nombreRol);

    List<usuario_sistema> findByRolNombreIn(List<String> roles);
}
