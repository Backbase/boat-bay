package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class SpecTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecType.class);
        SpecType specType1 = new SpecType();
        specType1.setId(1L);
        SpecType specType2 = new SpecType();
        specType2.setId(specType1.getId());
        assertThat(specType1).isEqualTo(specType2);
        specType2.setId(2L);
        assertThat(specType1).isNotEqualTo(specType2);
        specType1.setId(null);
        assertThat(specType1).isNotEqualTo(specType2);
    }
}
