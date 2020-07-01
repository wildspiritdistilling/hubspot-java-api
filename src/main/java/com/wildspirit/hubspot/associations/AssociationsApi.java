package com.wildspirit.hubspot.associations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import okhttp3.*;

import java.io.IOException;

public final class AssociationsApi {

    private final OkHttpClient http;
    private final String apiKey;
    private final ObjectMapper mapper;

    public AssociationsApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        this.http = http;
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

    public void associate(AssociateRequest req) {
        final String bodyData;
        try {
            bodyData = mapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new HubSpotException(e);
        }
        RequestBody body = RequestBody.create(bodyData, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(String.format("https://api.hubapi.com/crm-associations/v1/associations?hapikey=%s", apiKey))
                .put(body)
                .build();
        try (Response response = http.newCall(request).execute()) {
            if (response.code() != 204) {
                throw new HubSpotException("Error " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            throw new HubSpotException(e);
        }
    }
}
