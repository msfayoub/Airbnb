package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OfferTest {

    private Offer offer;
    private User user;
    private Accommodation accommodation;
    private Calendar start;
    private Calendar end;

    @BeforeEach
    public void setUp() {
        user = new User();
        accommodation = new Accommodation();
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        end.add(Calendar.DATE, 7);
        offer = new Offer(user, accommodation, start, end, 150.0, 50.0);
    }

    @Test
    public void testUpdate() {
        Calendar newStart = Calendar.getInstance();
        newStart.add(Calendar.DATE, 2);
        Calendar newEnd = Calendar.getInstance();
        newEnd.add(Calendar.DATE, 9);

        offer.update(newStart, newEnd, 160.0, 55.0);

        assertEquals(newStart, offer.getStartAvailability());
        assertEquals(newEnd, offer.getEndAvailability());
        assertEquals(160.0, offer.getPricePerNight());
        assertEquals(55.0, offer.getCleaningFee());
    }
}