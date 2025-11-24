package model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookingTest {

    @Test
    public void testBookingCreation() {
        User user = new User("user@test.com", "hash", "User", "Name", "123", "client", 100.0);
        Offer offer = new Offer();
        Transaction transaction = new Transaction();
        Date arrival = new Date();
        Date departure = new Date(System.currentTimeMillis() + 86400000); // +1 day
        
        Booking booking = new Booking(user, offer, transaction, arrival, departure, 2, 150.0);
        
        assertEquals(user, booking.getUser());
        assertEquals(offer, booking.getOffer());
        assertEquals(transaction, booking.getTransaction());
        assertEquals(2, booking.getNbPerson());
        assertEquals(150.0, booking.getTotalPrice(), 0.01);
        assertNotNull("Booking date should be set", booking.getBookingDate());
    }

    @Test
    public void testBookingSetters() {
        Booking booking = new Booking();
        booking.setNbPerson(4);
        booking.setTotalPrice(200.0);
        
        assertEquals(4, booking.getNbPerson());
        assertEquals(200.0, booking.getTotalPrice(), 0.01);
    }
}
