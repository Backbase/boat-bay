package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class ProductReleaseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductRelease.class);
        ProductRelease productRelease1 = new ProductRelease();
        productRelease1.setId(1L);
        ProductRelease productRelease2 = new ProductRelease();
        productRelease2.setId(productRelease1.getId());
        assertThat(productRelease1).isEqualTo(productRelease2);
        productRelease2.setId(2L);
        assertThat(productRelease1).isNotEqualTo(productRelease2);
        productRelease1.setId(null);
        assertThat(productRelease1).isNotEqualTo(productRelease2);
    }
}
