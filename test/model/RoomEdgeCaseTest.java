package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomEdgeCaseTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room();
    }

    @Test
    public void testSetId() {
        room.setId(1);
        assertEquals(1, room.getId());
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
    public void testSetNbSingleBed() {
        room.setNbSingleBed(2);
        assertEquals(2, room.getNbSingleBed());
    }

    @Test
    public void testSetNbDoubleBed() {
        room.setNbDoubleBed(1);
        assertEquals(1, room.getNbDoubleBed());
    }

    @Test
    public void testZeroBeds() {
        room.setNbSingleBed(0);
        room.setNbDoubleBed(0);
        assertEquals(0, room.getNbSingleBed());
        assertEquals(0, room.getNbDoubleBed());
    }

    @Test
    public void testMultipleBeds() {
        room.setNbSingleBed(5);
        room.setNbDoubleBed(3);
        assertEquals(5, room.getNbSingleBed());
        assertEquals(3, room.getNbDoubleBed());
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
    public void testNegativeBeds() {
        room.setNbSingleBed(-1);
        room.setNbDoubleBed(-1);
        assertEquals(-1, room.getNbSingleBed());
        assertEquals(-1, room.getNbDoubleBed());
    }
}
