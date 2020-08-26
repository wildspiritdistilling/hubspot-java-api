package com.wildspirit.hubspot.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Filter {
    @JsonProperty("propertyName")
    public final String propertyName;
    @JsonProperty("operator")
    public final Operator operator;
    @JsonProperty("value")
    public final Object value;

    public Filter(
            @JsonProperty("propertyName") String propertyName,
            @JsonProperty("operator") Operator operator,
            @JsonProperty("value") Object value
    ) {
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }
}
