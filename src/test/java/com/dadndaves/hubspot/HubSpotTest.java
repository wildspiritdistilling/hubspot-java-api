package com.dadndaves.hubspot;

import org.junit.Assert;
import org.junit.Test;

public class HubSpotTest {
    @Test
    public void testIterateThroughCompaniesWithoutError() throws Exception {
        HubSpot hubSpot = new HubSpot("c178c7a6-6d29-469c-bc7a-994f8fd8e592");
        hubSpot.getCompanies().forEach(company -> {
            Assert.assertNotNull(company);
            System.out.println(company.properties.get("name").value);
        });
    }
}
