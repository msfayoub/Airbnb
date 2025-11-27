package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AmenityExtraTest {

    private Amenity amenity;

    @BeforeEach
    public void setUp() {
        amenity = new Amenity();
    }

    @Test
    public void testSetId() {
        amenity.setId(1L);
        assertEquals(1L, amenity.getId());
    }

    @Test
    public void testSetName() {
        amenity.setName("WiFi");
        assertEquals("WiFi", amenity.getName());
    }

    @Test
    public void testEmptyName() {
        amenity.setName("");
        assertEquals("", amenity.getName());
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
    public void testMultipleNameChanges() {
        amenity.setName("WiFi");
        amenity.setName("Pool");
        amenity.setName("Gym");
        assertEquals("Gym", amenity.getName());
    }

    @Test
    public void testConstructorWithRoom() {
        Room room = new Room();
        Amenity newAmenity = new Amenity(room, "Heating");
        assertEquals("Heating", newAmenity.getName());
    }
}
