package com.backbase.oss.boat.bay.service.statistics;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BoatStatistics {

    private LocalDateTime updatedOn;

    private long mustViolationsCount;
    private long shouldViolationsCount;
    private long mayViolationsCount;
    private long hintViolationsCount;

}
