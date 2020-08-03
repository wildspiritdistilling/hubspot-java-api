package com.wildspirit.hubspot.companies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.common.*;
import com.wildspirit.hubspot.contact.Contact;
import com.wildspirit.hubspot.search.FilterGroup;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class CompanyApi extends AbstractApi {
    public CompanyApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public Stream<Company> all(GetCompaniesRequest req) {
        UrlBuilder builder = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies");
        if (req.properties != null) {
            StringJoiner joiner = new StringJoiner(",");
            req.properties.forEach(joiner::add);
            builder = builder.addParameter("properties", joiner.toString());
        }
        return CollectionResponseIterator.httpGet(builder, this, GetCompaniesResponse.class).stream();
    }

    public Company get(GetCompanyRequest req) {
        if (req.id == null) {
            throw new IllegalStateException("Must specify a company id");
        }
        return httpGet(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/" + req.id), Company.class);
    }

    public Company create(CreateCompanyRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies"), req, Company.class);
    }

    public Company update(UpdateCompanyRequest req) {
        return httpPatch(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/" + req.id), req, Company.class);
    }

    public Stream<Company> search(SearchCompaniesRequest req) {
        UrlBuilder url = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/search");
        return CollectionResponseIterator.httpPost(url, req, this, SearchCompaniesResponse.class).stream();
    }

    public static class GetCompaniesRequest {
        public final List<String> properties;

        public GetCompaniesRequest(List<String> properties) {
            this.properties = properties;
        }
    }

    public static class GetCompaniesResponse extends CollectionResponse<Company> {
        public GetCompaniesResponse(@JsonProperty("results") List<Company> results, @JsonProperty("paging") Paging paging) {
            super(results, paging);
        }
    }

    public static class CreateCompanyRequest {
        public final Map<String, Object> properties;

        public CreateCompanyRequest(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    public static class UpdateCompanyRequest {
        @JsonIgnore
        public final Long id;
        public final Map<String, Object> properties;

        public UpdateCompanyRequest(Long id, Map<String, Object> properties) {
            this.id = id;
            this.properties = properties;
        }
    }

    public static class GetCompanyRequest {
        public final Long id;

        public GetCompanyRequest(Long id) {
            this.id = id;
        }
    }

    public static class SearchCompaniesRequest {
        public final List<String> properties;
        public final List<FilterGroup> filterGroups;

        public SearchCompaniesRequest(@JsonProperty("properties") List<String> properties, @JsonProperty("filterGroups") List<FilterGroup> filterGroups) {
            this.properties = properties;
            this.filterGroups = filterGroups;
        }
    }

    public static class SearchCompaniesResponse extends CollectionResponse<Company> {
        public SearchCompaniesResponse(@JsonProperty("results") List<Company> results, @JsonProperty("paging") Paging paging) {
            super(results, paging);
        }
    }
}
