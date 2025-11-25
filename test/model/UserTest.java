package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("test@example.com", "password123", "John", "Doe", "1234567890", "customer", 100.0);
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user.getDate());
        assertEquals("test@example.com", user.getMailAddress());
        assertEquals("password123", user.getHashedPassword());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getName());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals("customer", user.getUserType());
        assertEquals(100.0, user.getBalance());
    }

    @Test
    public void testUpdate() {
        user.update("Jane", "Smith", "0987654321");
        assertEquals("Jane", user.getFirstname());
        assertEquals("Smith", user.getName());
        assertEquals("0987654321", user.getPhoneNumber());
    }
}
