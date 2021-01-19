package com.backbase.oss.boat.bay.config;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

public class BoatMeterRegistryCustomizer implements MeterRegistryCustomizer {
    @Override
    public void customize(MeterRegistry registry) {

        List<String> tags = new ArrayList<>(Arrays.stream(Severity.values()).map(Severity::name).collect(Collectors.toList()));



    }
}
