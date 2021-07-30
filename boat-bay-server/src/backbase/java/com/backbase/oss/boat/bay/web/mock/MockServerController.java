package com.backbase.oss.boat.bay.web.mock;

import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.BoatProductReleaseRepository;
import com.backbase.oss.boat.bay.repository.BoatProductRepository;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.Expectation;
import org.mockserver.mock.OpenAPIExpectation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RestController("/api/boat")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MockServerController {

    Map<ProductRelease, ClientAndServer> mockServers = new HashMap<>();
    private final BoatProductReleaseRepository boatProductReleaseRepository;
    private final BoatProductRepository boatProductRepository;

    private Product getProduct(@PathVariable String portalKey, @PathVariable String productKey) {
        return boatProductRepository.findByKeyAndPortalKey(productKey, portalKey)
            .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping("/portals/{portalKey}/products/{productKey}/releases/{releaseKey}/simulators")
    public ResponseEntity<Void> createSim(@PathVariable String portalKey, @PathVariable String productKey, @PathVariable String releaseKey){


        Product product = getProduct(portalKey, productKey);
        ProductRelease productRelease = boatProductReleaseRepository.findByProductAndKey(product, releaseKey).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ClientAndServer value = startClientAndServer(1090);

            mockServers.put(productRelease, value);


            MockServerClient localhost = new MockServerClient("localhost", 1090);


            productRelease.getSpecs().stream()
                .filter(spec -> spec.getValid() == Boolean.TRUE)
                .filter(spec -> spec.getName().contains("client-api"))
                .forEach(spec -> {


                OpenAPIExpectation openAPIExpectation = new OpenAPIExpectation().withSpecUrlOrPayload(spec.getOpenApi());

                try {
                    Expectation[] upsert = localhost.upsert(openAPIExpectation);
                } catch (Exception e) {
                    log.error("Can't create mocks for spec: {}  due to: {}", spec.getName(), e.getMessage());
                }

            });



        return ResponseEntity.accepted().build();
    }
}
