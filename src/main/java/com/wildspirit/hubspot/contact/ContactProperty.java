package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ContactProperty {
    @JsonProperty("property")
    public final String name;
    @JsonProperty("value")
    public final String value;

    public ContactProperty(
            @JsonProperty("property") String name,
            @JsonProperty("value") String value
    ) {
        this.name = name;
        this.value = value;
    }
}
