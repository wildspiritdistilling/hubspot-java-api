package com.wildspirit.hubspot.engagements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.common.AbstractApi;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EngagementApi extends AbstractApi {
    public EngagementApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public CreateEngagementResponse create(CreateEngagementRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/engagements/v1/engagements"), req, CreateEngagementResponse.class);
    }

    public static class CreateEngagementRequest extends Engagement {
        public CreateEngagementRequest(
                @JsonProperty("engagement") EngagementInfo engagement,
                @JsonProperty("associations") Associations associations,
                @JsonProperty("metadata") Map<String, Object> metdadata
        ) {
            super(engagement, associations, metdadata);
        }

        public static CreateEngagementRequest companyNote(long companyId, String text) {
            return new CreateEngagementRequest(
                    new EngagementInfo(true, 1, Type.NOTE, new Date()),
                    new Associations(null, List.of(companyId)),
                    Map.of("body", text)
            );
        }
    }

    public static class CreateEngagementResponse extends Engagement {
        public CreateEngagementResponse(
                @JsonProperty("engagement") EngagementInfo engagement,
                @JsonProperty("associations") Associations associations,
                @JsonProperty("metadata") Map<String, Object> metdadata
        ) {
            super(engagement, associations, metdadata);
        }
    }
}
