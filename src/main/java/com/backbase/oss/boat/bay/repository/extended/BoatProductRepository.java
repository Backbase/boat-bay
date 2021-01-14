package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import java.util.Optional;

public interface BoatProductRepository extends ProductRepository {

    Optional<Product> findByKeyAndPortalKey(String productKey, String portalKey);
}
