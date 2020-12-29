package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class PortalLintRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortalLintRule.class);
        PortalLintRule portalLintRule1 = new PortalLintRule();
        portalLintRule1.setId(1L);
        PortalLintRule portalLintRule2 = new PortalLintRule();
        portalLintRule2.setId(portalLintRule1.getId());
        assertThat(portalLintRule1).isEqualTo(portalLintRule2);
        portalLintRule2.setId(2L);
        assertThat(portalLintRule1).isNotEqualTo(portalLintRule2);
        portalLintRule1.setId(null);
        assertThat(portalLintRule1).isNotEqualTo(portalLintRule2);
    }
}
