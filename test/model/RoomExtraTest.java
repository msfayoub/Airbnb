package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomExtraTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room();
    }

    @Test
    public void testSetId() {
        room.setId(1L);
        assertEquals(1L, room.getId());
    }

    @Test
    public void testSetType() {
        room.setType("Bedroom");
        assertEquals("Bedroom", room.getType());
        
        room.setType("Bathroom");
        assertEquals("Bathroom", room.getType());
        
        room.setType("Kitchen");
        assertEquals("Kitchen", room.getType());
    }

    @Test
    public void testSetNullAccommodation() {
        room.setAccommodation(null);
        assertNull(room.getAccommodation());
    }

    @Test
    public void testEmptyType() {
        room.setType("");
        assertEquals("", room.getType());
    }

    @Test
    public void testConstructorWithAccommodationAndType() {
        Accommodation acc = new Accommodation();
        Room newRoom = new Room(acc, "Bedroom");
        assertEquals("Bedroom", newRoom.getType());
        assertEquals(acc, newRoom.getAccommodation());
    }

    @Test
    public void testSetAccommodation() {
        Accommodation acc = new Accommodation();
        room.setAccommodation(acc);
        assertEquals(acc, room.getAccommodation());
    }

    @Test
    public void testMultipleTypeChanges() {
        room.setType("Kitchen");
        room.setType("Bathroom");
        room.setType("Bedroom");
        assertEquals("Bedroom", room.getType());
    }
}
