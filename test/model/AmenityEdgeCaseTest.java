package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AmenityEdgeCaseTest {

    private Amenity amenity;

    @BeforeEach
    public void setUp() {
        amenity = new Amenity();
    }

    @Test
    public void testSetId() {
        amenity.setId(1);
        assertEquals(1, amenity.getId());
    }

    @Test
    public void testSetName() {
        amenity.setName("WiFi");
        assertEquals("WiFi", amenity.getName());
    }

    @Test
    public void testSetType() {
        amenity.setType("Technology");
        assertEquals("Technology", amenity.getType());
    }

    @Test
    public void testEmptyName() {
        amenity.setName("");
        assertEquals("", amenity.getName());
    }

    @Test
    public void testEmptyType() {
        amenity.setType("");
        assertEquals("", amenity.getType());
    }

    @Test
    public void testVeryLongName() {
        String longName = "Very Long Amenity Name ".repeat(10);
        amenity.setName(longName);
        assertEquals(longName, amenity.getName());
    }

    @Test
    public void testSpecialCharactersInName() {
        amenity.setName("Air Conditioning - 24/7");
        assertEquals("Air Conditioning - 24/7", amenity.getName());
    }

    @Test
    public void testMultipleTypes() {
        amenity.setType("Bedroom");
        assertEquals("Bedroom", amenity.getType());
        
        amenity.setType("Bathroom");
        assertEquals("Bathroom", amenity.getType());
        
        amenity.setType("Kitchen");
        assertEquals("Kitchen", amenity.getType());
    }

    @Test
    public void testSetNullRoom() {
        amenity.setRoom(null);
        assertNull(amenity.getRoom());
    }
}
