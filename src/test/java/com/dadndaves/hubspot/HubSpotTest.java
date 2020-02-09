package com.dadndaves.hubspot;

import org.junit.Assert;
import org.junit.Test;

public class HubSpotTest {
    @Test
    public void testIterateThroughCompaniesWithoutError() throws Exception {
        HubSpot hubSpot = HubSpot.fromKey("c5ce8ef3-cd80-41aa-b7ef-7dc8cdc0dd43");
        hubSpot.getCompanies().forEach(company -> {
            Assert.assertNotNull(company);
            System.out.println(company.properties.get("name").value);
        });
    }
}
