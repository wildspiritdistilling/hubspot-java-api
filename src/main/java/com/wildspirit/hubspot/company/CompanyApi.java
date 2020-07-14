package com.wildspirit.hubspot.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class CompanyApi {
    private final OkHttpClient client;
    private final String apiKey;
    private final ObjectMapper mapper;

    public CompanyApi(OkHttpClient client, String apiKey, ObjectMapper mapper) {
        this.client = client;
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

    public Stream<Company> all() {
        return all(new String[] { "name" });
    }

    public Stream<Company> all(String[] properties) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new CompanyIteratorImpl(client, apiKey, mapper, properties), Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    public Company create(List<UpdateCompanyRequest.Property> properties) {
        UpdateCompanyRequest req = new UpdateCompanyRequest(properties);
        byte[] body;
        try {
            body = mapper.writeValueAsBytes(req);
        } catch (JsonProcessingException e) {
            throw new HubSpotException(e);
        }
        Request request = new Request.Builder()
                .post(RequestBody.create(okhttp3.MediaType.parse("application/json"), body))
                .url(String.format("https://api.hubapi.com/companies/v2/companies?hapikey=%s", apiKey))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                // TOODO: map error body and attach to exception
                // {"validationResults":[{"isValid":false,"message":"1579179660000 is at 13:1:0.0 UTC, not midnight!","error":"INVALID_DATE","name":"last_purchase"}],"status":"error","message":"Property values were not valid","correlationId":"079df615-35f9-46ed-80f9-d5930ce5c48d","requestId":"e942fbd1-1236-4c1d-b59c-ff44c89bf1c5"}
                throw new HubSpotException("Error " + response.code() + " " + response.message() + " " + response.body().string());
            }
            return mapper.readValue(response.body().bytes(), Company.class);
        } catch (Throwable e) {
            if (e instanceof HubSpotException) {
                throw (HubSpotException)e;
            }
            throw new HubSpotException(e);
        }
    }

    public Company update(long companyId, List<UpdateCompanyRequest.Property> properties) {
        UpdateCompanyRequest req = new UpdateCompanyRequest(properties);
        byte[] body;
        try {
            body = mapper.writeValueAsBytes(req);
        } catch (JsonProcessingException e) {
            throw new HubSpotException(e);
        }
        Request request = new Request.Builder()
                .put(RequestBody.create(okhttp3.MediaType.parse("application/json"), body))
                .url(String.format("https://api.hubapi.com/companies/v2/companies/%s?hapikey=%s", companyId, apiKey))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                // TOODO: map error body
                // {"validationResults":[{"isValid":false,"message":"1579179660000 is at 13:1:0.0 UTC, not midnight!","error":"INVALID_DATE","name":"last_purchase"}],"status":"error","message":"Property values were not valid","correlationId":"079df615-35f9-46ed-80f9-d5930ce5c48d","requestId":"e942fbd1-1236-4c1d-b59c-ff44c89bf1c5"}
                System.out.println(response.body().string());
                throw new HubSpotException("Error " + response.code() + " " + response.message());
            }
            return mapper.readValue(response.body().bytes(), Company.class);
        } catch (Throwable e) {
            throw new HubSpotException(e);
        }
    }

    private static class CompanyIteratorImpl implements Iterator<Company> {

        private String offset;
        private boolean hasMore;
        private Iterator<Company> companies;

        private final OkHttpClient client;
        private final String apiKey;
        private final ObjectMapper mapper;
        private final String properties;

        CompanyIteratorImpl(OkHttpClient client, String apiKey, ObjectMapper mapper, String[] properties) {
            this.client = client;
            this.apiKey = apiKey;
            this.mapper = mapper;
            StringJoiner joiner = new StringJoiner("&");
            for (String s : properties) {
                joiner.add("properties=" + s);
            }
            this.properties = joiner.toString();
        }

        @Override
        public boolean hasNext() {
            if (offset == null || !hasMore) {
                nextPage();
            } else if (!companies.hasNext()) {
                nextPage();
            }
            return companies.hasNext();
        }

        private void nextPage() {

            String url = String.format("https://api.hubapi.com/companies/v2/companies/paged?hapikey=%s&%s", apiKey, properties);
            if (offset != null) {
                url = String.format(url + "&offset=%s", offset);
            }

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() != 200) {
                    throw new HubSpotException("Error " + response.code() + " " + response.message());
                }
                try {
                    CompaniesCollection collection = mapper.readValue(response.body().string(), CompaniesCollection.class);
                    hasMore = collection.hasMore;
                    offset = collection.offset;
                    companies = collection.companies.iterator();
                } catch (IOException e) {
                    throw new HubSpotException(e);
                }
            } catch (IOException e) {
                throw new HubSpotException(e);
            }
        }

        @Override
        public Company next() {
            return companies.next();
        }
    }
}
