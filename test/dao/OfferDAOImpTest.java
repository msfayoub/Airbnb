package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.Accommodation;
import model.Offer;
import model.User;

public class OfferDAOImpTest {

    private OfferDAOImp offerDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        offerDAO = new OfferDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = OfferDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(offerDAO, mockEm);
    }

    @Test
    public void testCreateOffer() {
        // Given
        User user = new User();
        Accommodation accommodation = new Accommodation();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 5);
        double price = 100.0;
        double fee = 20.0;

        // When
        offerDAO.createOffer(user, accommodation, start, end, price, fee);

        // Then
        ArgumentCaptor<Offer> offerCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(mockEm).persist(offerCaptor.capture());
        
        Offer persistedOffer = offerCaptor.getValue();
        assertEquals(user, persistedOffer.getUser());
        assertEquals(accommodation, persistedOffer.getAccommodation());
        assertEquals(price, persistedOffer.getPricePerNight());
    }

    @Test
    public void testUpdateOffer() {
        // Given
        Offer offer = new Offer();
        offer.setPricePerNight(120.0);

        // When
        offerDAO.updateOffer(offer);

        // Then
        verify(mockEm).merge(offer);
    }

    @Test
    public void testGetOffer() {
        int offerId = 1;
        Offer offer = new Offer();
        when(mockEm.find(Offer.class, offerId)).thenReturn(offer);

        Offer result = offerDAO.getOffer(offerId);

        assertNotNull(result);
        verify(mockEm).find(Offer.class, offerId);
    }

    @Test
    public void testDeleteOffer() {
        Offer offer = new Offer();
        try {
            Field idField = Offer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(offer, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mockEm.find(Offer.class, 1)).thenReturn(offer);

        offerDAO.deleteOffer(offer);

        verify(mockEm).find(Offer.class, 1);
        verify(mockEm).remove(offer);
    }

    @Test
    public void testGetAllOffer() {
        TypedQuery<Offer> mockQuery = mock(TypedQuery.class);
        when(mockEm.createQuery("SELECT o FROM Offer o", Offer.class)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Arrays.asList(new Offer(), new Offer()));

        offerDAO.getAllOffer();

        verify(mockEm).createQuery("SELECT o FROM Offer o", Offer.class);
        verify(mockQuery).getResultList();
    }

    @Test
    public void testGetUserOffer() {
        User user = new User();
        TypedQuery<Offer> mockQuery = mock(TypedQuery.class);
        when(mockEm.createQuery("SELECT o FROM Offer o WHERE o.user = ?1", Offer.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter(1, user)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Arrays.asList(new Offer()));

        offerDAO.getUserOffer(user);

        verify(mockEm).createQuery("SELECT o FROM Offer o WHERE o.user = ?1", Offer.class);
        verify(mockQuery).setParameter(1, user);
        verify(mockQuery).getResultList();
    }

    @Test
    public void testGetLastOffer() {
        TypedQuery<Offer> mockQuery = mock(TypedQuery.class);
        when(mockEm.createQuery("SELECT o FROM Offer o ORDER BY o.id DESC", Offer.class)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Arrays.asList(new Offer()));

        offerDAO.getLastOffer();

        verify(mockEm).createQuery("SELECT o FROM Offer o ORDER BY o.id DESC", Offer.class);
        verify(mockQuery).getResultList();
    }
}