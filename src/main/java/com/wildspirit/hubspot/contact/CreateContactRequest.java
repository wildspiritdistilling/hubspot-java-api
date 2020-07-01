package com.wildspirit.hubspot.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateContactRequest {
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
