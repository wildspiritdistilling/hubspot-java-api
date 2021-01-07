package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.common.AbstractApi;
import com.wildspirit.hubspot.common.CollectionResponse;
import com.wildspirit.hubspot.common.CollectionResponseIterator;
import com.wildspirit.hubspot.common.Paging;
import com.wildspirit.hubspot.search.FilterGroup;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public final class ContactApi extends AbstractApi {

    public ContactApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public Stream<Contact> all(GetContactsRequest req) {
        final UrlBuilder url = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts");
        return CollectionResponseIterator.httpPost(url, req, this, GetContactsResponse.class).stream();
    }

    public Contact create(CreateContactRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts"), req, Contact.class);
    }

    public CreateContactsBatchResponse create(CreateContactsBatchRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts/batch/create"), req, CreateContactsBatchResponse.class);
    }

    public Stream<Contact> search(SearchContactsRequest req) {
        UrlBuilder url = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts/search");
        return CollectionResponseIterator.httpPost(url, req, this, SearchContactsResponse.class).stream();
    }

    public Contact update(UpdateContactRequest req) {
        return httpPatch(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts/" + req.id), req, Contact.class);
    }

    public Contact get(GetContactRequest req) {
        UrlBuilder builder = UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts/" + req.id);
        if (req.properties != null) {
            StringJoiner joiner = new StringJoiner(",");
            req.properties.forEach(joiner::add);
            builder = builder.addParameter("properties", joiner.toString());
        }
        return httpGet(builder, Contact.class);
    }

    public UpdateContactsBatchResponse update(UpdateContactsBatchRequest req) {
        return httpPost(UrlBuilder.fromString("https://api.hubapi.com/crm/v3/objects/contacts/batch/update"), req, UpdateContactsBatchResponse.class);
    }

    public static final class GetContactsRequest {
        public final List<String> properties;

        public GetContactsRequest(List<String> properties) {
            this.properties = properties;
        }
    }

    public static final class GetContactsResponse extends CollectionResponse<Contact> {
        public GetContactsResponse(List<Contact> results, Paging paging) {
            super(results, paging);
        }
    }

    public static class CreateContactRequest {
        public final Map<String, Object> properties;

        public CreateContactRequest(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    public static class SearchContactsRequest {
        public final List<String> properties;
        public final List<FilterGroup> filterGroups;

        public SearchContactsRequest(@JsonProperty("properties") List<String> properties, @JsonProperty("filterGroups") List<FilterGroup> filterGroups) {
            this.properties = properties;
            this.filterGroups = filterGroups;
        }
    }

    public static class SearchContactsResponse extends CollectionResponse<Contact> {
        public SearchContactsResponse(@JsonProperty("results") List<Contact> results, @JsonProperty("paging") Paging paging) {
            super(results, paging);
        }
    }

    public static class UpdateContactRequest {
        public final Long id;
        public final Map<String, Object> properties;

        public UpdateContactRequest(Long id, Map<String, Object> properties) {
            this.id = id;
            this.properties = properties;
        }
    }

    public static class UpdateContactsBatchRequest {
        public final List<UpdateContactRequest> inputs;

        public UpdateContactsBatchRequest(List<UpdateContactRequest> inputs) {
            this.inputs = inputs;
        }
    }

    public static class UpdateContactsBatchResponse {
        public final String status;
        public final List<Contact> results;
        public final Date requestedAt;
        public final Date statedAt;
        public final Date completedAt;
        public final Map<String, String> links;

        public UpdateContactsBatchResponse(
                @JsonProperty("status") String status,
                @JsonProperty("results") List<Contact> results,
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

    public static class CreateContactsBatchRequest {
        public final List<CreateContactRequest> inputs;

        public CreateContactsBatchRequest(List<CreateContactRequest> inputs) {
            this.inputs = inputs;
        }
    }

    public static class CreateContactsBatchResponse {
        public final String status;
        public final List<Contact> results;
        public final Date requestedAt;
        public final Date statedAt;
        public final Date completedAt;
        public final Map<String, String> links;

        public CreateContactsBatchResponse(
                @JsonProperty("status") String status,
                @JsonProperty("results") List<Contact> results,
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

    public static class GetContactRequest {
        public final Long id;
        public final List<String> properties;

        public GetContactRequest(Long id, List<String> properties) {
            this.id = id;
            this.properties = properties;
        }
    }
}
