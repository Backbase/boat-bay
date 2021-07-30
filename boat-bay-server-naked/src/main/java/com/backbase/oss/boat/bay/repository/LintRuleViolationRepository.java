package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LintRuleViolation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LintRuleViolationRepository extends JpaRepository<LintRuleViolation, Long> {}
