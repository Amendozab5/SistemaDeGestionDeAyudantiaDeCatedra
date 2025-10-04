package com.uteq.sgaac.services;

import com.uteq.sgaac.model.ActividadRefuerzo;
import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.ActividadRefuerzoRepository;
import com.uteq.sgaac.repository.AsignaturaRepository;
import com.uteq.sgaac.repository.EstudianteRepository;
import com.uteq.sgaac.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActividadRefuerzoService {

    @Autowired
    private ActividadRefuerzoRepository actividadRefuerzoRepository;

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    private final Path rootLocation = Paths.get("upload-dir/actividades-refuerzo");

    public ActividadRefuerzo createActividad(String titulo, String descripcion, String tipoActividad,
                                             MultipartFile archivo, String enlace, Long asignaturaId, String username) throws IOException {

        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Estudiante estudiante = estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado"));
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new IllegalStateException("Asignatura no encontrada"));

        ActividadRefuerzo actividad = new ActividadRefuerzo();
        actividad.setTitulo(titulo);
        actividad.setDescripcion(descripcion);
        actividad.setTipoActividad(tipoActividad);
        actividad.setFechaCreacion(LocalDateTime.now());
        actividad.setAyudante(estudiante);
        actividad.setAsignatura(asignatura);

        if ("enlace".equals(tipoActividad)) {
            actividad.setEnlace(enlace);
        } else {
            if (archivo != null && !archivo.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Files.createDirectories(rootLocation);
                Files.copy(archivo.getInputStream(), this.rootLocation.resolve(filename));
                actividad.setNombreArchivo(archivo.getOriginalFilename());
                actividad.setUrlArchivo("/" + rootLocation.resolve(filename).toString());
            }
        }

        return actividadRefuerzoRepository.save(actividad);
    }
}
