package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressTest {

    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address("123 Main St", "Apt 4B", "10001", "New York", "USA");
    }

    @Test
    public void testUpdate() {
        address.update("456 Other St", "Suite 1", "90210", "Beverly Hills", "USA");
        assertEquals("456 Other St", address.getStreetAndNumber());
        assertEquals("Suite 1", address.getComplement());
        assertEquals("90210", address.getPostalCode());
        assertEquals("Beverly Hills", address.getCity());
        assertEquals("USA", address.getCountry());
    }

    @Test
    public void testSetters() {
        address.setStreetAndNumber("789 New St");
        address.setComplement("Apt 5");
        address.setPostalCode("12345");
        address.setCity("New City");
        address.setCountry("France");

        assertEquals("789 New St", address.getStreetAndNumber());
        assertEquals("Apt 5", address.getComplement());
        assertEquals("12345", address.getPostalCode());
        assertEquals("New City", address.getCity());
        assertEquals("France", address.getCountry());
    }

    @Test
    public void testToString() {
        String result = address.toString();
        assertNotNull(result);
        assertTrue(result.contains("123 Main St"));
        assertTrue(result.contains("New York"));
    }
}
