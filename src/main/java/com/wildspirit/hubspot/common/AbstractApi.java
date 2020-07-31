package com.wildspirit.hubspot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.*;

import java.io.IOException;

public class AbstractApi {
    protected final OkHttpClient http;
    protected final String apiKey;
    protected final ObjectMapper mapper;

    protected AbstractApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        this.http = http;
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

    protected <T> T httpGet(String url, Class<T> responseClazz) {
        UrlBuilder builder = UrlBuilder.fromString(url)
                .addParameter("hapikey", apiKey);
        Request request = new Request.Builder()
                .url(builder.toString())
                .build();

        try (Response response = http.newCall(request).execute()) {

            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new HubSpotException("Too many requests");
                case 403:
                    throw new HubSpotException("Forbidden");
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                default:
                    throw new HubSpotException("Error " + response.code() + " " + response.body().string());
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPost(String url, Object object, Class<T> responseClazz) {
        UrlBuilder urlBuilder = UrlBuilder.fromString(url)
                .addParameter("hapikey", apiKey);
        String bodyData;
        try {
            bodyData = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), bodyData);
        Request.Builder builder = new Request.Builder()
                .url(urlBuilder.toString())
                .post(requestBody);
        Request request = builder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPatch(String url, Object object, Class<T> responseClazz) {
        UrlBuilder urlBuilder = UrlBuilder.fromString(url)
                .addParameter("hapikey", apiKey);
        String bodyData;
        try {
            bodyData = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), bodyData);
        Request.Builder builder = new Request.Builder()
                .url(urlBuilder.toString())
                .patch(requestBody);
        Request request = builder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }
}
