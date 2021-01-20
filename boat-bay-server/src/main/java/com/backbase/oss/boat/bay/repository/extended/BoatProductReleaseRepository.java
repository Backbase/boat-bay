package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;
import java.util.List;
import java.util.Optional;

public interface BoatProductReleaseRepository extends ProductReleaseRepository {




    List<ProductRelease> findAllByProduct(Product product);


    Optional<ProductRelease> findByProductAndKey(Product product, String key);

}
