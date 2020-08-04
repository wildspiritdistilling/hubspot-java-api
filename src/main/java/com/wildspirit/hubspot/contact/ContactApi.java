package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import com.wildspirit.hubspot.common.AbstractApi;
import com.wildspirit.hubspot.common.CollectionResponse;
import com.wildspirit.hubspot.common.CollectionResponseIterator;
import com.wildspirit.hubspot.common.Paging;
import com.wildspirit.hubspot.companies.CompanyApi;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static final class ContactProperty {
        @JsonProperty("property")
        public final String name;
        @JsonProperty("value")
        public final String value;

        public ContactProperty(
                @JsonProperty("property") String name,
                @JsonProperty("value") String value
        ) {
            this.name = name;
            this.value = value;
        }
    }
}
