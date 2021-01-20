package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Portal;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Portal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortalRepository extends JpaRepository<Portal, Long> {
}
