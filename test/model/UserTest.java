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

    @Test
    public void testSetters() {
        user.setMailAddress("newemail@example.com");
        user.setHashedPassword("newpassword");
        user.setFirstname("NewFirstname");
        user.setName("NewName");
        user.setPhoneNumber("9999999999");
        user.setUserType("host");
        user.setBalance(200.0);

        assertEquals("newemail@example.com", user.getMailAddress());
        assertEquals("newpassword", user.getHashedPassword());
        assertEquals("NewFirstname", user.getFirstname());
        assertEquals("NewName", user.getName());
        assertEquals("9999999999", user.getPhoneNumber());
        assertEquals("host", user.getUserType());
        assertEquals(200.0, user.getBalance());
    }

    @Test
    public void testDefaultConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        // Note: date field is null when using default constructor
        // It's only initialized in the parameterized constructor
    }

    @Test
    public void testBalanceOperations() {
        user.setBalance(0.0);
        assertEquals(0.0, user.getBalance());
        
        user.setBalance(500.0);
        assertEquals(500.0, user.getBalance());
    }

    @Test
    public void testUserTypeChange() {
        assertEquals("customer", user.getUserType());
        user.setUserType("host");
        assertEquals("host", user.getUserType());
    }
}
