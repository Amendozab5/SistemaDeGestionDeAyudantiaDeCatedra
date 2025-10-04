package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {

    public static Specification<usuario_sistema> findByCriteria(String q, String rol, String estado) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(q)) {
                String pattern = "%" + q.toLowerCase() + "%";
                Predicate queryPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nombres")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern)
                );
                predicates.add(queryPredicate);
            }

            if (StringUtils.hasText(rol)) {
                predicates.add(criteriaBuilder.equal(root.get("rol").get("nombre"), rol));
            }

            if (StringUtils.hasText(estado)) {
                switch (estado) {
                    case "ACTIVA":
                        predicates.add(criteriaBuilder.isTrue(root.get("activo")));
                        predicates.add(criteriaBuilder.isTrue(root.get("confirmado")));
                        break;
                    case "PENDIENTE":
                        predicates.add(criteriaBuilder.isTrue(root.get("activo")));
                        predicates.add(criteriaBuilder.isFalse(root.get("confirmado")));
                        break;
                    case "SUSPENDIDA":
                        predicates.add(criteriaBuilder.isFalse(root.get("activo")));
                        break;
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
