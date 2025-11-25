package dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Address;

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
    public void testGetAddress() {
        int addressId = 1;
        Address address = new Address();
        when(mockEm.find(Address.class, addressId)).thenReturn(address);

        Address foundAddress = addressDAO.getAddress(addressId);

        assertNotNull(foundAddress);
        verify(mockEm).find(Address.class, addressId);
    }
}
