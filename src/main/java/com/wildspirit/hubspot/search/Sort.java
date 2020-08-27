package com.wildspirit.hubspot.search;

public class Sort {

    public final String propertyName;
    public final Direction direction;

    public Sort(String propertyName, Direction direction) {
        this.propertyName = propertyName;
        this.direction = direction;
    }

    public enum Direction {
        DESCENDING,
        ASCENDING
    }
}
