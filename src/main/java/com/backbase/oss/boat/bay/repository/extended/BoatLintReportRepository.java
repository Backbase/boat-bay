package com.backbase.oss.boat.bay.repository.extended;


import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import java.util.Optional;

public interface BoatLintReportRepository extends LintReportRepository  {

    Optional<LintReport> findBySpec(Spec spec);
}
