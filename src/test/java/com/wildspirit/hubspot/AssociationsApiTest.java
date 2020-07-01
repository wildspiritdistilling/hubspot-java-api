package com.wildspirit.hubspot;

import com.wildspirit.hubspot.associations.AssociateRequest;
import com.wildspirit.hubspot.company.Company;
import com.wildspirit.hubspot.company.UpdateCompanyRequest;
import com.wildspirit.hubspot.contact.Contact;
import com.wildspirit.hubspot.contact.CreateContactRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssociationsApiTest {
    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void associateContactWithCompany() {
        // Create a company
        List<UpdateCompanyRequest.Property> properties = new ArrayList<>();
        String name = "Hello World " + System.currentTimeMillis();
        properties.add(new UpdateCompanyRequest.Property("name", name));
        properties.add(new UpdateCompanyRequest.Property("phone", "0414251133"));
        Company company = hubSpot.companies().create(properties);
        Assert.assertNotNull(company);
        Assert.assertEquals(name, company.getProperty("name"));
        Assert.assertEquals("0414251133", company.getProperty("phone"));
        // Create the contact
        final String contactFirstName = "bob" + System.currentTimeMillis();
        final String contactLastName = "mcbob" + System.currentTimeMillis();
        Contact contact = hubSpot.contacts().create(new CreateContactRequest.Builder()
                .addProperty("firstname", contactFirstName)
                .addProperty("lastname", contactLastName).build()
        );
        Assert.assertEquals(contactFirstName, contact.properties.get("firstname").value);
        Assert.assertEquals(contactLastName, contact.properties.get("lastname").value);
        // Associate the contact with the company
        hubSpot.crmAssociations().associate(
                new AssociateRequest.Builder()
                .objects(contact.vid, company.companyId)
                .definition(AssociateRequest.AssociationDefinition.CONTACT_TO_COMPANY)
                .build()
        );
        // Check that the association was made
        final List<Contact> contacts = hubSpot.companies().associatedContacts(company.companyId).collect(Collectors.toList());
        Assert.assertEquals(1, contacts.size());
        Contact relatedContact = contacts.get(0);
        Assert.assertEquals(contactFirstName, relatedContact.properties.get("firstname").value);
        Assert.assertEquals(contactLastName, relatedContact.properties.get("lastname").value);
    }
}
