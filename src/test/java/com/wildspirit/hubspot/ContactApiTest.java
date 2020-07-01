package com.wildspirit.hubspot;

import com.wildspirit.hubspot.contact.Contact;
import com.wildspirit.hubspot.contact.CreateContactRequest;
import com.wildspirit.hubspot.contact.GetContactsRequest;
import org.junit.Assert;
import org.junit.Test;

public class ContactApiTest {
    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void testIterateThroughContactsWithoutError() {
        hubSpot.contacts().all(new GetContactsRequest.Builder().build()).forEach(contact -> {
            Assert.assertNotNull(contact);
            System.out.println(contact.properties.get("lastmodifieddate").value);
        });
    }

    @Test
    public void testCreateContact() {
        Contact contact = hubSpot.contacts().create(new CreateContactRequest.Builder()
                .addProperty("firstname", "bob")
                .addProperty("lastname", "mcbob").build()
        );
        Assert.assertEquals("bob", contact.properties.get("firstname").value);
        Assert.assertEquals("mcbob", contact.properties.get("lastname").value);
    }
}
