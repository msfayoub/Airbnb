package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.Calendar;

import javax.persistence.EntityManager;

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
}