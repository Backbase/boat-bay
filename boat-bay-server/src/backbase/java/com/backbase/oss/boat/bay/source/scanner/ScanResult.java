package com.backbase.oss.boat.bay.source.scanner;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Data;

import static com.backbase.oss.boat.bay.source.SpecSourceResolver.LATEST;

@Data
public class ScanResult {

    private final Source source;
    private final SourceScannerOptions scannerOptions;
    private final List<ProductRelease> productReleases;






    public ScanResult(Source source, SourceScannerOptions sourceScannerOptions) {
        this.source = source;
        this.scannerOptions = sourceScannerOptions;
        this.productReleases = new ArrayList<>();

    }

    public ScanResult(Source source, SourceScannerOptions sourceScannerOptions, List<Spec> specs) {
        this.source = source;
        this.scannerOptions = sourceScannerOptions;
        this.productReleases = new ArrayList<>();
    }

    public void addProductRelease(ProductRelease productRelease) {
        this.productReleases.add(productRelease);
    }


    public void addSpec(Spec spec) {

        if(productReleases.isEmpty()) {
            ProductRelease newProductRelease = new ProductRelease();
            newProductRelease.setKey(LATEST.toLowerCase(Locale.ROOT));
            newProductRelease.setName(LATEST);
            newProductRelease.setVersion(LATEST);
            newProductRelease.setReleaseDate(ZonedDateTime.now());
            newProductRelease.setProduct(source.getProduct());
            productReleases.add(newProductRelease);
        }
        productReleases.get(0).addSpec(spec);

    }
}
