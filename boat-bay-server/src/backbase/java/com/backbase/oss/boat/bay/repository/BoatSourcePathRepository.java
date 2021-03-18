package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SourcePath;
import com.backbase.oss.boat.bay.repository.SourcePathRepository;
import com.backbase.oss.boat.bay.repository.SourceRepository;
import java.util.List;

public interface BoatSourcePathRepository extends SourcePathRepository {

    void deleteAllBySource(Source source);

}
