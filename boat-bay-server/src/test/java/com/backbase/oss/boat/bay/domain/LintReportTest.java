package com.backbase.oss.boat.bay.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.oss.boat.bay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LintReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LintReport.class);
        LintReport lintReport1 = new LintReport();
        lintReport1.setId(1L);
        LintReport lintReport2 = new LintReport();
        lintReport2.setId(lintReport1.getId());
        assertThat(lintReport1).isEqualTo(lintReport2);
        lintReport2.setId(2L);
        assertThat(lintReport1).isNotEqualTo(lintReport2);
        lintReport1.setId(null);
        assertThat(lintReport1).isNotEqualTo(lintReport2);
    }
}
