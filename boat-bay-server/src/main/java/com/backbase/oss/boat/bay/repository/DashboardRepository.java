package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Dashboard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {}
