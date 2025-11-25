package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AmenityTest {

    private Amenity amenity;
    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room();
        amenity = new Amenity(room, "wifi");
    }

    @Test
    public void testAmenityCreation() {
        assertNotNull(amenity);
        assertEquals("wifi", amenity.getName());
    }

    @Test
    public void testSetName() {
        amenity.setName("tv");
        assertEquals("tv", amenity.getName());
    }

    @Test
    public void testNoArgsConstructor() {
        Amenity a = new Amenity();
        assertNotNull(a);
    }

    @Test
    public void testDifferentAmenities() {
        amenity.setName("tv");
        assertEquals("tv", amenity.getName());
        
        amenity.setName("air conditioning");
        assertEquals("air conditioning", amenity.getName());
        
        amenity.setName("kitchen");
        assertEquals("kitchen", amenity.getName());
    }

    @Test
    public void testRoomAssociation() {
        Room newRoom = new Room();
        amenity.setRoom(newRoom);
        assertEquals(newRoom, amenity.getRoom());
    }

    @Test
    public void testMultipleAmenitiesForSameRoom() {
        Amenity amenity1 = new Amenity(room, "wifi");
        Amenity amenity2 = new Amenity(room, "tv");
        Amenity amenity3 = new Amenity(room, "heating");
        
        assertEquals("wifi", amenity1.getName());
        assertEquals("tv", amenity2.getName());
        assertEquals("heating", amenity3.getName());
        assertEquals(room, amenity1.getRoom());
        assertEquals(room, amenity2.getRoom());
        assertEquals(room, amenity3.getRoom());
    }
}
