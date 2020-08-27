package com.wildspirit.hubspot.engagements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wildspirit.hubspot.common.HubSpotType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@HubSpotType(type = "engagement", collectionName = "engagements")
public class Engagement {

    @JsonProperty("engagement")
    public final EngagementInfo engagement;
    @JsonProperty("associations")
    public final Associations associations;
    @JsonProperty("metadata")
    public final Map<String, Object> metdadata;

    public Engagement(
            @JsonProperty("engagement") EngagementInfo engagement,
            @JsonProperty("associations") Associations associations,
            @JsonProperty("metadata") Map<String, Object> metdadata
    ) {
        this.engagement = engagement;
        this.associations = associations;
        this.metdadata = metdadata;
    }

    public static class EngagementInfo {
        public final Long id;
        public final Boolean active;
        public final Integer ownerId;
        public final Type type;
        public final Date timestamp;

        public EngagementInfo(
                @JsonProperty("id") Long id,
                @JsonProperty("active") Boolean active,
                @JsonProperty("ownerId") Integer ownerId,
                @JsonProperty("type") Type type,
                @JsonProperty("timestamp") Date timestamp
        ) {
            this.id = id;
            this.active = active;
            this.ownerId = ownerId;
            this.type = type;
            this.timestamp = timestamp;
        }
    }

    public static class Associations {
        @JsonProperty("contactIds")
        public final List<Long> contactIds;
        @JsonProperty("companyIds")
        public final List<Long> companyIds;

        public Associations(
                @JsonProperty("contactIds") List<Long> contactIds,
                @JsonProperty("companyIds") List<Long> companyIds
        ) {
            this.contactIds = contactIds;
            this.companyIds = companyIds;
        }
    }

    public enum Type {
        NOTE,
        CALL,
        EMAIL,
        TASK,
        MEETING,
        INCOMING_EMAIL
    }
}
