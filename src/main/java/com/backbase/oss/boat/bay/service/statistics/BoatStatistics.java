package com.backbase.oss.boat.bay.service.statistics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BoatStatistics {

    private LocalDateTime updatedOn;

    private List<BoatIssueCount> issues = new ArrayList<>();

}
