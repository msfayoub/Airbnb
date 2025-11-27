package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccommodationExtraTest {

    private Accommodation accommodation;

    @BeforeEach
    public void setUp() {
        accommodation = new Accommodation();
    }

    @Test
    public void testGetId() {
        assertEquals(0, accommodation.getId());
    }

    @Test
    public void testZeroCapacity() {
        accommodation.setCapacity(0);
        assertEquals(0, accommodation.getCapacity());
    }

    @Test
    public void testZeroRooms() {
        accommodation.setNumberOfRooms(0);
        assertEquals(0, accommodation.getNumberOfRooms());
    }

    @Test
    public void testVeryLargeCapacity() {
        accommodation.setCapacity(999);
        assertEquals(999, accommodation.getCapacity());
    }

    @Test
    public void testEmptyDescription() {
        accommodation.setDescription("");
        assertEquals("", accommodation.getDescription());
    }

    @Test
    public void testNullUser() {
        accommodation.setUser(null);
        assertNull(accommodation.getUser());
    }

    @Test
    public void testNullAddress() {
        accommodation.setAddress(null);
        assertNull(accommodation.getAddress());
    }

    @Test
    public void testNullHouseRules() {
        accommodation.setHouseRules(null);
        assertNull(accommodation.getHouseRules());
    }

    @Test
    public void testMultipleTypes() {
        accommodation.setType("Apartment");
        assertEquals("Apartment", accommodation.getType());
        
        accommodation.setType("House");
        assertEquals("House", accommodation.getType());
    }

    @Test
    public void testAddPicture() {
        Picture pic = new Picture("test.jpg");
        accommodation.addPicture(pic);
        assertEquals(1, accommodation.getPictures().size());
    }
}
