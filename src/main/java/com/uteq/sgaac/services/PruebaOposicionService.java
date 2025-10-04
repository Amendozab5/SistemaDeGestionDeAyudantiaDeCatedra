package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AgendarPruebaDTO;
import com.uteq.sgaac.dto.AgendarPruebaGrupoDTO;
import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.Postulacion;
import com.uteq.sgaac.model.PruebaOposicion;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.DocenteRepository;
import com.uteq.sgaac.repository.PostulacionRepository;
import com.uteq.sgaac.repository.PruebaOposicionRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PruebaOposicionService {

    private static final Logger logger = LoggerFactory.getLogger(PruebaOposicionService.class);

    private final PruebaOposicionRepository pruebaOposicionRepository;
    private final PostulacionRepository postulacionRepository;
    private final NotificacionService notificacionService;
    private final DocenteRepository docenteRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public PruebaOposicionService(PruebaOposicionRepository pruebaOposicionRepository,
                                  PostulacionRepository postulacionRepository,
                                  NotificacionService notificacionService,
                                  DocenteRepository docenteRepository,
                                  JavaMailSender mailSender,
                                  TemplateEngine templateEngine) {
        this.pruebaOposicionRepository = pruebaOposicionRepository;
        this.postulacionRepository = postulacionRepository;
        this.notificacionService = notificacionService;
        this.docenteRepository = docenteRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Transactional
    public PruebaOposicion agendarPrueba(AgendarPruebaDTO dto) {

        if (pruebaOposicionRepository.findByPostulacionId(dto.getPostulacionId()).isPresent()) {
            throw new RuntimeException("Ya existe una prueba de oposición agendada para esta postulación.");
        }

        Postulacion postulacion = postulacionRepository.findById(dto.getPostulacionId())
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada con id: " + dto.getPostulacionId()));

        if (postulacion.getTribunalAsignado() == null || postulacion.getTribunalAsignado().isEmpty()) {
            throw new IllegalStateException("No se puede agendar la prueba porque no se ha asignado un tribunal a la postulación.");
        }

        PruebaOposicion nuevaPrueba = new PruebaOposicion();
        nuevaPrueba.setPostulacion(postulacion);
        nuevaPrueba.setTitulo(dto.getTema());
        nuevaPrueba.setDescripcion(dto.getDescripcion());
        nuevaPrueba.setFechaOposicion(dto.getFechaHora());
        nuevaPrueba.setLugar(dto.getLugar());
        nuevaPrueba.setEstado("AGENDADA");
        nuevaPrueba.setTribunal(new HashSet<>(postulacion.getTribunalAsignado()));

        PruebaOposicion pruebaGuardada = pruebaOposicionRepository.save(nuevaPrueba);

        // In-app notification
        String mensaje = "Se ha agendado tu prueba de oposición para la asignatura de " + postulacion.getAsignatura().getNombre() + ".";
        String url = "/homeEstudiante?view=mis-pruebas";
        notificacionService.crearNotificacion(postulacion.getEstudiante().getUsuario(), mensaje, url);

        // Email notification
        try {
            sendEmailAgendamiento(postulacion.getEstudiante().getUsuario(), pruebaGuardada);
        } catch (MessagingException e) {
            logger.error("Error al enviar email de agendamiento para el usuario " + postulacion.getEstudiante().getUsuario().getEmail(), e);
        }

        return pruebaGuardada;
    }

    private void sendEmailAgendamiento(usuario_sistema usuario, PruebaOposicion prueba) throws MessagingException {
        Context context = new Context();
        context.setVariable("nombreEstudiante", usuario.getNombres() + " " + usuario.getApellidos());
        context.setVariable("asignatura", prueba.getPostulacion().getAsignatura().getNombre());
        context.setVariable("tema", prueba.getTitulo());
        context.setVariable("descripcion", prueba.getDescripcion());
        context.setVariable("fechaHora", prueba.getFechaOposicion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("lugar", prueba.getLugar());
        
        Set<Docente> tribunal = prueba.getTribunal();
        StringBuilder tribunalNombres = new StringBuilder();
        if (tribunal != null && !tribunal.isEmpty()) {
            for (Docente d : tribunal) {
                tribunalNombres.append(d.getUsuario().getNombres()).append(" ").append(d.getUsuario().getApellidos()).append(", ");
            }
            tribunalNombres.setLength(tribunalNombres.length() - 2); // Remove trailing comma and space
        } else {
            tribunalNombres.append("No asignado");
        }
        context.setVariable("tribunal", tribunalNombres.toString());

        String process = templateEngine.process("emails/notificacion-agendamiento", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        
        helper.setSubject("Agendamiento de Prueba de Oposición - SGAAC");
        helper.setTo(usuario.getEmail());
        helper.setText(process, true);
        
        mailSender.send(mimeMessage);
    }

    /**
     * Busca todas las pruebas de oposición de un estudiante.
     * @param estudiante El estudiante.
     * @return Una lista de sus pruebas de oposición.
     */
    @Transactional(readOnly = true)
    public List<PruebaOposicion> findByEstudiante(Estudiante estudiante) {
        return pruebaOposicionRepository.findByPostulacion_EstudianteOrderByFechaOposicionDesc(estudiante);
    }
}
