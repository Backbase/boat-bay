package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class LintRuleViolationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LintRuleViolation.class);
        LintRuleViolation lintRuleViolation1 = new LintRuleViolation();
        lintRuleViolation1.setId(1L);
        LintRuleViolation lintRuleViolation2 = new LintRuleViolation();
        lintRuleViolation2.setId(lintRuleViolation1.getId());
        assertThat(lintRuleViolation1).isEqualTo(lintRuleViolation2);
        lintRuleViolation2.setId(2L);
        assertThat(lintRuleViolation1).isNotEqualTo(lintRuleViolation2);
        lintRuleViolation1.setId(null);
        assertThat(lintRuleViolation1).isNotEqualTo(lintRuleViolation2);
    }
}
