package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

public interface BoatSourceRepository extends SourceRepository {

    Optional<Source> findByKeyAndPortal(String key, Portal portal);

    List<Source> findAllByCronExpressionIsNotNullAndActiveIsTrue();

    List<Source> findAllByActiveIsTrueAndRunOnStartupIsTrue();
}
