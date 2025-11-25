package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccommodationTest {

    private Accommodation accommodation;
    private User user;
    private Address address;
    private HouseRules houseRules;

    @BeforeEach
    public void setUp() {
        user = new User();
        address = new Address();
        houseRules = new HouseRules();
        accommodation = new Accommodation(user, "Lovely Place", address, houseRules, "Apartment", 4, 2, "A lovely apartment.");
    }

    @Test
    public void testUpdate() {
        accommodation.update("Updated Place", "House", 5, 3, "An updated description.");
        assertEquals("Updated Place", accommodation.getName());
        assertEquals("House", accommodation.getType());
        assertEquals(5, accommodation.getCapacity());
        assertEquals(3, accommodation.getNumberOfRooms());
        assertEquals("An updated description.", accommodation.getDescription());
    }

    @Test
    public void testGetters() {
        assertEquals(user, accommodation.getUser());
        assertEquals(address, accommodation.getAddress());
        assertEquals(houseRules, accommodation.getHouseRules());
    }

    @Test
    public void testSetters() {
        User newUser = new User();
        Address newAddress = new Address();
        HouseRules newHouseRules = new HouseRules();

        accommodation.setUser(newUser);
        accommodation.setAddress(newAddress);
        accommodation.setHouseRules(newHouseRules);
        accommodation.setName("New Name");
        accommodation.setType("Apartment");
        accommodation.setCapacity(2);
        accommodation.setNumberOfRooms(1);
        accommodation.setDescription("New description");

        assertEquals(newUser, accommodation.getUser());
        assertEquals(newAddress, accommodation.getAddress());
        assertEquals(newHouseRules, accommodation.getHouseRules());
        assertEquals("New Name", accommodation.getName());
        assertEquals("Apartment", accommodation.getType());
        assertEquals(2, accommodation.getCapacity());
        assertEquals(1, accommodation.getNumberOfRooms());
        assertEquals("New description", accommodation.getDescription());
    }

    @Test
    public void testDefaultConstructor() {
        Accommodation emptyAccommodation = new Accommodation();
        assertNotNull(emptyAccommodation);
        // Note: fields are null when using default constructor
        assertEquals(0, emptyAccommodation.getCapacity());
    }

    @Test
    public void testUpdateWithDifferentValues() {
        accommodation.update("Beach House", "Villa", 10, 6, "Luxury beachfront villa");
        assertEquals("Beach House", accommodation.getName());
        assertEquals("Villa", accommodation.getType());
        assertEquals(10, accommodation.getCapacity());
        assertEquals(6, accommodation.getNumberOfRooms());
        assertEquals("Luxury beachfront villa", accommodation.getDescription());
    }

    @Test
    public void testAccommodationCreation() {
        assertEquals("Lovely Place", accommodation.getName());
        assertEquals("Apartment", accommodation.getType());
        assertEquals(4, accommodation.getCapacity());
        assertEquals(2, accommodation.getNumberOfRooms());
        assertEquals("A lovely apartment.", accommodation.getDescription());
    }
}
