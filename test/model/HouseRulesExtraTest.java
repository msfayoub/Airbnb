package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

public class HouseRulesExtraTest {

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
    public void testSetSmokeAllowed() {
        houseRules.setSmokeAllowed(true);
        assertTrue(houseRules.isSmokeAllowed());
        
        houseRules.setSmokeAllowed(false);
        assertFalse(houseRules.isSmokeAllowed());
    }

    @Test
    public void testSetPetAllowed() {
        houseRules.setPetAllowed(true);
        assertTrue(houseRules.isPetAllowed());
        
        houseRules.setPetAllowed(false);
        assertFalse(houseRules.isPetAllowed());
    }

    @Test
    public void testSetPartyAllowed() {
        houseRules.setPartyAllowed(true);
        assertTrue(houseRules.isPartyAllowed());
        
        houseRules.setPartyAllowed(false);
        assertFalse(houseRules.isPartyAllowed());
    }

    @Test
    public void testAllPermissionsTrue() {
        houseRules.setSmokeAllowed(true);
        houseRules.setPetAllowed(true);
        houseRules.setPartyAllowed(true);
        
        assertTrue(houseRules.isSmokeAllowed());
        assertTrue(houseRules.isPetAllowed());
        assertTrue(houseRules.isPartyAllowed());
    }

    @Test
    public void testAllPermissionsFalse() {
        houseRules.setSmokeAllowed(false);
        houseRules.setPetAllowed(false);
        houseRules.setPartyAllowed(false);
        
        assertFalse(houseRules.isSmokeAllowed());
        assertFalse(houseRules.isPetAllowed());
        assertFalse(houseRules.isPartyAllowed());
    }

    @Test
    public void testSetArrivalHour() {
        LocalTime time = LocalTime.of(14, 0);
        houseRules.setArrivalHour(time);
        assertEquals(time, houseRules.getArrivalHour());
    }

    @Test
    public void testSetDepartureHour() {
        LocalTime time = LocalTime.of(11, 0);
        houseRules.setDepartureHour(time);
        assertEquals(time, houseRules.getDepartureHour());
    }

    @Test
    public void testUpdateMethod() {
        LocalTime arrival = LocalTime.of(15, 0);
        LocalTime departure = LocalTime.of(10, 0);
        houseRules.update(arrival, departure, true, false, true);
        
        assertEquals(arrival, houseRules.getArrivalHour());
        assertEquals(departure, houseRules.getDepartureHour());
        assertTrue(houseRules.isPetAllowed());
        assertFalse(houseRules.isPartyAllowed());
        assertTrue(houseRules.isSmokeAllowed());
    }

    @Test
    public void testToString() {
        LocalTime arrival = LocalTime.of(14, 0);
        LocalTime departure = LocalTime.of(11, 0);
        houseRules.setArrivalHour(arrival);
        houseRules.setDepartureHour(departure);
        houseRules.setPetAllowed(true);
        assertNotNull(houseRules.toString());
    }
}
