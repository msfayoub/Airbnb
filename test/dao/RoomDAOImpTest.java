package dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Accommodation;
import model.Offer;
import model.Room;

public class RoomDAOImpTest {

    private RoomDAOImp roomDAO;
    private EntityManager mockEm;

    @BeforeEach
    public void setUp() throws Exception {
        roomDAO = new RoomDAOImp();
        mockEm = mock(EntityManager.class);

        Field emField = RoomDAOImp.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(roomDAO, mockEm);
    }

    @Test
    public void testGetRoom() {
        long roomId = 1L;
        Room room = new Room();
        when(mockEm.find(Room.class, roomId)).thenReturn(room);

        Room foundRoom = roomDAO.getRoom(roomId);

        assertNotNull(foundRoom);
        verify(mockEm).find(Room.class, roomId);
    }
}
