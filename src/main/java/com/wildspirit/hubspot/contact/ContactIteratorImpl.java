package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringJoiner;

public class ContactIteratorImpl implements Iterator<Contact> {

    private Integer offset;
    private boolean hasMore;
    private Iterator<Contact> contacts;

    private final OkHttpClient client;
    private final String apiKey;
    private final ObjectMapper mapper;
    private final String properties;
    private final String url;

    public ContactIteratorImpl(String url, OkHttpClient client, String apiKey, ObjectMapper mapper, ContactApi.GetContactsRequest req) {
        this.url = url;
        this.client = client;
        this.apiKey = apiKey;
        this.mapper = mapper;
        StringJoiner joiner = new StringJoiner("&");
        if (req.properties != null) {
            for (String s : req.properties) {
                joiner.add("property=" + s);
            }
        }
        if (req.count != null) {
            joiner.add("count=" + req.count);
        }
        if (req.formSubmissionMode != null) {
            joiner.add("formSubmissionMode=" + req.formSubmissionMode.name().toLowerCase());
        }
        if (req.showListMemberships != null && req.showListMemberships) {
            joiner.add("showListMemberships=true");
        }
        if (req.propertyMode != null) {
            joiner.add("propertyMode=" + req.propertyMode.name().toLowerCase());
        }
        this.properties = joiner.toString();
    }

    @Override
    public boolean hasNext() {
        if (offset == null || !hasMore) {
            nextPage();
        } else if (!contacts.hasNext()) {
            nextPage();
        }
        return contacts.hasNext();
    }

    private void nextPage() {
        String url = String.format(this.url, apiKey, properties);
        if (offset != null) {
            url = String.format(url + "&vidOffset=%s", offset);
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

                final String body = response.body().string();
                System.out.println(body);
                ContactApi.GetContactsResponse collection = mapper.readValue(body, ContactApi.GetContactsResponse.class);
                hasMore = collection.hasMore;
                offset = collection.vidOffset;
                contacts = collection.contacts.iterator();
            } catch (IOException e) {
                throw new HubSpotException(e);
            }
        } catch (IOException e) {
            throw new HubSpotException(e);
        }
    }

    @Override
    public Contact next() {
        return contacts.next();
    }
}
