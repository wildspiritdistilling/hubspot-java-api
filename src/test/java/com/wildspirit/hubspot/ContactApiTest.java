package com.wildspirit.hubspot;

import com.wildspirit.hubspot.contact.Contact;
import com.wildspirit.hubspot.contact.ContactApi;
import com.wildspirit.hubspot.contact.ContactApi.CreateContactRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ContactApiTest {
    private final HubSpot hubSpot = HubSpot.fromEnvironment("HUBSPOT_TEST_KEY");

    @Test
    public void testIterateThroughContactsWithoutError() {
        hubSpot.contacts().all(new ContactApi.GetContactsRequest(List.of("name", "lastmodifieddate"))).forEach(contact -> {
            Assert.assertNotNull(contact);
            System.out.println(contact.properties.get("lastmodifieddate"));
        });
    }

    @Test
    public void testCreateContact() {
        Map<String, Object> props = Map.of("firstname", "bob", "lastname", "mcbob");
        Contact contact = hubSpot.contacts().create(new CreateContactRequest(props));
        Assert.assertEquals("bob", contact.properties.get("firstname"));
        Assert.assertEquals("mcbob", contact.properties.get("lastname"));
    }
}
