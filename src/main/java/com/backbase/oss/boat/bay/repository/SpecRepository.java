package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Spec;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Spec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecRepository extends JpaRepository<Spec, Long> {
}
