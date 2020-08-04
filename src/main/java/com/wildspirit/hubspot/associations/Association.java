package com.wildspirit.hubspot.associations;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Association {
    @JsonProperty("id")
    public final long id;
    @JsonProperty("type")
    public final String type;

    public Association(@JsonProperty("id") long id, @JsonProperty("type") String type) {
        this.id = id;
        this.type = type;
    }
}
