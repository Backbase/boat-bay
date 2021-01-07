package com.backbase.oss.boat.bay.service.source.scanner;

import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import java.util.List;

public interface SpecSourceScanner {

    void setSource(Source source);

    Source getSource();

    ScanResult scan();

    SourceType getSourceType();
}
