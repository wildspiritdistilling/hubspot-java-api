package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public final class Contact {
    @JsonProperty("vid")
    public final Long vid;
    @JsonProperty("canonical-vid")
    public final Long canonicalVid;
    @JsonProperty("properties")
    public final Map<String, Property> properties;

    public Contact(
            @JsonProperty("vid") Long vid,
            @JsonProperty("canonical-vid") Long canonicalVid,
            @JsonProperty("properties") Map<String, Property> properties
    ) {
        this.vid = vid;
        this.canonicalVid = canonicalVid;
        this.properties = properties;
    }

    public static final class Property {
        @JsonProperty("value")
        public final String value;

        public Property(@JsonProperty("value") String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
