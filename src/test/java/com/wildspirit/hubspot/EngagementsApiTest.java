package com.wildspirit.hubspot;

import com.wildspirit.hubspot.companies.Company;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.engagements.EngagementApi.CreateEngagementRequest;
import com.wildspirit.hubspot.engagements.EngagementApi.CreateEngagementResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EngagementsApiTest {
    private HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void createNote() {
        Map<String, Object> properties = new HashMap<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.put("name", name);
        properties.put("phone", "0414251133");
        Company company = hubSpot.companies().create(new CompanyApi.CreateCompanyRequest(properties));
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.properties.get("name"));
        Assert.assertEquals("0414251133", company.properties.get("phone"));

        CreateEngagementResponse engagement = hubSpot.engagements().create(CreateEngagementRequest.companyNote(company.id, "a cool note"));
        Assert.assertNotNull(engagement);
        Assert.assertEquals("a cool note", engagement.metdadata.get("body"));
        Assert.assertTrue(engagement.associations.companyIds.contains(company.id));
    }
}
