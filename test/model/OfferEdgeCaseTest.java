package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OfferEdgeCaseTest {

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
    public void testNegativePrice() {
        offer.setPricePerNight(-10.0);
        assertEquals(-10.0, offer.getPricePerNight());
    }

    @Test
    public void testSetNullAccommodation() {
        offer.setAccommodation(null);
        assertNull(offer.getAccommodation());
    }

    @Test
    public void testSetNullStartDate() {
        offer.setStartDate(null);
        assertNull(offer.getStartDate());
    }

    @Test
    public void testSetNullEndDate() {
        offer.setEndDate(null);
        assertNull(offer.getEndDate());
    }

    @Test
    public void testPriceDecimalPrecision() {
        offer.setPricePerNight(99.99);
        assertEquals(99.99, offer.getPricePerNight());
        
        offer.setPricePerNight(12.34);
        assertEquals(12.34, offer.getPricePerNight());
    }

    @Test
    public void testMultiplePriceChanges() {
        offer.setPricePerNight(100.0);
        offer.setPricePerNight(200.0);
        offer.setPricePerNight(150.0);
        assertEquals(150.0, offer.getPricePerNight());
    }
}
