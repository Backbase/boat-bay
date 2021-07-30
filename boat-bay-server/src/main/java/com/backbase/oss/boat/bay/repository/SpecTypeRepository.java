package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.SpecType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SpecType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecTypeRepository extends JpaRepository<SpecType, Long> {}
