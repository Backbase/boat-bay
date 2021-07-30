package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.SourcePath;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SourcePath entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourcePathRepository extends JpaRepository<SourcePath, Long> {}
