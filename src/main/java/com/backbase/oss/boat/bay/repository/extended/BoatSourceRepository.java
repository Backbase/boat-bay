package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import java.util.List;

public interface BoatSourceRepository extends SourceRepository {




    List<Source> findAllByCronExpressionIsNotNullAndActiveIsTrue();

    List<Source> findAllByActiveIsTrueAndRunOnStartupIsTrue();




}
