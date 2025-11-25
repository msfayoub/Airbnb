package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingTest {

    private Booking booking;
    private User user;
    private Offer offer;
    private Transaction transaction;
    private Date arrivalDate;
    private Date departureDate;

    @BeforeEach
    public void setUp() {
        user = new User();
        offer = new Offer();
        transaction = new Transaction();
        arrivalDate = new Date();
        departureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day later
        booking = new Booking(user, offer, transaction, arrivalDate, departureDate, 2, 200.0);
    }

    @Test
    public void testBookingCreation() {
        assertNotNull(booking.getBookingDate());
        assertEquals(user, booking.getUser());
        assertEquals(offer, booking.getOffer());
        assertEquals(transaction, booking.getTransaction());
        assertEquals(arrivalDate, booking.getArrivalDate());
        assertEquals(departureDate, booking.getDepartureDate());
        assertEquals(2, booking.getNbPerson());
        assertEquals(200.0, booking.getTotalPrice());
    }
}
