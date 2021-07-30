package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import java.util.List;
import java.util.Optional;

public interface BoatLintReportRepository extends LintReportRepository {
    Optional<LintReport> findBySpec(Spec spec);

    Optional<LintReport> findDistinctFirstBySpecProductOrderByLintedOn(Product product);

    Optional<LintReport> findDistinctFirstBySpecServiceDefinitionCapability(Capability capability);
}
