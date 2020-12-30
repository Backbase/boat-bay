package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import java.util.Optional;

public interface BoatCapabilityRepository extends CapabilityRepository {

    Optional<Capability> findByProductAndKey(Product product, String key);
}
