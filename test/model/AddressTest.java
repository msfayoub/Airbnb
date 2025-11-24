package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AddressTest {

    @Test
    public void testAddressCreation() {
        Address address = new Address("123 Main St", "Apt 4B", "12345", "Paris", "France");
        
        assertEquals("123 Main St", address.getStreetAndNumber());
        assertEquals("Apt 4B", address.getComplement());
        assertEquals("12345", address.getPostalCode());
        assertEquals("Paris", address.getCity());
        assertEquals("France", address.getCountry());
    }

    @Test
    public void testAddressUpdate() {
        Address address = new Address("123 Main St", "Apt 4B", "12345", "Paris", "France");
        address.update("456 Oak Ave", "Suite 2", "67890", "Lyon", "France");
        
        assertEquals("456 Oak Ave", address.getStreetAndNumber());
        assertEquals("Suite 2", address.getComplement());
        assertEquals("67890", address.getPostalCode());
        assertEquals("Lyon", address.getCity());
    }

    @Test
    public void testAddressToString() {
        Address address = new Address("123 Main St", "Apt 4B", "12345", "Paris", "France");
        String result = address.toString();
        
        assertTrue("toString should contain street", result.contains("123 Main St"));
        assertTrue("toString should contain city", result.contains("Paris"));
    }
}
