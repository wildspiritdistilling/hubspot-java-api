package com.wildspirit.hubspot.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CollectionResponse {
    @JsonProperty("has-more")
    public final boolean hasMore;

    @JsonProperty("offset")
    public final String offset;

    public CollectionResponse(boolean hasMore, String offset) {
        this.hasMore = hasMore;
        this.offset = offset;
    }

}
