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

    protected <T> T httpGet(UrlBuilder builder, Class<T> responseClazz) {
        builder = builder.addParameter("hapikey", apiKey);
        Request request = new Request.Builder().url(builder.toString()).build();

        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new HubSpotException("Too many requests");
                case 403:
                    throw new HubSpotException("Forbidden");
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new HubSpotException("Error " + response.code() + " " + bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPost(UrlBuilder builder, Object object, Class<T> responseClazz) {
        builder = builder.addParameter("hapikey", apiKey);
        RequestBody requestBody = serializeBody(object);
        Request.Builder requestBuilder = new Request.Builder()
                .url(builder.toString())
                .post(requestBody);
        Request request = requestBuilder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new HubSpotException("Too many requests");
                case 403:
                    throw new HubSpotException("Forbidden");
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new HubSpotException("Error " + response.code() + " " + bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPut(UrlBuilder builder, Object object, Class<T> responseClazz) {
        builder = builder.addParameter("hapikey", apiKey);
        RequestBody requestBody = serializeBody(object);
        Request.Builder requestBuilder = new Request.Builder()
                .url(builder.toString())
                .put(requestBody);
        Request request = requestBuilder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new HubSpotException("Too many requests");
                case 403:
                    throw new HubSpotException("Forbidden");
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new HubSpotException("Error " + response.code() + " " + bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPatch(UrlBuilder url, Object object, Class<T> responseClazz) {
        url = url.addParameter("hapikey", apiKey);
        RequestBody requestBody = serializeBody(object);
        Request.Builder builder = new Request.Builder()
                .url(url.toString())
                .patch(requestBody);
        Request request = builder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new HubSpotException("Too many requests");
                case 403:
                    throw new HubSpotException("Forbidden");
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new HubSpotException("Error " + response.code() + " " + bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    private RequestBody serializeBody(Object object) {
        String bodyData;
        try {
            bodyData = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return RequestBody.create(MediaType.get("application/json"), bodyData);
    }
}
