package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserEdgeCaseTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testSetDateWithNull() {
        user.setDate(null);
        assertNull(user.getDate());
    }

    @Test
    public void testVeryLongEmail() {
        String longEmail = "verylongemailaddress@verylongdomainname.com";
        user.setMailAddress(longEmail);
        assertEquals(longEmail, user.getMailAddress());
    }

    @Test
    public void testSpecialCharactersInName() {
        user.setName("O'Brien");
        assertEquals("O'Brien", user.getName());
        
        user.setFirstname("François");
        assertEquals("François", user.getFirstname());
    }

    @Test
    public void testEmptyStrings() {
        user.setMailAddress("");
        user.setHashedPassword("");
        user.setFirstname("");
        user.setName("");
        user.setPhoneNumber("");
        user.setUserType("");
        
        assertEquals("", user.getMailAddress());
        assertEquals("", user.getHashedPassword());
        assertEquals("", user.getFirstname());
        assertEquals("", user.getName());
        assertEquals("", user.getPhoneNumber());
        assertEquals("", user.getUserType());
    }

    @Test
    public void testVeryHighBalance() {
        user.setBalance(1000000.0);
        assertEquals(1000000.0, user.getBalance());
    }

    @Test
    public void testNegativeBalance() {
        user.setBalance(-100.0);
        assertEquals(-100.0, user.getBalance());
    }

    @Test
    public void testUpdateWithEmptyStrings() {
        user.update("", "", "");
        assertEquals("", user.getFirstname());
        assertEquals("", user.getName());
        assertEquals("", user.getPhoneNumber());
    }

    @Test
    public void testMultipleUserTypes() {
        user.setUserType("admin");
        assertEquals("admin", user.getUserType());
        
        user.setUserType("host");
        assertEquals("host", user.getUserType());
        
        user.setUserType("customer");
        assertEquals("customer", user.getUserType());
    }

    @Test
    public void testUpdateMultipleTimes() {
        user.update("First", "Last", "111");
        user.update("Second", "Name", "222");
        user.update("Third", "Final", "333");
        
        assertEquals("Third", user.getFirstname());
        assertEquals("Final", user.getName());
        assertEquals("333", user.getPhoneNumber());
    }
}
