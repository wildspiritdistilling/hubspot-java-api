package com.wildspirit.hubspot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.associations.AssociationsApi;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.contact.ContactApi;
import com.wildspirit.hubspot.engagements.EngagementApi;
import com.wildspirit.hubspot.integrations.IntegrationsApi;
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

    public ContactApi contacts() {
        return new ContactApi(client, apiKey, mapper);
    }

    public AssociationsApi associations() {
        return new AssociationsApi(client, apiKey, mapper);
    }

    public EngagementApi engagements() {
        return new EngagementApi(client, apiKey, mapper);
    }

    public IntegrationsApi integrations() {
        return new IntegrationsApi(client, apiKey, mapper);
    }
}
