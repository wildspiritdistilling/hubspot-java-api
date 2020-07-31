package com.wildspirit.hubspot;

import com.wildspirit.hubspot.companies.Company;
import com.wildspirit.hubspot.companies.CompanyApi;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HubSpotTest {

    private HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void testIterateThroughCompaniesWithoutError() {
        hubSpot.companies().all(new CompanyApi.GetCompaniesRequest(null)).forEach(company -> {
            Assert.assertNotNull(company);
            System.out.println(company.properties.get("name"));
        });
    }

    @Test
    public void testCreateCompany() {
        Map<String, Object> properties = new HashMap<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.put("name", name);
        properties.put("phone", "0414251133");
        Company company = hubSpot.companies().create(new CompanyApi.CreateCompanyRequest(properties));
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.properties.get("name"));
        Assert.assertEquals("0414251133", company.properties.get("phone"));
    }

    @Test
    public void testUpdateCompany() {
        // Create the company
        Map<String, Object> properties = new HashMap<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.put("name", name);
        properties.put("phone", "0414251133");
        Company company = hubSpot.companies().create(new CompanyApi.CreateCompanyRequest(properties));
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.properties.get("name"));
        Assert.assertEquals("0414251133", company.properties.get("phone"));
        // Update the company
        Map<String, Object> updateProperties = new HashMap<>();
        updateProperties.put("phone", "020414251133");
        Company updatedCompany = hubSpot.companies().update(new CompanyApi.UpdateCompanyRequest(company.id, updateProperties));
        Assert.assertEquals(company.id, updatedCompany.id);
        Assert.assertEquals("020414251133", updatedCompany.properties.get("phone"));
    }
}
