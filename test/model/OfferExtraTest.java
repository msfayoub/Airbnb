package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;

public class OfferExtraTest {

    private Offer offer;

    @BeforeEach
    public void setUp() {
        offer = new Offer();
    }

    @Test
    public void testSetId() {
        offer.setId(1);
        assertEquals(1, offer.getId());
    }

    @Test
    public void testZeroPrice() {
        offer.setPricePerNight(0.0);
        assertEquals(0.0, offer.getPricePerNight());
    }

    @Test
    public void testVeryHighPrice() {
        offer.setPricePerNight(999999.99);
        assertEquals(999999.99, offer.getPricePerNight());
    }

    @Test
    public void testSetNullAccommodation() {
        offer.setAccommodation(null);
        assertNull(offer.getAccommodation());
    }

    @Test
    public void testSetNullUser() {
        offer.setUser(null);
        assertNull(offer.getUser());
    }

    @Test
    public void testSetStartAvailability() {
        Calendar cal = Calendar.getInstance();
        offer.setStartAvailability(cal);
        assertEquals(cal, offer.getStartAvailability());
    }

    @Test
    public void testSetEndAvailability() {
        Calendar cal = Calendar.getInstance();
        offer.setEndAvailability(cal);
        assertEquals(cal, offer.getEndAvailability());
    }

    @Test
    public void testSetCleaningFee() {
        offer.setCleaningFee(25.50);
        assertEquals(25.50, offer.getCleaningFee());
    }

    @Test
    public void testZeroCleaningFee() {
        offer.setCleaningFee(0.0);
        assertEquals(0.0, offer.getCleaningFee());
    }

    @Test
    public void testUpdateMethod() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        offer.update(start, end, 99.99, 15.0);
        
        assertEquals(start, offer.getStartAvailability());
        assertEquals(end, offer.getEndAvailability());
        assertEquals(99.99, offer.getPricePerNight());
        assertEquals(15.0, offer.getCleaningFee());
    }
}
