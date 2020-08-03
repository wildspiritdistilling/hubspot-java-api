package com.wildspirit.hubspot.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class FilterGroup {
    @JsonProperty("filters")
    public final List<Filter> filters;

    public FilterGroup(@JsonProperty("filters") List<Filter> filters) {
        this.filters = filters;
    }
}
