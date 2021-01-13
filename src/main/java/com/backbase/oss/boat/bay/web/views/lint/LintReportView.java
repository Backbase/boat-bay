package com.backbase.oss.boat.bay.web.views.lint;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/lint")
@Transactional
@RequiredArgsConstructor
public class LintReportView {

    private final SpecRepository specRepository;
    private final BoatLintReportRepository repository;
    private final BoatSpecLinter boatSpecLinter;
    private final LintReportMapper lintReportMapper;

    @GetMapping("/report")
    public ResponseEntity<List<BoatLintReport>> getLintReports() {
        List<BoatLintReport> reports = repository.findAll().stream()
            .map(lintReportMapper::mapReportWithoutViolations)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/report/spec/{id}")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable Long id) {

        Spec spec = specRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport = Optional.ofNullable(spec.getLintReport()).orElseGet(() -> boatSpecLinter.lint(spec) );
        boatSpecLinter.getApiValidator(spec);

        BoatLintReport lintReport = lintReportMapper.mapReport(specReport);

        return ResponseEntity.ok(lintReport);
    }

}
