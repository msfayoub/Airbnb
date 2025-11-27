package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccommodationEdgeCaseTest {

    private Accommodation accommodation;

    @BeforeEach
    public void setUp() {
        accommodation = new Accommodation();
    }

    @Test
    public void testSetId() {
        accommodation.setId(1);
        assertEquals(1, accommodation.getId());
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
    public void testVeryLongName() {
        String longName = "A".repeat(200);
        accommodation.setName(longName);
        assertEquals(longName, accommodation.getName());
    }

    @Test
    public void testVeryLongDescription() {
        String longDesc = "Very long description. ".repeat(50);
        accommodation.setDescription(longDesc);
        assertEquals(longDesc, accommodation.getDescription());
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
    public void testUpdateWithLargeValues() {
        accommodation.update("Big House", "Mansion", 50, 25, "A very large property");
        assertEquals("Big House", accommodation.getName());
        assertEquals("Mansion", accommodation.getType());
        assertEquals(50, accommodation.getCapacity());
        assertEquals(25, accommodation.getNumberOfRooms());
    }

    @Test
    public void testMultipleTypes() {
        accommodation.setType("Apartment");
        assertEquals("Apartment", accommodation.getType());
        
        accommodation.setType("House");
        assertEquals("House", accommodation.getType());
        
        accommodation.setType("Villa");
        assertEquals("Villa", accommodation.getType());
    }
}
