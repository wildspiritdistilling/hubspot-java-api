package com.dadndaves.hubspot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class UpdateCompanyRequest {
    public final List<Property> properties;

    public UpdateCompanyRequest(List<Property> properties) {
        this.properties = properties;
    }

    public static final class Property {
        public final String name;
        public final String value;

        public Property(@JsonProperty("name") String name,
                        @JsonProperty("value") String value) {
            this.name = name;
            this.value = value;
        }
    }
}
