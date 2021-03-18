package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.repository.ProductReleaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface BoatProductReleaseRepository extends ProductReleaseRepository {


    List<ProductRelease> findAllByProductOrderByReleaseDate(Product product);


    @EntityGraph(attributePaths = { "specs" })
    Optional<ProductRelease> findByProductAndKey(Product product, String key);

}
