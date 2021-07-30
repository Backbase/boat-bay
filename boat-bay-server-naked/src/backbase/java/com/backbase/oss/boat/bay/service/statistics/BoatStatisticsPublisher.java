package com.backbase.oss.boat.bay.service.statistics;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.service.model.BoatStatistics;

public interface BoatStatisticsPublisher {
    void publish(Product product, BoatStatistics boatStatistics);

    void publish(Capability capability, BoatStatistics boatStatistics);

    void publish(ServiceDefinition serviceDefinition, BoatStatistics boatStatistics);
}
