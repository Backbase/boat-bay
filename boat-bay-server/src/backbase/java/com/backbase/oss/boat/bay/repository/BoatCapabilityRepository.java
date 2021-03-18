package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoatCapabilityRepository extends CapabilityRepository {

    Optional<Capability> findByProductAndKey(Product product, String key);

    Page<Capability> findByProduct(Product product, Pageable pageable);

    long countByProduct(Product product);
}
