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

    @Test
    public void testSetters() {
        User newUser = new User();
        Offer newOffer = new Offer();
        Transaction newTransaction = new Transaction();
        Date newArrival = new Date(System.currentTimeMillis() + 172800000);
        Date newDeparture = new Date(System.currentTimeMillis() + 259200000);

        booking.setUser(newUser);
        booking.setOffer(newOffer);
        booking.setTransaction(newTransaction);
        booking.setArrivalDate(newArrival);
        booking.setDepartureDate(newDeparture);
        booking.setNbPerson(4);
        booking.setTotalPrice(400.0);

        assertEquals(newUser, booking.getUser());
        assertEquals(newOffer, booking.getOffer());
        assertEquals(newTransaction, booking.getTransaction());
        assertEquals(newArrival, booking.getArrivalDate());
        assertEquals(newDeparture, booking.getDepartureDate());
        assertEquals(4, booking.getNbPerson());
        assertEquals(400.0, booking.getTotalPrice());
    }
}
