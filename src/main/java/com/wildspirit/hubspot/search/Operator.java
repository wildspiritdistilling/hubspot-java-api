package com.wildspirit.hubspot.search;

import com.fasterxml.jackson.annotation.JsonValue;

public enum  Operator {
    EQUAL_TO("EQ"),
    NOT_EQUAL_TO("NEQ"),
    LESS_THAN("LT"),
    LESS_THAN_OR_EQUAL_TO("LTE"),
    GREATER_THAN("GT"),
    HAS_PROPERTY("HAS_PROPERTY"),
    NOT_HAS_PROPERTY("NOT_HAS_PROPERTY"),
    CONTAINS_TOKEN("CONTAINS_TOKEN"),
    NOT_CONTAINS_TOKEN("NOT_CONTAINS_TOKEN");

    @JsonValue
    public final String value;

    Operator(String value) {
        this.value = value;
    }
}
