package com.dadndaves.hubspot;

import com.dadndaves.hubspot.UpdateCompanyRequest.Property;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HubSpotTest {

    private HubSpot hubSpot = HubSpot.fromKey("c5ce8ef3-cd80-41aa-b7ef-7dc8cdc0dd43");

    @Test
    public void testIterateThroughCompaniesWithoutError() {
        hubSpot.getCompanies().forEach(company -> {
            Assert.assertNotNull(company);
            System.out.println(company.properties.get("name").value);
        });
    }

    @Test
    public void testCreateCompany() {
        List<Property> properties = new ArrayList<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.add(new Property("name", name));
        properties.add(new Property("phone", "0414251133"));
        Company company = hubSpot.createCompany(properties);
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.getProperty("name"));
        Assert.assertEquals("0414251133", company.getProperty("phone"));
    }

    @Test
    public void testUpdateCompany() {
        // Create the company
        List<Property> properties = new ArrayList<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.add(new Property("name", name));
        properties.add(new Property("phone", "0414251133"));
        Company createdCompany = hubSpot.createCompany(properties);
        Assert.assertNotNull(createdCompany);
        Assert.assertEquals(name, createdCompany.getProperty("name"));
        Assert.assertEquals("0414251133", createdCompany.getProperty("phone"));
        // Update the company
        List<Property> updateProperties = new ArrayList<>();
        updateProperties.add(new Property("phone", "020414251133"));
        Company updatedCompany = hubSpot.updateCompany(createdCompany.companyId, updateProperties);
        Assert.assertEquals(createdCompany.companyId, updatedCompany.companyId);
        Assert.assertEquals("020414251133", updatedCompany.getProperty("phone"));
    }
}
