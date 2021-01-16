package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import java.util.Optional;

public interface BoatPortalRepository extends PortalRepository {

   Optional<Portal> findByKey(String key);


}
