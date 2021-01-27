package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
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

    public Specification<Spec> hasCapabilityKey(String key) {
        return ((root, query, builder) -> {
            Join<Spec, Capability> join = root.join("capability");
            return (builder.equal(join.get("key"), key));
        });
    }

    public Specification<Spec> inProductRelease(String key) {
        return ((root, query, builder) -> {
            Join<Spec, ProductRelease> join = root.join("productReleases");
            return (builder.equal(join.get("key"), key));
        });
    }

    public Specification<Spec> hasServiceDefinition(String key) {
        return ((root, query, builder) -> {
            Join<Spec, ServiceDefinition> join = root.join("serviceDefinition");
            return (builder.equal(join.get("key"), key));
        });
    }
}
