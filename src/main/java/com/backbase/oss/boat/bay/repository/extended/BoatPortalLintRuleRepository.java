package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.repository.PortalLintRuleRepository;
import java.util.List;
import java.util.Set;

public interface BoatPortalLintRuleRepository extends PortalLintRuleRepository {


    boolean existsAllByPortal(Portal portal);

    Set<PortalLintRule> findAllByPortal(Portal portal);

    Set<PortalLintRule> findAllByPortalAndEnabled(Portal portal, boolean enabled);

}
