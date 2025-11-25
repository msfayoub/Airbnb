package dao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import model.Address;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressDAOImpTest {

    private AddressDAOImp addressDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        addressDAO = new AddressDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = AddressDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(addressDAO, mockEm);
    }

    @Test
    public void testCreateAddress() {
        Address address = new Address("123 Main St", "Apt 1", "12345", "City", "Country");
        
        addressDAO.createAddress(address);
        
        verify(mockEm).persist(address);
    }

    @Test
    public void testCreateAddressWithParameters() {
        Address createdAddress = addressDAO.createAddress("123 Main St", "Apt 1", "12345", "City", "Country");
        
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(mockEm).persist(addressCaptor.capture());
        
        Address persistedAddress = addressCaptor.getValue();
        assertEquals("123 Main St", persistedAddress.getStreetAndNumber());
        assertEquals("City", persistedAddress.getCity());
    }

    @Test
    public void testUpdateAddress() {
        Address address = new Address();
        
        addressDAO.updateAddress(address);
        
        verify(mockEm).merge(address);
    }
}
