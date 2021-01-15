package com.backbase.oss.boat.bay.web.utils;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.service.export.ExportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestBody {
    private List<Spec> specs;
    private String location;
    private String projectId, artifactId, version;
}
