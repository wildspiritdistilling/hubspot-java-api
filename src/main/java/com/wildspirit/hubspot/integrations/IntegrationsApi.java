package com.wildspirit.hubspot.integrations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.common.AbstractApi;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;

public class IntegrationsApi extends AbstractApi {
    public IntegrationsApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public AccountDetailsResponse accountDetails() {
        return httpGet(UrlBuilder.fromString("https://api.hubapi.com/integrations/v1/me"), AccountDetailsResponse.class);
    }

    public static class AccountDetailsResponse {
        public final Long portalId;
        public final String timeZone;
        public final String currency;
        public final Long utcOffsetMilliseconds;
        public final String utcOffset;

        public AccountDetailsResponse(
                @JsonProperty("portalId") Long portalId,
                @JsonProperty("timeZone") String timeZone,
                @JsonProperty("currency") String currency,
                @JsonProperty("utcOffsetMilliseconds") Long utcOffsetMilliseconds,
                @JsonProperty("utcOffset") String utcOffset) {
            this.portalId = portalId;
            this.timeZone = timeZone;
            this.currency = currency;
            this.utcOffsetMilliseconds = utcOffsetMilliseconds;
            this.utcOffset = utcOffset;
        }
    }
}
