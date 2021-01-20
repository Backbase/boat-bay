package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class ServiceDefinitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceDefinition.class);
        ServiceDefinition serviceDefinition1 = new ServiceDefinition();
        serviceDefinition1.setId(1L);
        ServiceDefinition serviceDefinition2 = new ServiceDefinition();
        serviceDefinition2.setId(serviceDefinition1.getId());
        assertThat(serviceDefinition1).isEqualTo(serviceDefinition2);
        serviceDefinition2.setId(2L);
        assertThat(serviceDefinition1).isNotEqualTo(serviceDefinition2);
        serviceDefinition1.setId(null);
        assertThat(serviceDefinition1).isNotEqualTo(serviceDefinition2);
    }
}
