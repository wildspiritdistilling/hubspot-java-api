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

    public Engagement get(GetEngagementRequest req) {
        return httpGet(UrlBuilder.fromString(String.format("https://api.hubapi.com/engagements/v1/engagements/%s", req.id)), Engagement.class);
    }

    public void delete(DeleteEngagementRequest req) {
        httpDelete(UrlBuilder.fromString(String.format("https://api.hubapi.com/engagements/v1/engagements/%s", req.id)));
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
                    new EngagementInfo(null, true, 1, Type.NOTE, new Date()),
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

    public static class GetEngagementRequest {
        public final Long id;

        public GetEngagementRequest(Long id) {
            this.id = id;
        }
    }

    public static class DeleteEngagementRequest {
        public final Long id;

        public DeleteEngagementRequest(Long id) {
            this.id = id;
        }
    }
}
