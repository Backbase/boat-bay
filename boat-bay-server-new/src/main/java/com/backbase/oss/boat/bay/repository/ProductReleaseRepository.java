package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductRelease entity.
 */
@Repository
public interface ProductReleaseRepository extends JpaRepository<ProductRelease, Long> {
    @Query(
        value = "select distinct productRelease from ProductRelease productRelease left join fetch productRelease.specs",
        countQuery = "select count(distinct productRelease) from ProductRelease productRelease"
    )
    Page<ProductRelease> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct productRelease from ProductRelease productRelease left join fetch productRelease.specs")
    List<ProductRelease> findAllWithEagerRelationships();

    @Query("select productRelease from ProductRelease productRelease left join fetch productRelease.specs where productRelease.id =:id")
    Optional<ProductRelease> findOneWithEagerRelationships(@Param("id") Long id);
}
