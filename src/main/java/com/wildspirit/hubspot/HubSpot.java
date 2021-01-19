package com.wildspirit.hubspot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.wildspirit.hubspot.associations.AssociationsApi;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.contact.ContactApi;
import com.wildspirit.hubspot.engagements.EngagementApi;
import com.wildspirit.hubspot.integrations.IntegrationsApi;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.text.DateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class HubSpot {

    static final SimpleModule DATE_MODULE = new SimpleModule()
            .addSerializer(Date.class, new DateSerializerImpl());

    static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")))
            .registerModule(DATE_MODULE);

    private final OkHttpClient client = new OkHttpClient();

    private final String apiKey;

    private HubSpot(String apiKey) {
        this.apiKey = apiKey;
    }

    public static HubSpot fromEnvironment(String envName) {
        String key = System.getenv(envName);
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException(envName + " not set");
        }
        return new HubSpot(key);
    }

    public static HubSpot fromKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("key was null or empty");
        }
        return new HubSpot(key);
    }

    public CompanyApi companies() {
        return new CompanyApi(client, apiKey, mapper);
    }

    public ContactApi contacts() {
        return new ContactApi(client, apiKey, mapper);
    }

    public AssociationsApi associations() {
        return new AssociationsApi(client, apiKey, mapper);
    }

    public EngagementApi engagements() {
        return new EngagementApi(client, apiKey, mapper);
    }

    public IntegrationsApi integrations() {
        return new IntegrationsApi(client, apiKey, mapper);
    }

    /**
     * Makes sure we handle everything with midnight dates, which HubSpot annoyingly wants us to do!
     */
    private static class DateSerializerImpl extends DateSerializer {
        public DateSerializerImpl() {}

        public DateSerializerImpl(Boolean useTimestamp, DateFormat customFormat) {
            super(useTimestamp, customFormat);
        }

        @Override
        public void serialize(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.ofInstant(value.toInstant(), ZoneId.of(ZoneOffset.UTC.getId()));
            LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
            value = Date.from(todayMidnight.toInstant(ZoneOffset.UTC));
            super.serialize(value, g, provider);
        }
    }
}
