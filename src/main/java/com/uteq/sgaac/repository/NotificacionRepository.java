package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Notificacion;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Busca todas las notificaciones para un usuario específico, ordenadas por fecha de creación descendente.
     * Esto nos permite mostrar siempre las notificaciones más recientes primero.
     * @param usuario El usuario para el cual se buscan las notificaciones.
     * @return Una lista de notificaciones.
     */
    List<Notificacion> findByUsuarioOrderByFechaCreacionDesc(usuario_sistema usuario);

}