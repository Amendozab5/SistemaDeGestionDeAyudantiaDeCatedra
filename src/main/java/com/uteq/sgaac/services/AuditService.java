package com.uteq.sgaac.services;

import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired private AudActividadAyudanteRepository audActividadAyudanteRepository;
    @Autowired private AudAyudanteCatedraRepository audAyudanteCatedraRepository;
    @Autowired private AudEvaluacionMeritosRepository audEvaluacionMeritosRepository;
    @Autowired private AudPostulacionRepository audPostulacionRepository;
    @Autowired private AudPruebaOposicionRepository audPruebaOposicionRepository;
    @Autowired private AudResultadoRepository audResultadoRepository;
    @Autowired private AudRolesRepository audRolesRepository;

    // Generic Specification Builder
    private <T> Specification<T> buildSpecification(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaHorareg"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("fechaHorareg"), endDate.plusDays(1).atStartOfDay()));
            }
            if (StringUtils.hasText(usuario)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("usuario")), "%" + usuario.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(tipo)) {
                predicates.add(criteriaBuilder.equal(root.get("tipo"), tipo));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    public List<AudActividadAyudante> findActividadAyudanteAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudActividadAyudante> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audActividadAyudanteRepository.findAll(spec);
    }

    public List<AudAyudanteCatedra> findAyudanteCatedraAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudAyudanteCatedra> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audAyudanteCatedraRepository.findAll(spec);
    }

    public List<AudEvaluacionMeritos> findEvaluacionMeritosAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudEvaluacionMeritos> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audEvaluacionMeritosRepository.findAll(spec);
    }

    public List<AudPostulacion> findPostulacionAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudPostulacion> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audPostulacionRepository.findAll(spec);
    }

    public List<AudPruebaOposicion> findPruebaOposicionAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudPruebaOposicion> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audPruebaOposicionRepository.findAll(spec);
    }

    public List<AudResultado> findResultadoAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudResultado> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audResultadoRepository.findAll(spec);
    }

    public List<AudRoles> findRolesAudit(LocalDate startDate, LocalDate endDate, String usuario, String tipo) {
        Specification<AudRoles> spec = buildSpecification(startDate, endDate, usuario, tipo);
        return audRolesRepository.findAll(spec);
    }
}