package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudRolesRepository extends JpaRepository<AudRoles, Long>, JpaSpecificationExecutor<AudRoles> {
}
