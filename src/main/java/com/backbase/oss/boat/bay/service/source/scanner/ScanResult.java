package com.backbase.oss.boat.bay.service.source.scanner;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import java.util.List;
import lombok.Data;

@Data
public class ScanResult {

    private Source source;
    private List<Spec> specs;
    private List<ProductRelease> productReleases;

    public ScanResult(Source source, List<Spec> specs) {
        this.source = source;
        this.specs = specs;
    }

}
