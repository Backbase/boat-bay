package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;

public interface BoatPortalRepository extends PortalRepository {

    Portal findByKey(String key);


}
