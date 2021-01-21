package com.backbase.oss.boat.bay.web.views.dashboard.models;

import lombok.Data;

@Data
public class BoatTag {

    Long id;
    private String name;
    private String description;
    private boolean hide;
    private String color;
    private int numberOfOccurrences;

}
