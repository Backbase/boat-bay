package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ProductRelease_;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Spec_;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BoatSpecQuerySpecs {

    public Specification<Spec> hasProduct(Long productId) {
        return ((root, query, builder) -> {
            Join<Spec, Product> specProduct = root.join("product");
            return (builder.equal(specProduct.get("id"), productId));
        });
    }

    public Specification<Spec> hasCapability(String capabilityId) {
        return ((root, query, builder) -> {
            Join<Spec, Capability> specCapability = root.join("capability");
            return (builder.equal(specCapability.get("id"), capabilityId));
        });

    }

    public Specification<Spec> inProductRelease(String productReleaseId) {
        return ((root, query, builder) -> {
            Join<Spec, ProductRelease> specProductReleaseJoin = root.join(Spec_.PRODUCT_RELEASES);
            return (builder.equal(specProductReleaseJoin.get("id"), productReleaseId));
        });
    }
}
