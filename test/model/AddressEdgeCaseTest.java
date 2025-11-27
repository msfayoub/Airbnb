package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressEdgeCaseTest {

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
    public void testSetStreet() {
        address.setStreet("123 Main Street");
        assertEquals("123 Main Street", address.getStreet());
    }

    @Test
    public void testSetZipCode() {
        address.setZipCode("75001");
        assertEquals("75001", address.getZipCode());
    }

    @Test
    public void testEmptyFields() {
        address.setCountry("");
        address.setCity("");
        address.setStreet("");
        address.setZipCode("");
        
        assertEquals("", address.getCountry());
        assertEquals("", address.getCity());
        assertEquals("", address.getStreet());
        assertEquals("", address.getZipCode());
    }

    @Test
    public void testVeryLongStreet() {
        String longStreet = "Very Long Street Name ".repeat(10);
        address.setStreet(longStreet);
        assertEquals(longStreet, address.getStreet());
    }

    @Test
    public void testSpecialCharactersInCity() {
        address.setCity("Saint-Étienne");
        assertEquals("Saint-Étienne", address.getCity());
    }

    @Test
    public void testNumericZipCode() {
        address.setZipCode("12345");
        assertEquals("12345", address.getZipCode());
    }

    @Test
    public void testAlphanumericZipCode() {
        address.setZipCode("AB12 3CD");
        assertEquals("AB12 3CD", address.getZipCode());
    }
}
