package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class GetContactsResponse {
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
