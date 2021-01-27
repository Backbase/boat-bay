package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatCapabilityRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatDashboardRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintReportRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatProductRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatServiceRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecQuerySpecs;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatTagRepository;
import com.backbase.oss.boat.bay.service.lint.BoatSpecLinter;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsCollector;
import com.backbase.oss.boat.bay.web.views.dashboard.config.BoatCacheManager;
import com.backbase.oss.boat.bay.web.views.dashboard.diff.DiffReportRenderer;
import com.backbase.oss.boat.bay.web.views.dashboard.mapper.BoatDashboardMapper;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatCapability;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatLintReport;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatLintRule;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortal;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatPortalDashboard;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatProduct;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatProductRelease;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatService;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatSpec;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatTag;
import com.backbase.oss.boat.bay.web.views.dashboard.models.BoatViolation;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import com.backbase.oss.codegen.doc.BoatDocsGenerator;
import io.github.jhipster.web.util.PaginationUtil;
import io.swagger.v3.oas.models.OpenAPI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.zally.rule.api.Severity;


@Controller
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoatDocumentationController {

    public static final String VIEWS = "views";
    public static final String TAGS = "tags";

    private final BoatSpecRepository boatSpecRepository;


    @GetMapping("/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}/documentation")
    public ModelAndView getSpecAsOpenAPI(@PathVariable String portalKey,
                                                     @PathVariable String productKey,
                                                     @PathVariable String capabilityKey,
                                                     @PathVariable String serviceKey,
                                                     @PathVariable String specKey,
                                                     @PathVariable String version) {

        Spec spec = boatSpecRepository.findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
            portalKey,productKey,capabilityKey,serviceKey,specKey,version).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        try {
            OpenAPI parse = OpenAPILoader.parse(spec.getOpenApi());
            BoatDocsGenerator boatDocsGenerator = new BoatDocsGenerator();






        } catch (OpenAPILoaderException e) {
            e.printStackTrace();
        }

        return new ModelAndView();

    }

}
