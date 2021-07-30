package com.backbase.oss.boat.bay.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.oss.boat.bay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LintRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LintRule.class);
        LintRule lintRule1 = new LintRule();
        lintRule1.setId(1L);
        LintRule lintRule2 = new LintRule();
        lintRule2.setId(lintRule1.getId());
        assertThat(lintRule1).isEqualTo(lintRule2);
        lintRule2.setId(2L);
        assertThat(lintRule1).isNotEqualTo(lintRule2);
        lintRule1.setId(null);
        assertThat(lintRule1).isNotEqualTo(lintRule2);
    }
}
