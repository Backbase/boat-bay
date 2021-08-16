package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.*;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import java.util.List;
import java.util.Optional;

public interface BoatLintReportRepository extends LintReportRepository {
    Optional<LintReport> findBySpec(Spec spec);

    Optional<LintReport> findDistinctFirstBySpecProductOrderByLintedOn(Product product);

    Optional<LintReport> findDistinctFirstBySpecProductPortalOrderByLintedOn(Portal portal);


    Optional<LintReport> findDistinctFirstBySpecServiceDefinitionCapability(Capability capability);
}
