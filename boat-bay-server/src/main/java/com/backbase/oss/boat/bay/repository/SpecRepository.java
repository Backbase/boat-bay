package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Spec;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Spec entity.
 */
@Repository
public interface SpecRepository extends JpaRepository<Spec, Long> {
    @Query(
        value = "select distinct spec from Spec spec left join fetch spec.tags",
        countQuery = "select count(distinct spec) from Spec spec"
    )
    Page<Spec> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct spec from Spec spec left join fetch spec.tags")
    List<Spec> findAllWithEagerRelationships();

    @Query("select spec from Spec spec left join fetch spec.tags where spec.id =:id")
    Optional<Spec> findOneWithEagerRelationships(@Param("id") Long id);
}
