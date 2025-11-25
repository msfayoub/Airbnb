package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomTest {

    private Room room;
    private Accommodation accommodation;

    @BeforeEach
    public void setUp() {
        accommodation = new Accommodation();
        room = new Room(accommodation, "Master Bedroom", "A large room with a king-size bed.", 2);
    }

    @Test
    public void testUpdate() {
        room.update("Guest Room", "A smaller room with a queen-size bed.", 2);
        assertEquals("Guest Room", room.getName());
        assertEquals("A smaller room with a queen-size bed.", room.getDescription());
        assertEquals(2, room.getCapacity());
    }
}