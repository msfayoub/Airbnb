package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingExtraTest {

    private Booking booking;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
    }

    @Test
    public void testSetId() {
        booking.setId(1);
        assertEquals(1, booking.getId());
    }

    @Test
    public void testSetBookingDate() {
        Date date = new Date();
        booking.setBookingDate(date);
        assertEquals(date, booking.getBookingDate());
    }

    @Test
    public void testZeroPriceBooking() {
        booking.setTotalPrice(0.0);
        assertEquals(0.0, booking.getTotalPrice());
    }

    @Test
    public void testZeroPersonsBooking() {
        booking.setNbPerson(0);
        assertEquals(0, booking.getNbPerson());
    }

    @Test
    public void testVeryLargeTotalPrice() {
        booking.setTotalPrice(999999.99);
        assertEquals(999999.99, booking.getTotalPrice());
    }

    @Test
    public void testVeryLargeNbPerson() {
        booking.setNbPerson(100);
        assertEquals(100, booking.getNbPerson());
    }

    @Test
    public void testSameDayBooking() {
        Date date = new Date();
        booking.setArrivalDate(date);
        booking.setDepartureDate(date);
        assertEquals(booking.getArrivalDate(), booking.getDepartureDate());
    }

    @Test
    public void testSetNullUser() {
        booking.setUser(null);
        assertNull(booking.getUser());
    }

    @Test
    public void testSetNullOffer() {
        booking.setOffer(null);
        assertNull(booking.getOffer());
    }

    @Test
    public void testSetNullTransaction() {
        booking.setTransaction(null);
        assertNull(booking.getTransaction());
    }
}
