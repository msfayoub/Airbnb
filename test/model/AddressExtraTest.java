package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressExtraTest {

    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address();
    }

    @Test
    public void testSetId() {
        address.setId(1);
        assertEquals(1, address.getId());
    }

    @Test
    public void testSetCountry() {
        address.setCountry("France");
        assertEquals("France", address.getCountry());
    }

    @Test
    public void testSetCity() {
        address.setCity("Paris");
        assertEquals("Paris", address.getCity());
    }

    @Test
    public void testSetStreetAndNumber() {
        address.setStreetAndNumber("123 Main Street");
        assertEquals("123 Main Street", address.getStreetAndNumber());
    }

    @Test
    public void testSetPostalCode() {
        address.setPostalCode("75001");
        assertEquals("75001", address.getPostalCode());
    }

    @Test
    public void testSetComplement() {
        address.setComplement("Apt 4B");
        assertEquals("Apt 4B", address.getComplement());
    }

    @Test
    public void testEmptyFields() {
        address.setCountry("");
        address.setCity("");
        address.setStreetAndNumber("");
        address.setPostalCode("");
        
        assertEquals("", address.getCountry());
        assertEquals("", address.getCity());
        assertEquals("", address.getStreetAndNumber());
        assertEquals("", address.getPostalCode());
    }

    @Test
    public void testUpdateMethod() {
        address.update("10 Rue de Test", "Apt 5", "75000", "Paris", "France");
        assertEquals("10 Rue de Test", address.getStreetAndNumber());
        assertEquals("Apt 5", address.getComplement());
        assertEquals("75000", address.getPostalCode());
        assertEquals("Paris", address.getCity());
        assertEquals("France", address.getCountry());
    }

    @Test
    public void testToString() {
        address.setStreetAndNumber("123 Main");
        address.setComplement("Building A");
        address.setPostalCode("12345");
        address.setCity("TestCity");
        address.setCountry("TestCountry");
        assertNotNull(address.toString());
    }
}
