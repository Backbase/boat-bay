package com.backbase.oss.boat.bay.source.scanner;

import static com.backbase.oss.boat.bay.source.SpecSourceResolver.LATEST;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Data;

@Data
public class ScanResult {

    private final Source source;

    private final List<ProductRelease> productReleases;

    public ScanResult(Source source) {
        this.source = source;
        this.productReleases = new ArrayList<>();
    }

    public ScanResult(Source source, List<Spec> specs) {
        this.source = source;
        this.productReleases = new ArrayList<>();
    }

    public void addProductRelease(ProductRelease productRelease) {
        this.productReleases.add(productRelease);
    }

    public void addSpec(Spec spec) {
        if (productReleases.isEmpty()) {
            ProductRelease newProductRelease = new ProductRelease();
            newProductRelease.setKey(spec.getMvnVersion());
            newProductRelease.setName(spec.getMvnVersion());
            newProductRelease.setVersion(spec.getMvnVersion());
            newProductRelease.setReleaseDate(ZonedDateTime.now());
            newProductRelease.setProduct(source.getProduct());
            productReleases.add(newProductRelease);
        }
        productReleases.get(0).addSpec(spec);
    }

    public long specCount() {
        return productReleases.stream().mapToLong(pr -> pr.getSpecs().size()).sum();
    }
}
