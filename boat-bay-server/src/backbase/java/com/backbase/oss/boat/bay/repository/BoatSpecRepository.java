package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoatSpecRepository extends SpecRepository, JpaSpecificationExecutor<Spec> {

    Optional<Spec> findByChecksumAndSource(String checkSum, Source source);

    List<Spec> findAllByLintReportIsNull();

    List<Spec> findAllByCapabilityProduct(Product product);

    List<Spec> findAllByCapability(Capability capability);

    Page<Spec> findAllByCapabilityProduct(Product product, Pageable pageable);


    Page<Spec> findAllByCapability(Capability capability, Pageable pageable);

    Page<Spec> findAllByServiceDefinition(ServiceDefinition serviceDefinition, Pageable pageable);

    Page<Spec> findAllByCapabilityAndServiceDefinition(Capability capability, ServiceDefinition serviceDefinition, Pageable pageable);


    List<Spec> findAllByChangesIsNull();

    List<Spec> findAllByNameAndServiceDefinitionAndVersionIsNotNull(String name, ServiceDefinition serviceDefinition);

    List<Spec> findAllByKeyAndServiceDefinitionAndVersionIsNotNull(String key, ServiceDefinition serviceDefinition);


    List<Spec> findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKey(
        String portalKey,
        String productKey,
        String capabilityKey,
        String serviceDefinitionKey,
        String specKey
    );


    Optional<Spec> findByPortalKeyAndProductKeyAndCapabilityKeyAndServiceDefinitionKeyAndKeyAndVersion(
        String portalKey,
        String productKey,
        String capabilityKey,
        String serviceDefinitionKey,
        String specKey,
        String version);

}
