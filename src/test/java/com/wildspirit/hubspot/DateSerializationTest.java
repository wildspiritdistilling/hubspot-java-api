package com.wildspirit.hubspot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.companies.CompanyApi.CreateCompanyRequest;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateSerializationTest {
    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void testDateSerializesUTCMidnight() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat();
        Date parsed = format.parse("19/1/21, 11:00 am");
        String string = HubSpot.mapper.writeValueAsString(new DateHolder(parsed));
        Assert.assertEquals("{\"aDate\":1611014400000}", string);
    }

    @Test
    public void testSetProperty() {
        String companyName = "Crappy" + System.currentTimeMillis();
        hubSpot.companies().create(new CreateCompanyRequest(Map.of("name", companyName, "last_purchase", new Date())));
    }

    public class DateHolder {
        public final Date aDate;

        public DateHolder(Date aDate) {
            this.aDate = aDate;
        }
    }
}
