package com.backbase.oss.boat.bay.service.source.scanner;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ScanResult {

    private final Source source;
    private final List<Spec> specs;
    private final List<ProductRelease> productReleases;

    public ScanResult(Source source) {
        this.source = source;
        this.specs = new ArrayList<>();
        this.productReleases = new ArrayList<>();

    }

    public ScanResult(Source source, List<Spec> specs) {
        this.source = source;
        this.specs = specs;
        this.productReleases = new ArrayList<>();
    }

    public void addProductRelease(ProductRelease productRelease) {
        this.productReleases.add(productRelease);
    }
}
