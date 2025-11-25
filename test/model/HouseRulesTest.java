package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HouseRulesTest {

    private HouseRules houseRules;
    private LocalTime arrivalTime;
    private LocalTime departureTime;

    @BeforeEach
    public void setUp() {
        arrivalTime = LocalTime.of(14, 0);
        departureTime = LocalTime.of(11, 0);
        houseRules = new HouseRules(arrivalTime, departureTime, true, false, false);
    }

    @Test
    public void testHouseRulesCreation() {
        assertNotNull(houseRules);
        assertEquals(arrivalTime, houseRules.getArrivalHour());
        assertEquals(departureTime, houseRules.getDepartureHour());
        assertTrue(houseRules.isPetAllowed());
        assertFalse(houseRules.isPartyAllowed());
        assertFalse(houseRules.isSmokeAllowed());
    }

    @Test
    public void testUpdate() {
        LocalTime newArrival = LocalTime.of(15, 0);
        LocalTime newDeparture = LocalTime.of(10, 0);
        
        houseRules.update(newArrival, newDeparture, false, true, true);
        
        assertEquals(newArrival, houseRules.getArrivalHour());
        assertEquals(newDeparture, houseRules.getDepartureHour());
        assertFalse(houseRules.isPetAllowed());
        assertTrue(houseRules.isPartyAllowed());
        assertTrue(houseRules.isSmokeAllowed());
    }

    @Test
    public void testSetters() {
        houseRules.setArrivalHour(LocalTime.of(16, 0));
        houseRules.setDepartureHour(LocalTime.of(9, 0));
        houseRules.setPetAllowed(false);
        houseRules.setPartyAllowed(true);
        houseRules.setSmokeAllowed(true);

        assertEquals(LocalTime.of(16, 0), houseRules.getArrivalHour());
        assertEquals(LocalTime.of(9, 0), houseRules.getDepartureHour());
        assertFalse(houseRules.isPetAllowed());
        assertTrue(houseRules.isPartyAllowed());
        assertTrue(houseRules.isSmokeAllowed());
    }

    @Test
    public void testToString() {
        String result = houseRules.toString();
        assertNotNull(result);
        assertTrue(result.contains("14:00"));
        assertTrue(result.contains("11:00"));
    }

    @Test
    public void testNoArgsConstructor() {
        HouseRules hr = new HouseRules();
        assertNotNull(hr);
    }
}
