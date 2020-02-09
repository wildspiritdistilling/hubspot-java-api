package com.dadndaves.hubspot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

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

        public static Property fromString(String name, String value) {
            return new Property(name, value);
        }

        public static Property fromDate(String name, Date value) {
            // Need to convert to midnight on this day or hubspot has a tantrum
            long time = value.getTime();
            value = new Date(time - time % (24 * 60 * 60 * 1000));
            return new Property(name, Long.toString(value.getTime()));
        }

        public static Property fromDouble(String name, Double value) {
            return new Property(name, Double.toString(value));
        }

        public static Property fromOptions(String name, List<String> options) {
            StringBuilder sb = new StringBuilder();
            options.forEach(s -> {
                sb.append(s);
                sb.append(';');
            });
            return new Property(name, sb.toString());
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Property.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("value='" + value + "'")
                    .toString();
        }
    }
}
