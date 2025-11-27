package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HouseRulesEdgeCaseTest {

    private HouseRules houseRules;

    @BeforeEach
    public void setUp() {
        houseRules = new HouseRules();
    }

    @Test
    public void testSetId() {
        houseRules.setId(1);
        assertEquals(1, houseRules.getId());
    }

    @Test
    public void testSetSmokingAllowed() {
        houseRules.setSmokingAllowed(true);
        assertTrue(houseRules.isSmokingAllowed());
        
        houseRules.setSmokingAllowed(false);
        assertFalse(houseRules.isSmokingAllowed());
    }

    @Test
    public void testSetPetAllowed() {
        houseRules.setPetAllowed(true);
        assertTrue(houseRules.isPetAllowed());
        
        houseRules.setPetAllowed(false);
        assertFalse(houseRules.isPetAllowed());
    }

    @Test
    public void testSetEventsAllowed() {
        houseRules.setEventsAllowed(true);
        assertTrue(houseRules.isEventsAllowed());
        
        houseRules.setEventsAllowed(false);
        assertFalse(houseRules.isEventsAllowed());
    }

    @Test
    public void testAllPermissionsTrue() {
        houseRules.setSmokingAllowed(true);
        houseRules.setPetAllowed(true);
        houseRules.setEventsAllowed(true);
        
        assertTrue(houseRules.isSmokingAllowed());
        assertTrue(houseRules.isPetAllowed());
        assertTrue(houseRules.isEventsAllowed());
    }

    @Test
    public void testAllPermissionsFalse() {
        houseRules.setSmokingAllowed(false);
        houseRules.setPetAllowed(false);
        houseRules.setEventsAllowed(false);
        
        assertFalse(houseRules.isSmokingAllowed());
        assertFalse(houseRules.isPetAllowed());
        assertFalse(houseRules.isEventsAllowed());
    }

    @Test
    public void testMixedPermissions() {
        houseRules.setSmokingAllowed(true);
        houseRules.setPetAllowed(false);
        houseRules.setEventsAllowed(true);
        
        assertTrue(houseRules.isSmokingAllowed());
        assertFalse(houseRules.isPetAllowed());
        assertTrue(houseRules.isEventsAllowed());
    }

    @Test
    public void testMultipleChanges() {
        houseRules.setSmokingAllowed(true);
        houseRules.setSmokingAllowed(false);
        houseRules.setSmokingAllowed(true);
        
        assertTrue(houseRules.isSmokingAllowed());
    }
}
