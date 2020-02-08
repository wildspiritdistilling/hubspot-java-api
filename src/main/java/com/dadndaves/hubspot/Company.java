package com.dadndaves.hubspot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public final class Company {
    public final long portalId;
    public final long companyId;

    @JsonProperty("properties")
    public final Map<String, Property> properties;

    @JsonCreator
    public Company(
            @JsonProperty("portalId") long portalId,
            @JsonProperty("companyId") long companyId,
            @JsonProperty("properties") Map<String, Property> properties) {
        this.portalId = portalId;
        this.companyId = companyId;
        this.properties = properties;
    }

    public static final class Property {
        public final String value;
        public final String sourceId;
        public final Date timestamp;

        @JsonCreator
        public Property(@JsonProperty("value") String value,
                        @JsonProperty("sourceId") String sourceId,
                        @JsonProperty("timestamp") Date timestamp) {
            this.value = value;
            this.sourceId = sourceId;
            this.timestamp = timestamp;
        }
    }
}
