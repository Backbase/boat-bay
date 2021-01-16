package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoatSpecRepository extends SpecRepository {

    Optional<Spec> findByChecksumAndSource(String checkSum, Source source);

    List<Spec> findAllByLintReportIsNull();

    List<Spec> findAllByCapabilityProduct(Product product);

    List<Spec> findAllByCapability(Capability capability);

    Page<Spec> findAllByCapabilityProduct(Product product, Pageable pageable);

}
