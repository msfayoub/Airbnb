package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.Booking;
import model.Offer;
import model.Transaction;
import model.User;

public class BookingDAOImpTest {

    private BookingDAOImp bookingDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        bookingDAO = new BookingDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = BookingDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(bookingDAO, mockEm);
    }

    @Test
    public void testCreateBooking() {
        // Given
        User user = new User();
        Offer offer = new Offer();
        Transaction transaction = new Transaction();
        Date arrivalDate = new Date();
        Date departureDate = new Date();
        int nbPerson = 2;
        double totalPrice = 250.0;

        // When
        bookingDAO.createBooking(user, offer, transaction, arrivalDate, departureDate, nbPerson, totalPrice);

        // Then
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(mockEm).persist(bookingCaptor.capture());
        
        Booking persistedBooking = bookingCaptor.getValue();
        assertEquals(user, persistedBooking.getUser());
        assertEquals(offer, persistedBooking.getOffer());
        assertEquals(totalPrice, persistedBooking.getTotalPrice());
    }

    @Test
    public void testDeleteBooking() {
        // Given
        int bookingId = 1;
        Booking booking = new Booking();
        when(mockEm.find(Booking.class, bookingId)).thenReturn(booking);

        // When
        bookingDAO.deleteBooking(bookingId);

        // Then
        verify(mockEm).find(Booking.class, bookingId);
        verify(mockEm).remove(booking);
    }
}