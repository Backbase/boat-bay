package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoatServiceRepository extends ServiceDefinitionRepository {

    Optional<ServiceDefinition> findByCapabilityAndKey(Capability capability, String key);

    Page<ServiceDefinition> findByCapabilityProduct(Product product, Pageable pageable);

    long countByCapabilityProduct(Product product);
}