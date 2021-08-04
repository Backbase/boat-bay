package com.backbase.oss.boat.bay.source.scanner;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.enumeration.SourceType;

public interface SpecSourceScanner {
    void setSource(Source source);

    Source getSource();

    ScanResult scan();

    SourceType getSourceType();
}
