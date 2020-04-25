package com.wildspirit.hubspot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.company.CompaniesCollection;
import com.wildspirit.hubspot.company.Company;
import com.wildspirit.hubspot.company.CompanyApi;
import com.wildspirit.hubspot.company.UpdateCompanyRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        if (key.isEmpty()) {
            throw new IllegalStateException(envName + " not set");
        }
        return new HubSpot(key);
    }

    public static HubSpot fromKey(String key) {
        if (key.isEmpty()) {
            throw new IllegalStateException("key was null or empty");
        }
        return new HubSpot(key);
    }

    public CompanyApi companies() {
        return new CompanyApi(client, apiKey, mapper);
    }
}
