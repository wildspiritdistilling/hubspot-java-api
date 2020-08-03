package com.wildspirit.hubspot.associations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import com.wildspirit.hubspot.common.AbstractApi;
import okhttp3.*;

import java.io.IOException;

public final class AssociationsApi extends AbstractApi {

    public AssociationsApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public void associate(AssociateRequest req) {
        final String bodyData;
        try {
            bodyData = mapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new HubSpotException(e);
        }
        RequestBody body = RequestBody.create(MediaType.get("application/json"), bodyData);
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
