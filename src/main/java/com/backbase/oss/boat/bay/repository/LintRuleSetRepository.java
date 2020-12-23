package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.LintRuleSet;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LintRuleSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LintRuleSetRepository extends JpaRepository<LintRuleSet, Long> {
}
