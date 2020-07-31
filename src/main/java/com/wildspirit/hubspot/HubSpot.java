package com.wildspirit.hubspot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.companies.CompanyApi;
import okhttp3.OkHttpClient;

public class HubSpot {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final OkHttpClient client = new OkHttpClient();

    private final String apiKey;

    private HubSpot(String apiKey) {
        this.apiKey = apiKey;
    }

    public static HubSpot fromEnvironment(String envName) {
        String key = System.getenv(envName);
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException(envName + " not set");
        }
        return new HubSpot(key);
    }

    public static HubSpot fromKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("key was null or empty");
        }
        return new HubSpot(key);
    }

    public CompanyApi companies() {
        return new CompanyApi(client, apiKey, mapper);
    }
}
