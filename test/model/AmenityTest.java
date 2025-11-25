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
}
