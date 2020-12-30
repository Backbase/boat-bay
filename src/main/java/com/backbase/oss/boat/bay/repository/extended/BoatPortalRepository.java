package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import java.util.Optional;

public interface BoatPortalRepository extends PortalRepository {

    Portal findByKeyAndVersion(String key, String version);

}
