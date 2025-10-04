package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<usuario_sistema, Long> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que puede contener el usuario si se encuentra.
     */
    Optional<usuario_sistema> findByUsername(String username);

}
