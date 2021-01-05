package com.backbase.oss.boat.bay.dto;


import java.util.List;

public class DashboardDto {

    private String name;
    private String title;
    private PortalDto portal;
    private List<PortalVersionDto> portalVersions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PortalDto getPortal() {
        return portal;
    }

    public void setPortal(PortalDto portal) {
        this.portal = portal;
    }

    public List<PortalVersionDto> getPortalVersions() {
        return portalVersions;
    }

    public void setPortalVersions(List<PortalVersionDto> portalVersions) {
        this.portalVersions = portalVersions;
    }
}
