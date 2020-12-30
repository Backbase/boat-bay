package com.backbase.oss.boat.bay.dto;


import java.util.List;
import lombok.Data;

@Data
public class DashboardDto {

    private String name;
    private String title;
    private PortalDto portal;
    private List<String> portalVersions;

}
