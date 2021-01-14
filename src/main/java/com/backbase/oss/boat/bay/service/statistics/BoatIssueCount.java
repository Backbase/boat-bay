package com.backbase.oss.boat.bay.service.statistics;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import lombok.Data;

@Data
public class BoatIssueCount {

    private Severity severity;
    private Long numberOfIssues;

}
