package com.backbase.oss.boat.bay.web.utils;

import com.backbase.oss.boat.bay.domain.Spec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestBody {
    private List<UploadSpec> specs;
    private String location;
    private String projectId, artifactId, version;

    @Data
    class UploadSpec{

        @NotNull
        private String filename;
        @NotNull
        private String openApi;
        @NotNull
        private String key;

        @NotNull
        private String name;


    }
}
