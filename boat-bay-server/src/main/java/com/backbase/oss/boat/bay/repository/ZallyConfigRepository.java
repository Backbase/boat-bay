package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.ZallyConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ZallyConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZallyConfigRepository extends JpaRepository<ZallyConfig, Long> {}
