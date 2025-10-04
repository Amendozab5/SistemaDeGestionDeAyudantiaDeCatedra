package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.*;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConvocatoriaService {

    private static final Logger logger = LoggerFactory.getLogger(ConvocatoriaService.class);

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private DocenteRepository docenteRepository; 

    @Autowired
    private ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository;

    @Autowired
    private RequisitoRepository requisitoRepository;

    @Transactional
    public void crearPlazas(CrearPlazasForm form, Coordinador coordinador) {
        Convocatoria convocatoria = convocatoriaRepository.findById(form.getConvocatoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Convocatoria no encontrada con ID: " + form.getConvocatoriaId()));

        for (ConvocatoriaAsignaturaItemDTO item : form.getAsignaturas()) {
            Asignatura asignatura = asignaturaRepository.findById(item.getAsignaturaId())
                    .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada con ID: " + item.getAsignaturaId()));
            
            if (convocatoriaAsignaturaRepository.existsByConvocatoriaAndAsignatura(convocatoria, asignatura)) {
                throw new IllegalStateException("La plaza para la asignatura '" + asignatura.getNombre() + "' en esta convocatoria ya existe.");
            }

            Docente docente = docenteRepository.findById(item.getDocenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con ID: " + item.getDocenteId()));

            ConvocatoriaAsignatura nuevaPlaza = new ConvocatoriaAsignatura();
            nuevaPlaza.setConvocatoria(convocatoria);
            nuevaPlaza.setAsignatura(asignatura);
            nuevaPlaza.setDocente(docente);
            nuevaPlaza.setCoordinador(coordinador);
            nuevaPlaza.setCupos(item.getCupos());
            nuevaPlaza.setFechaInicioPostulacion(item.getFechaInicioPostulacion());
            nuevaPlaza.setFechaFinPostulacion(item.getFechaFinPostulacion());
            nuevaPlaza.setAprobadoDecano(false); // Por defecto no está aprobada

            convocatoriaAsignaturaRepository.save(nuevaPlaza);
        }
    }

    @Transactional(readOnly = true)
    public List<ConvocatoriaDTO> getAllConvocatorias() {
        List<Convocatoria> convocatorias = convocatoriaRepository.findAllWithDetails();
        return convocatorias.stream().map(this::toConvocatoriaDTO).collect(Collectors.toList());
    }

    private ConvocatoriaDTO toConvocatoriaDTO(Convocatoria convocatoria) {
        List<ConvocatoriaAsignaturaDTO> asignaturaDTOs = convocatoria.getConvocatoriaAsignaturas().stream()
                .map(this::toConvocatoriaAsignaturaDTO)
                .collect(Collectors.toList());

        return new ConvocatoriaDTO(
                convocatoria.getIdConvocatoria(),
                convocatoria.getTitulo(),
                convocatoria.getDescripcion(),
                convocatoria.getPeriodo(),
                convocatoria.getFechaInicio(),
                convocatoria.getFechaFin(),
                convocatoria.getEstado(),
                asignaturaDTOs
        );
    }

    private ConvocatoriaAsignaturaDTO toConvocatoriaAsignaturaDTO(ConvocatoriaAsignatura ca) {
        AsignaturaDTO asignaturaDTO = new AsignaturaDTO(ca.getAsignatura().getIdAsignatura(), ca.getAsignatura().getNombre());
        return new ConvocatoriaAsignaturaDTO(ca.getCupos(), asignaturaDTO);
    }


    public Optional<Convocatoria> getConvocatoriaById(Integer id) {
        return convocatoriaRepository.findById(id.longValue());
    }

    @Transactional
    public Convocatoria save(Convocatoria convocatoria) {
        return convocatoriaRepository.save(convocatoria);
    }

    @Transactional
    public Convocatoria save(Convocatoria convocatoria, List<Long> asignaturasId, List<Long> requisitosId) {
        Convocatoria savedConvocatoria = convocatoriaRepository.save(convocatoria);

        convocatoriaAsignaturaRepository.deleteByConvocatoria(savedConvocatoria);

        Set<Requisito> requisitos = new HashSet<>();
        if (requisitosId != null && !requisitosId.isEmpty()) {
            requisitos.addAll(requisitoRepository.findAllById(requisitosId));
        }

        if (asignaturasId != null && !asignaturasId.isEmpty()) {
            for (Long asignaturaId : asignaturasId) {
                Optional<Asignatura> asignaturaOptional = asignaturaRepository.findById(asignaturaId);
                if (asignaturaOptional.isPresent()) {
                    Asignatura asignatura = asignaturaOptional.get();
                    ConvocatoriaAsignatura convocatoriaAsignatura = new ConvocatoriaAsignatura();
                    convocatoriaAsignatura.setConvocatoria(savedConvocatoria);
                    convocatoriaAsignatura.setAsignatura(asignatura);
                    convocatoriaAsignatura.setCupos(1); // Default value, can be made configurable

                    convocatoriaAsignatura.setRequisitos(requisitos);

                    convocatoriaAsignaturaRepository.save(convocatoriaAsignatura);
                }
            }
        }
        return savedConvocatoria;
    }

    public void deleteConvocatoria(Integer id) {
        convocatoriaRepository.deleteById(id.longValue());
    }

    public List<PlazaPendienteDTO> findPlazasPendientesPorFacultad(Facultad facultad) {
        List<ConvocatoriaAsignatura> plazas = convocatoriaAsignaturaRepository.findPlazasPendientesByFacultad(facultad);
        return plazas.stream().map(plaza -> {
            String coordinadorNombre = "N/A";
            if (plaza.getCoordinador() != null && plaza.getCoordinador().getUsuario() != null) {
                coordinadorNombre = plaza.getCoordinador().getUsuario().getNombres() + " " + plaza.getCoordinador().getUsuario().getApellidos();
            }

            String docenteNombre = "N/A";
            if (plaza.getDocente() != null && plaza.getDocente().getUsuario() != null) {
                docenteNombre = plaza.getDocente().getUsuario().getNombres() + " " + plaza.getDocente().getUsuario().getApellidos();
            }

            return new PlazaPendienteDTO(
                    plaza.getId(),
                    plaza.getConvocatoria().getTitulo(),
                    coordinadorNombre,
                    plaza.getAsignatura().getNombre(),
                    docenteNombre,
                    plaza.getCupos()
            );
        }).collect(Collectors.toList());
    }

    public List<PlazaPendienteDTO> findPlazasAprobadasPorFacultad(Facultad facultad) {
        List<ConvocatoriaAsignatura> plazas = convocatoriaAsignaturaRepository.findPlazasAprobadasByFacultad(facultad);
        return plazas.stream().map(plaza -> {
            String coordinadorNombre = "N/A";
            if (plaza.getCoordinador() != null && plaza.getCoordinador().getUsuario() != null) {
                coordinadorNombre = plaza.getCoordinador().getUsuario().getNombres() + " " + plaza.getCoordinador().getUsuario().getApellidos();
            }

            String docenteNombre = "N/A";
            if (plaza.getDocente() != null && plaza.getDocente().getUsuario() != null) {
                docenteNombre = plaza.getDocente().getUsuario().getNombres() + " " + plaza.getDocente().getUsuario().getApellidos();
            }

            return new PlazaPendienteDTO(
                    plaza.getId(),
                    plaza.getConvocatoria().getTitulo(),
                    coordinadorNombre,
                    plaza.getAsignatura().getNombre(),
                    docenteNombre,
                    plaza.getCupos()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void aprobarPlaza(Long id) {
        convocatoriaAsignaturaRepository.findById(id).ifPresent(plaza -> {
            plaza.setAprobadoDecano(true);
            convocatoriaAsignaturaRepository.save(plaza);
        });
    }

    @Transactional
    public void rechazarPlaza(Long id) {
        convocatoriaAsignaturaRepository.deleteById(id);
    }

    public List<Convocatoria> findConvocatoriasListasParaPublicar() {
        List<Convocatoria> convocatorias = convocatoriaRepository.findListasParaPublicar(false);
        logger.info("Buscando convocatorias listas para publicar. Encontradas: {}", convocatorias.size());
        return convocatorias;
    }

    @Transactional
    public void publicarConvocatoria(Long id, LocalDate fechaInicio, LocalDate fechaFin) {
        convocatoriaRepository.findById(id).ifPresent(convocatoria -> {
            if (!convocatoria.getEstado()) {
                convocatoria.setEstado(true);
                convocatoria.setFechaInicio(fechaInicio);
                convocatoria.setFechaFin(fechaFin);
                convocatoriaRepository.save(convocatoria);
            }
        });
    }
}