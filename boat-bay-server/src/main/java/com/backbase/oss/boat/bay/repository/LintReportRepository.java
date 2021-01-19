package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.LintReport;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LintReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LintReportRepository extends JpaRepository<LintReport, Long> {
}
