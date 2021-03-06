package com.wildspirit.hubspot.companies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wildspirit.hubspot.common.HubSpotType;

import java.util.Date;
import java.util.Map;

@HubSpotType(type = "company", collectionName = "companies")
public final class Company {
    public final Date createdAt;
    public final Date updatedAt;
    public final Boolean archived;
    public final Long id;
    public final Map<String, Object> properties;

    public Company(
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("updatedAt") Date updatedAt,
            @JsonProperty("archived") Boolean archived,
            @JsonProperty("id") Long id,
            @JsonProperty("properties") Map<String, Object> properties)
    {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.archived = archived;
        this.id = id;
        this.properties = properties;
    }
}
