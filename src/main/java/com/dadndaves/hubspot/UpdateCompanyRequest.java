package com.dadndaves.hubspot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

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

        public static Property fromDate(String name, Date value) {
            long time = value.getTime();
            value = new Date(time - time % (24 * 60 * 60 * 1000));
            return new Property(name, Long.toString(value.getTime()));
        }

        public static Property fromDouble(String name, Double value) {
            return new Property(name, Double.toString(value));
        }
    }
}
