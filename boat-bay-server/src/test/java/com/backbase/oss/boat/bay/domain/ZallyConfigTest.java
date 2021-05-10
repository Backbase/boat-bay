package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class ZallyConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZallyConfig.class);
        ZallyConfig zallyConfig1 = new ZallyConfig();
        zallyConfig1.setId(1L);
        ZallyConfig zallyConfig2 = new ZallyConfig();
        zallyConfig2.setId(zallyConfig1.getId());
        assertThat(zallyConfig1).isEqualTo(zallyConfig2);
        zallyConfig2.setId(2L);
        assertThat(zallyConfig1).isNotEqualTo(zallyConfig2);
        zallyConfig1.setId(null);
        assertThat(zallyConfig1).isNotEqualTo(zallyConfig2);
    }
}
