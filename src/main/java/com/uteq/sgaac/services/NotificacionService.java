package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Notificacion;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Crea una nueva notificación para un usuario específico.
     * @param usuario El destinatario de la notificación.
     * @param mensaje El contenido del mensaje.
     * @param url La URL a la que se dirigirá al hacer clic (puede ser null).
     * @return La notificación guardada.
     */
    @Transactional
    public Notificacion crearNotificacion(usuario_sistema usuario, String mensaje, String url) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setUrl(url);
        notificacion.setLeida(false);
        return notificacionRepository.save(notificacion);
    }

    /**
     * Obtiene todas las notificaciones para un usuario, ordenadas de más nueva a más antigua.
     * @param usuario El usuario cuyas notificaciones se quieren obtener.
     * @return Una lista de notificaciones.
     */
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesDeUsuario(usuario_sistema usuario) {
        return notificacionRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    /**
     * Marca una notificación como leída.
     * @param idNotificacion El ID de la notificación a marcar.
     * @return Un Optional con la notificación actualizada si se encontró, o un Optional vacío si no.
     */
    @Transactional
    public Optional<Notificacion> marcarComoLeida(Long idNotificacion) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(idNotificacion);
        if (notificacionOpt.isPresent()) {
            Notificacion notificacion = notificacionOpt.get();
            if (!notificacion.isLeida()) {
                notificacion.setLeida(true);
                notificacionRepository.save(notificacion);
            }
            return Optional.of(notificacion);
        }
        return Optional.empty();
    }
}