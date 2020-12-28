package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class CapabilityServiceDefinitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapabilityServiceDefinition.class);
        CapabilityServiceDefinition capabilityServiceDefinition1 = new CapabilityServiceDefinition();
        capabilityServiceDefinition1.setId(1L);
        CapabilityServiceDefinition capabilityServiceDefinition2 = new CapabilityServiceDefinition();
        capabilityServiceDefinition2.setId(capabilityServiceDefinition1.getId());
        assertThat(capabilityServiceDefinition1).isEqualTo(capabilityServiceDefinition2);
        capabilityServiceDefinition2.setId(2L);
        assertThat(capabilityServiceDefinition1).isNotEqualTo(capabilityServiceDefinition2);
        capabilityServiceDefinition1.setId(null);
        assertThat(capabilityServiceDefinition1).isNotEqualTo(capabilityServiceDefinition2);
    }
}
