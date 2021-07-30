package com.backbase.oss.boat.bay.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.oss.boat.bay.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PortalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portal.class);
        Portal portal1 = new Portal();
        portal1.setId(1L);
        Portal portal2 = new Portal();
        portal2.setId(portal1.getId());
        assertThat(portal1).isEqualTo(portal2);
        portal2.setId(2L);
        assertThat(portal1).isNotEqualTo(portal2);
        portal1.setId(null);
        assertThat(portal1).isNotEqualTo(portal2);
    }
}
