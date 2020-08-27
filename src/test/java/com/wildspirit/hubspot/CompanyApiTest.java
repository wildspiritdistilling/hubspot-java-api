package com.wildspirit.hubspot;

import com.wildspirit.hubspot.companies.Company;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.companies.CompanyApi.SearchCompaniesRequest;
import com.wildspirit.hubspot.search.Filter;
import com.wildspirit.hubspot.search.FilterGroup;
import com.wildspirit.hubspot.search.Operator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompanyApiTest {

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

    @Test
    public void testSearch() throws InterruptedException {
        Map<String, Object> properties = new HashMap<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.put("name", name);
        properties.put("phone", "0414251133");
        Company company = hubSpot.companies().create(new CompanyApi.CreateCompanyRequest(properties));
        Assert.assertNotNull(company);

        // Wait until hubspot indexes...
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));

        List<String> props = List.of("name", "phone");
        Filter filter = new Filter("name", Operator.EQUAL_TO, name);
        List<Company> companies = hubSpot.companies().search(new SearchCompaniesRequest(props, List.of(new FilterGroup(List.of(filter))), List.of(), null)).collect(Collectors.toList());
        Assert.assertEquals(1, companies.size());
    }
}
