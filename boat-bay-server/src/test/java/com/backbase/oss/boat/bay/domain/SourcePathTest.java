package com.backbase.oss.boat.bay.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.backbase.oss.boat.bay.web.rest.TestUtil;

public class SourcePathTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourcePath.class);
        SourcePath sourcePath1 = new SourcePath();
        sourcePath1.setId(1L);
        SourcePath sourcePath2 = new SourcePath();
        sourcePath2.setId(sourcePath1.getId());
        assertThat(sourcePath1).isEqualTo(sourcePath2);
        sourcePath2.setId(2L);
        assertThat(sourcePath1).isNotEqualTo(sourcePath2);
        sourcePath1.setId(null);
        assertThat(sourcePath1).isNotEqualTo(sourcePath2);
    }
}
