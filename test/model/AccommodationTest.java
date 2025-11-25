package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
