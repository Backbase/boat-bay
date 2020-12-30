package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.repository.DashboardRepository;
import java.util.Optional;

public interface BoatDashboardRepository extends DashboardRepository{

    Optional<Dashboard> findDashboardByName(String name);

}
