package com.dadndaves.hubspot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class HubSpot {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final OkHttpClient client = new OkHttpClient();

    private final String apiKey;

    public HubSpot(String apiKey) {
        this.apiKey = apiKey;
    }

    public Iterable<Company> getCompanies() {
        return () -> new CompanyIteratorImpl(client, apiKey);
    }

    public Company updateCompany(long companyId, List<UpdateCompanyRequest.Property> properties) throws Exception {
        UpdateCompanyRequest req = new UpdateCompanyRequest(properties);
        System.out.println(mapper.writeValueAsString(req));
        Request request = new Request.Builder()
                .put(RequestBody.create(mapper.writeValueAsBytes(req), okhttp3.MediaType.parse("application/json")))
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
        }
    }

    private static class CompanyIteratorImpl implements Iterator<Company> {

        private String offset;
        private boolean hasMore;
        private Iterator<Company> companies;

        private final OkHttpClient client;
        private final String apiKey;

        CompanyIteratorImpl(OkHttpClient client, String apiKey) {
            this.client = client;
            this.apiKey = apiKey;
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

            String url = String.format("https://api.hubapi.com/companies/v2/companies/paged?hapikey=%s&properties=name", apiKey);
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
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Company next() {
            return companies.next();
        }
    }
}
