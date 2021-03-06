package com.wildspirit.hubspot;

import com.wildspirit.hubspot.associations.Association;
import com.wildspirit.hubspot.associations.AssociationsApi;
import com.wildspirit.hubspot.associations.AssociationsApi.FindAssociationsRequest;
import com.wildspirit.hubspot.companies.Company;
import com.wildspirit.hubspot.companies.CompanyApi;
import com.wildspirit.hubspot.contact.Contact;
import com.wildspirit.hubspot.contact.ContactApi;
import com.wildspirit.hubspot.contact.ContactApi.CreateContactRequest;
import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AssociationsApiTest {
    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void associateContactWithCompany() {
        // Create a company
        Map<String, Object> properties = new HashMap<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.put("name", name);
        properties.put("phone", "0414251133");
        Company company = hubSpot.companies().create(new CompanyApi.CreateCompanyRequest(properties));
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.properties.get("name"));
        Assert.assertEquals("0414251133", company.properties.get("phone"));
        // Create the contact
        final String contactFirstName = "bob" + System.currentTimeMillis();
        final String contactLastName = "mcbob" + System.currentTimeMillis();
        Map<String, Object> contactProps = Map.of("firstname", contactFirstName, "lastname", contactLastName);
        Contact contact = hubSpot.contacts().create(new CreateContactRequest(contactProps));
        Assert.assertEquals(contactFirstName, contact.properties.get("firstname"));
        Assert.assertEquals(contactLastName, contact.properties.get("lastname"));
        // Associate the contact with the company
        hubSpot.associations().associate(
                new AssociationsApi.CreateAssociationRequest.Builder()
                .objects(contact.id, company.id)
                .definition(AssociationsApi.CreateAssociationRequest.AssociationDefinition.CONTACT_TO_COMPANY)
                .build()
        );

        final Association association = hubSpot.associations().find(new FindAssociationsRequest(company.id, Company.class, Contact.class)).findFirst().orElseThrow(AssertionFailedError::new);
        Assert.assertEquals((Long)contact.id, (Long)association.id);
        Assert.assertEquals("company_to_contact", association.type);
    }
}
