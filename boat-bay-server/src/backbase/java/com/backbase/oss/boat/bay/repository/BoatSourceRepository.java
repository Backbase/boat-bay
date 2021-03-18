package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;

public interface BoatSourceRepository extends SourceRepository {

    List<Source> findAllByCronExpressionIsNotNullAndActiveIsTrue();

    List<Source> findAllByActiveIsTrueAndRunOnStartupIsTrue();




}
