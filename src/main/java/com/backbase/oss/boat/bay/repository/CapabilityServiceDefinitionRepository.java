package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.CapabilityServiceDefinition;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CapabilityServiceDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapabilityServiceDefinitionRepository extends JpaRepository<CapabilityServiceDefinition, Long> {
}
