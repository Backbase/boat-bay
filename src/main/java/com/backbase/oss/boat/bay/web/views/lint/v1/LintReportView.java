package com.backbase.oss.boat.bay.web.views.lint.v1;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.LintReportRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.quay.model.BoatLintReport;
import java.util.Optional;
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
    private final LintReportRepository repository;
    private final BoatSpecLinter boatSpecLinter;
    private final LintReportMapper lintReportMapper;

    @GetMapping("/report/spec/{id}")
    public ResponseEntity<BoatLintReport> getLintReportForSpec(@PathVariable Long id) {

        Spec spec = specRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LintReport specReport = Optional.ofNullable(spec.getLintReport()).orElseGet(() -> boatSpecLinter.lint(spec) );
        boatSpecLinter.getApiValidator(spec);

        BoatLintReport lintReport = lintReportMapper.mapReport(specReport);

        return ResponseEntity.ok(lintReport);
    }

}