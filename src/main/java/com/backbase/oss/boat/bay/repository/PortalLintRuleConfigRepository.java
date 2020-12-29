package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.PortalLintRuleConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PortalLintRuleConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortalLintRuleConfigRepository extends JpaRepository<PortalLintRuleConfig, Long> {
}
