package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.PortalLintRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PortalLintRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortalLintRuleRepository extends JpaRepository<PortalLintRule, Long> {
}
