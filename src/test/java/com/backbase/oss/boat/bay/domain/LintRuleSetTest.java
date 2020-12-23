package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class LintRuleSetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LintRuleSet.class);
        LintRuleSet lintRuleSet1 = new LintRuleSet();
        lintRuleSet1.setId(1L);
        LintRuleSet lintRuleSet2 = new LintRuleSet();
        lintRuleSet2.setId(lintRuleSet1.getId());
        assertThat(lintRuleSet1).isEqualTo(lintRuleSet2);
        lintRuleSet2.setId(2L);
        assertThat(lintRuleSet1).isNotEqualTo(lintRuleSet2);
        lintRuleSet1.setId(null);
        assertThat(lintRuleSet1).isNotEqualTo(lintRuleSet2);
    }
}
