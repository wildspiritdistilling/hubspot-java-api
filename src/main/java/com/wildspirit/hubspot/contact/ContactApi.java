package com.wildspirit.hubspot.contact;

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
        RequestBody body = RequestBody.create(bytes, MediaType.get("application/json"));
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

}
