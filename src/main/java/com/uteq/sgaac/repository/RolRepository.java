package com.uteq.sgaac.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uteq.sgaac.model.roles;

public interface RolRepository extends JpaRepository<roles, Long> {
    Optional<roles> findByNombre(String nombre);
}
