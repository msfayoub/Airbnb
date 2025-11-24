package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccommodationTest {

    @Test
    public void testAccommodationCreation() {
        User user = new User("owner@test.com", "hash", "Owner", "Name", "123", "host", 0.0);
        Address address = new Address("123 St", null, "12345", "City", "Country");
        HouseRules rules = new HouseRules();
        
        Accommodation acc = new Accommodation(user, "Test House", address, rules, "house", 4, 2, "Nice place");
        
        assertEquals("Test House", acc.getName());
        assertEquals("house", acc.getType());
        assertEquals(4, acc.getCapacity());
        assertEquals(2, acc.getNumberOfRooms());
        assertEquals("Nice place", acc.getDescription());
        assertEquals(user, acc.getUser());
    }

    @Test
    public void testAccommodationUpdate() {
        User user = new User("owner@test.com", "hash", "Owner", "Name", "123", "host", 0.0);
        Address address = new Address("123 St", null, "12345", "City", "Country");
        HouseRules rules = new HouseRules();
        Accommodation acc = new Accommodation(user, "Test House", address, rules, "house", 4, 2, "Nice place");
        
        acc.update("Updated House", "apartment", 6, 3, "Updated description");
        
        assertEquals("Updated House", acc.getName());
        assertEquals("apartment", acc.getType());
        assertEquals(6, acc.getCapacity());
        assertEquals(3, acc.getNumberOfRooms());
        assertEquals("Updated description", acc.getDescription());
    }
}
