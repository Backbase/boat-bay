package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class PortalLintRuleConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortalLintRuleConfig.class);
        PortalLintRuleConfig portalLintRuleConfig1 = new PortalLintRuleConfig();
        portalLintRuleConfig1.setId(1L);
        PortalLintRuleConfig portalLintRuleConfig2 = new PortalLintRuleConfig();
        portalLintRuleConfig2.setId(portalLintRuleConfig1.getId());
        assertThat(portalLintRuleConfig1).isEqualTo(portalLintRuleConfig2);
        portalLintRuleConfig2.setId(2L);
        assertThat(portalLintRuleConfig1).isNotEqualTo(portalLintRuleConfig2);
        portalLintRuleConfig1.setId(null);
        assertThat(portalLintRuleConfig1).isNotEqualTo(portalLintRuleConfig2);
    }
}
