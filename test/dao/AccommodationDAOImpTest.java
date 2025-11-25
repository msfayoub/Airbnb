package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.Accommodation;
import model.Address;
import model.HouseRules;
import model.User;

public class AccommodationDAOImpTest {

    private AccommodationDAOImp accommodationDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        accommodationDAO = new AccommodationDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = AccommodationDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(accommodationDAO, mockEm);
    }

    @Test
    public void testCreateAccommodation() {
        // Given
        User user = new User();
        Address address = new Address();
        HouseRules houseRules = new HouseRules();
        Accommodation accommodation = new Accommodation(user, "Test Hotel", address, houseRules, "Hotel", 4, 2, "A nice place");

        // When
        accommodationDAO.createAccommodation(accommodation);

        // Then
        ArgumentCaptor<Accommodation> accommodationCaptor = ArgumentCaptor.forClass(Accommodation.class);
        verify(mockEm).persist(accommodationCaptor.capture());
        assertEquals("Test Hotel", accommodationCaptor.getValue().getName());
    }

    @Test
    public void testGetAccommodation() {
        // Given
        int accommodationId = 1;
        Accommodation accommodation = new Accommodation();
        TypedQuery<Accommodation> mockQuery = mock(TypedQuery.class);
        
        when(mockEm.createQuery("SELECT a FROM Accommodation a WHERE a.id = ?1", Accommodation.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter(1, accommodationId)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(accommodation);

        // When
        Accommodation foundAccommodation = accommodationDAO.getAccommodation(accommodationId);

        // Then
        assertNotNull(foundAccommodation);
        verify(mockEm).createQuery("SELECT a FROM Accommodation a WHERE a.id = ?1", Accommodation.class);
    }

    @Test
    public void testDeleteAccommodation() {
        // Given
        int accommodationId = 1;
        Accommodation accommodation = new Accommodation();
        // The ID is set automatically by JPA, but we need to set it manually for the test
        try {
            Field idField = Accommodation.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(accommodation, accommodationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        TypedQuery<Accommodation> mockQuery = mock(TypedQuery.class);

        when(mockEm.createQuery("SELECT a FROM Accommodation a WHERE a.id = ?1", Accommodation.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter(1, accommodationId)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(accommodation);

        // When
        accommodationDAO.deleteAccommodation(accommodation);

        // Then
        verify(mockEm).remove(accommodation);
    }
}