package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.PortalRepository;
import java.util.List;

public interface BoatPortalRepository extends PortalRepository {

    Portal findByKeyAndVersion(String key, String version);

    List<PortalVersion> findAllByKey(String key);

    public static interface PortalVersion {
        Long getId();
        String getName();
        String getVersion();
    }
}
