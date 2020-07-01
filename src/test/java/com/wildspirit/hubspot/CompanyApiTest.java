package com.wildspirit.hubspot;

import com.wildspirit.hubspot.company.Company;
import com.wildspirit.hubspot.company.UpdateCompanyRequest;
import com.wildspirit.hubspot.contact.GetContactsRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CompanyApiTest {

    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void testIterateThroughCompaniesWithoutError() {
        hubSpot.companies().all().forEach(company -> {
            Assert.assertNotNull(company);
            System.out.println(company.properties.get("name").value);
        });
    }

    @Test
    public void testCreateCompany() {
        List<UpdateCompanyRequest.Property> properties = new ArrayList<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.add(new UpdateCompanyRequest.Property("name", name));
        properties.add(new UpdateCompanyRequest.Property("phone", "0414251133"));
        Company company = hubSpot.companies().create(properties);
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.getProperty("name"));
        Assert.assertEquals("0414251133", company.getProperty("phone"));
    }

    @Test
    public void testUpdateCompany() {
        // Create the company
        List<UpdateCompanyRequest.Property> properties = new ArrayList<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.add(new UpdateCompanyRequest.Property("name", name));
        properties.add(new UpdateCompanyRequest.Property("phone", "0414251133"));
        Company createdCompany = hubSpot.companies().create(properties);
        Assert.assertNotNull(createdCompany);
        Assert.assertEquals(name, createdCompany.getProperty("name"));
        Assert.assertEquals("0414251133", createdCompany.getProperty("phone"));
        // Update the company
        List<UpdateCompanyRequest.Property> updateProperties = new ArrayList<>();
        updateProperties.add(new UpdateCompanyRequest.Property("phone", "020414251133"));
        Company updatedCompany = hubSpot.companies().update(createdCompany.companyId, updateProperties);
        Assert.assertEquals(createdCompany.companyId, updatedCompany.companyId);
        Assert.assertEquals("020414251133", updatedCompany.getProperty("phone"));
    }
}
