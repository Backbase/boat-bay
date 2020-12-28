package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
import java.util.Optional;

public interface BoatServiceRepository extends ServiceDefinitionRepository {

    Optional<ServiceDefinition> findByCapabilityAndKey(Capability capability, String key);
}
