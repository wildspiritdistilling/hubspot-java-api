package com.wildspirit.hubspot.companies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.common.AbstractApi;
import com.wildspirit.hubspot.common.CollectionResponse;
import com.wildspirit.hubspot.common.CollectionResponseIterator;
import com.wildspirit.hubspot.common.Paging;
import com.wildspirit.hubspot.contact.ContactApi;
import com.wildspirit.hubspot.search.Filter;
import com.wildspirit.hubspot.search.FilterGroup;
import com.wildspirit.hubspot.search.Operator;
import com.wildspirit.hubspot.search.Sort;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;

import java.util.Date;
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

    public CreateCompaniesBatchResponse create(CreateCompaniesBatchRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/batch/create"), req, CreateCompaniesBatchResponse.class);
    }

    public Company update(UpdateCompanyRequest req) {
        return httpPatch(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/" + req.id), req, Company.class);
    }

    public UpdateCompaniesBatchResponse update(UpdateCompaniesBatchRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/batch/update"), req, UpdateCompaniesBatchResponse.class);
    }

    public Stream<Company> search(SearchCompaniesRequest req) {
        UrlBuilder url = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/companies/search");
        CollectionResponseIterator.RequestWrapper<SearchCompaniesRequest, SearchCompaniesResponse> wrapper = new CollectionResponseIterator.RequestWrapper<>() {
            @Override
            public SearchCompaniesRequest wrapRequest(SearchCompaniesRequest req, SearchCompaniesResponse resp) {
                return new SearchCompaniesRequest(req.properties, req.filterGroups, req.sorts, resp.paging.next.after);
            }
        };
        return CollectionResponseIterator.httpPost(url, req, wrapper, this, SearchCompaniesResponse.class).stream();
    }

    public void merge(MergeCompanyRequest req) {
        UrlBuilder url = UrlBuilder.fromString(String.format("https://api.hubapi.com/companies/v2/companies/%s/merge?portalId=%s", req.parentCompanyId, req.portalId));
        httpPut(url, req, MergeCompanyResponse.class);
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
        public final List<Sort> sorts;
        public final String after;

        public SearchCompaniesRequest(@JsonProperty("properties") List<String> properties, @JsonProperty("filterGroups") List<FilterGroup> filterGroups, @JsonProperty("sorts") List<Sort> sorts, String after) {
            this.properties = properties;
            this.filterGroups = filterGroups;
            this.sorts = sorts;
            this.after = after;
        }

        public static SearchCompaniesRequest byName(String name) {
            return new SearchCompaniesRequest(List.of("name"), List.of(new FilterGroup(List.of(new Filter("name", Operator.EQUAL_TO, name)))), List.of(), null);
        }

        public static SearchCompaniesRequest byEmail(String email) {
            return new SearchCompaniesRequest(List.of("name"), List.of(new FilterGroup(List.of(new Filter("email", Operator.EQUAL_TO, email)))), List.of(), null);
        }

        public static SearchCompaniesRequest singleProperty(String propertyName, Object propertyValue) {
            return new SearchCompaniesRequest(List.of("name"), List.of(new FilterGroup(List.of(new Filter(propertyName, Operator.EQUAL_TO, propertyValue)))), List.of(), null);
        }
    }

    public static class SearchCompaniesResponse extends CollectionResponse<Company> {
        public SearchCompaniesResponse(@JsonProperty("results") List<Company> results, @JsonProperty("paging") Paging paging) {
            super(results, paging);
        }
    }

    public static class CreateCompaniesBatchRequest {
        public final List<CreateCompanyRequest> inputs;

        public CreateCompaniesBatchRequest(List<CreateCompanyRequest> inputs) {
            this.inputs = inputs;
        }
    }

    public static class CreateCompaniesBatchResponse {
        public final String status;
        public final List<Company> results;
        public final Date requestedAt;
        public final Date statedAt;
        public final Date completedAt;
        public final Map<String, String> links;

        public CreateCompaniesBatchResponse(
                @JsonProperty("status") String status,
                @JsonProperty("results") List<Company> results,
                @JsonProperty("requestedAt") Date requestedAt,
                @JsonProperty("statedAt") Date statedAt,
                @JsonProperty("completedAt") Date completedAt,
                @JsonProperty("links") Map<String, String> links
        ) {
            this.status = status;
            this.results = results;
            this.requestedAt = requestedAt;
            this.statedAt = statedAt;
            this.completedAt = completedAt;
            this.links = links;
        }
    }

    public static class UpdateCompaniesBatchRequest {
        public final List<UpdateCompanyRequest> inputs;

        public UpdateCompaniesBatchRequest(List<UpdateCompanyRequest> inputs) {
            this.inputs = inputs;
        }
    }

    public static class UpdateCompaniesBatchResponse {
        public final String status;
        public final List<Company> results;
        public final Date requestedAt;
        public final Date statedAt;
        public final Date completedAt;
        public final Map<String, String> links;

        public UpdateCompaniesBatchResponse(
                @JsonProperty("status") String status,
                @JsonProperty("results") List<Company> results,
                @JsonProperty("requestedAt") Date requestedAt,
                @JsonProperty("statedAt") Date statedAt,
                @JsonProperty("completedAt") Date completedAt,
                @JsonProperty("links") Map<String, String> links
        ) {
            this.status = status;
            this.results = results;
            this.requestedAt = requestedAt;
            this.statedAt = statedAt;
            this.completedAt = completedAt;
            this.links = links;
        }
    }

    public static class MergeCompanyRequest {
        @JsonIgnore
        public final Long portalId;
        @JsonIgnore
        public final Long parentCompanyId;
        public final Long companyIdToMerge;

        public MergeCompanyRequest(Long portalId, Long parentCompanyId, Long companyIdToMerge) {
            this.portalId = portalId;
            this.parentCompanyId = parentCompanyId;
            this.companyIdToMerge = companyIdToMerge;
        }
    }

    public static class MergeCompanyResponse {}
}
