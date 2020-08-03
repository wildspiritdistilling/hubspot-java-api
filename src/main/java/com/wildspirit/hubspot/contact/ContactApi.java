package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import okhttp3.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ContactApi {

    private static final String ALL_CONTACTS_URL = "https://api.hubapi.com/contacts/v1/lists/all/contacts/all?hapikey=%s&%s";

    private final OkHttpClient client;
    private final String apiKey;
    private final ObjectMapper mapper;

    public ContactApi(OkHttpClient client, String apiKey, ObjectMapper mapper) {
        this.client = client;
        this.apiKey = apiKey;
        this.mapper = mapper;
    }

    public Stream<Contact> all(GetContactsRequest req) {
        final ContactIteratorImpl iterator = new ContactIteratorImpl(ALL_CONTACTS_URL, client, apiKey, mapper, req);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    public Contact create(CreateContactRequest req) {
        final byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(req);
        } catch (JsonProcessingException e) {
            throw new HubSpotException(e);
        }
        RequestBody body = RequestBody.create(MediaType.get("application/json"), bytes);
        Request request = new Request.Builder()
                .url(String.format("https://api.hubapi.com/contacts/v1/contact/?hapikey=%s", apiKey))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new HubSpotException("Error " + response.code() + " " + response.message());
            }
            return mapper.readValue(response.body().bytes(), Contact.class);
        } catch (IOException e) {
            throw new HubSpotException(e);
        }
    }


    public static final class GetContactsRequest {
        public final Long companyId;
        public final List<String> properties;
        public final PropertyMode propertyMode;
        public final FormSubmissionMode formSubmissionMode;
        public final Boolean showListMemberships;
        public final Integer count;

        public GetContactsRequest(Long companyId, List<String> properties, PropertyMode propertyMode, FormSubmissionMode formSubmissionMode, boolean showListMemberships, int count) {
            this.companyId = companyId;
            this.properties = properties;
            this.propertyMode = propertyMode;
            this.formSubmissionMode = formSubmissionMode;
            this.showListMemberships = showListMemberships;
            this.count = count;
        }

        public enum PropertyMode {
            VALUE_ONLY,
            VALUE_AND_HISTORY
        }

        public enum FormSubmissionMode {
            ALL,
            NONE,
            NEWEST,
            OLDEST
        }

        public static class Builder {
            private List<String> properties = new ArrayList<>();
            private PropertyMode propertyMode;
            private FormSubmissionMode formSubmissionMode;
            private boolean showListMemberships = false;
            private int count = 100;

            public Builder setProperties(List<String> properties) {
                this.properties = properties;
                return this;
            }

            public Builder setPropertyMode(PropertyMode propertyMode) {
                this.propertyMode = propertyMode;
                return this;
            }

            public Builder setFormSubmissionMode(FormSubmissionMode formSubmissionMode) {
                this.formSubmissionMode = formSubmissionMode;
                return this;
            }

            public Builder setShowListMemberships(boolean showListMemberships) {
                this.showListMemberships = showListMemberships;
                return this;
            }

            public Builder setCount(int count) {
                this.count = count;
                return this;
            }

            public GetContactsRequest build() {
                return new GetContactsRequest(null, properties, propertyMode, formSubmissionMode, showListMemberships, count);
            }
        }
    }

    public static final class GetContactsResponse {
        @JsonProperty("contacts")
        public final List<Contact> contacts;
        @JsonProperty("has-more")
        public final boolean hasMore;
        @JsonProperty("vid-offset")
        public final Integer vidOffset;

        public GetContactsResponse(
                @JsonProperty("contacts") List<Contact> contacts,
                @JsonProperty("hasMore") boolean hasMore,
                @JsonProperty("vid-offset") Integer vidOffset
        ) {
            this.contacts = contacts;
            this.hasMore = hasMore;
            this.vidOffset = vidOffset;
        }
    }

    public static class CreateContactRequest {
        @JsonProperty("properties")
        public final List<ContactProperty> properties;

        public CreateContactRequest(@JsonProperty("properties") List<ContactProperty> properties) {
            this.properties = properties;
        }

        public static final class Builder {
            public Map<String, ContactProperty> properties = new HashMap<>();

            public Builder addProperty(String name, String value) {
                properties.put(name, new ContactProperty(name, value));
                return this;
            }

            public CreateContactRequest build() {
                List<ContactProperty> properties = new ArrayList<>(this.properties.values());
                return new CreateContactRequest(properties);
            }
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
