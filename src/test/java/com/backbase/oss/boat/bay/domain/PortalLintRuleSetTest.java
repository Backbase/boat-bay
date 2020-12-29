package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class PortalLintRuleSetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortalLintRuleSet.class);
        PortalLintRuleSet portalLintRuleSet1 = new PortalLintRuleSet();
        portalLintRuleSet1.setId(1L);
        PortalLintRuleSet portalLintRuleSet2 = new PortalLintRuleSet();
        portalLintRuleSet2.setId(portalLintRuleSet1.getId());
        assertThat(portalLintRuleSet1).isEqualTo(portalLintRuleSet2);
        portalLintRuleSet2.setId(2L);
        assertThat(portalLintRuleSet1).isNotEqualTo(portalLintRuleSet2);
        portalLintRuleSet1.setId(null);
        assertThat(portalLintRuleSet1).isNotEqualTo(portalLintRuleSet2);
    }
}
