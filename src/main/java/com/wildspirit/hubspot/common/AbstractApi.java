package com.wildspirit.hubspot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpot;
import com.wildspirit.hubspot.HubSpotException;
import com.wildspirit.hubspot.common.HttpException.ForbiddenException;
import com.wildspirit.hubspot.common.HttpException.TooManyRequestsException;
import com.wildspirit.hubspot.common.HttpException.UnmappedHttpException;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractApi {

    private static final Logger LOGGER = Logger.getLogger(AbstractApi.class.getName());

    protected final OkHttpClient http;
    protected final String apiKey;
    protected final ObjectMapper mapper;

    protected AbstractApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        this.http = http;
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

    protected <T> T httpGet(UrlBuilder builder, Class<T> responseClazz) {
        return retry(() -> doHttpGet(builder, responseClazz));
    }

    private <T> T doHttpGet(UrlBuilder builder, Class<T> responseClazz) {
        builder = builder.addParameter("hapikey", apiKey);
        Request request = new Request.Builder().url(builder.toString()).build();

        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new TooManyRequestsException(response.message());
                case 403:
                    throw new ForbiddenException(response.message());
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new HttpException("Error " + response.code() + " " + bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPost(UrlBuilder builder, Object object, Class<T> responseClazz) {
        return retry(() -> doHttpPost(builder, object, responseClazz));
    }

    private <T> T doHttpPost(UrlBuilder builder, Object object, Class<T> responseClazz) {
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
                    throw new TooManyRequestsException(response.message());
                case 403:
                    throw new ForbiddenException(response.message());
                case 409:
                    throw new HttpException.ConflictException(response.message());
                case 200:
                case 201:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new UnmappedHttpException(response.code(), bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected <T> T httpPut(UrlBuilder builder, Object object, Class<T> responseClazz) {
        return retry(() -> doHttpPut(builder, object, responseClazz));
    }

    private <T> T doHttpPut(UrlBuilder builder, Object object, Class<T> responseClazz) {
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
                    throw new TooManyRequestsException(response.message());
                case 403:
                    throw new ForbiddenException(response.message());
                case 409:
                    throw new HttpException.ConflictException(response.message());
                case 200:
                case 201:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new UnmappedHttpException(response.code(), bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("Could not map " + responseClazz.getName(), e);
        }
    }

    protected void httpDelete(UrlBuilder builder) {
        retry(() -> {
            doHttpDelete(builder);
            return null;
        });
    }

    private void doHttpDelete(UrlBuilder builder) {
        builder = builder.addParameter("hapikey", apiKey);
        Request.Builder requestBuilder = new Request.Builder()
                .url(builder.toString())
                .delete();
        Request request = requestBuilder.build();
        try (Response response = http.newCall(request).execute()) {
            ResponseBody body = response.body();
            switch (response.code()) {
                case 429:
                    throw new TooManyRequestsException(response.message());
                case 403:
                    throw new ForbiddenException(response.message());
                case 409:
                    throw new HttpException.ConflictException(response.message());
                case 200:
                case 201:
                case 204:
                    return;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new UnmappedHttpException(response.code(), bodyString);
            }
        } catch (IOException e) {
            throw new HubSpotException("There was a problem processing the response", e);
        }
    }

    protected <T> T httpPatch(UrlBuilder url, Object object, Class<T> responseClazz) {
        return retry(() -> doHttpPatch(url, object, responseClazz));
    }

    private <T> T doHttpPatch(UrlBuilder url, Object object, Class<T> responseClazz) {
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
                    throw new TooManyRequestsException(response.message());
                case 403:
                    throw new ForbiddenException(response.message());
                case 200:
                    return body == null ? null : mapper.readValue(body.bytes(), responseClazz);
                case 204:
                    return null;
                default:
                    String bodyString  = body == null ? "" : body.string();
                    throw new UnmappedHttpException(response.code(), bodyString);
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

    private <T> T retry(Supplier<T> action) {
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                final T result = action.get();
                LOGGER.log(Level.INFO, "Recovered at attempt " + (i+1) + " of " + attempts);
                return result;
            } catch (TooManyRequestsException e) {
                LOGGER.log(Level.INFO, "Retry attempt " + (i+1) + " of " + attempts);
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                } catch (InterruptedException ignored) {}
            }
        }
        throw new TooManyRequestsException("Exceeded 3 retries");
    }
}
