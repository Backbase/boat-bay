package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.PortalLintRuleSet;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PortalLintRuleSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortalLintRuleSetRepository extends JpaRepository<PortalLintRuleSet, Long> {
}
