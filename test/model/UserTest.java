package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("test@test.com", "hashedPassword", "John", "Doe", "123456789", "client", 100.0);
        
        assertEquals("test@test.com", user.getMailAddress());
        assertEquals("hashedPassword", user.getHashedPassword());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getName());
        assertEquals("123456789", user.getPhoneNumber());
        assertEquals("client", user.getUserType());
        assertEquals(100.0, user.getBalance(), 0.01);
        assertNotNull("Date should be set", user.getDate());
    }

    @Test
    public void testUserUpdate() {
        User user = new User("test@test.com", "hash", "John", "Doe", "123", "client", 0.0);
        user.update("Jane", "Smith", "987654321");
        
        assertEquals("Jane", user.getFirstname());
        assertEquals("Smith", user.getName());
        assertEquals("987654321", user.getPhoneNumber());
    }

    @Test
    public void testUserSetters() {
        User user = new User();
        user.setMailAddress("new@test.com");
        user.setBalance(50.5);
        user.setUserType("admin");
        
        assertEquals("new@test.com", user.getMailAddress());
        assertEquals(50.5, user.getBalance(), 0.01);
        assertEquals("admin", user.getUserType());
    }
}
