package com.backbase.oss.boat.bay.web.views.dashboard.controller;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.extended.BoatSpecRepository;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import com.backbase.oss.codegen.doc.BoatDocsGenerator;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;


@RestController
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
