package com.wildspirit.hubspot.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class CollectionResponse<R> {
    public final List<R> results;
    public final Paging paging;

    public CollectionResponse(List<R> results, @JsonProperty("paging") Paging paging) {
        this.results = results;
        this.paging = paging;
    }
}
