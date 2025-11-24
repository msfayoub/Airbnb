package function;

import org.junit.Test;
import static org.junit.Assert.*;

public class TextToolsTest {

    @Test
    public void testGetTextFromDBEntry() {
        assertEquals("Chambre", textTools.getTextFromDBEntry("bedroom"));
        assertEquals("Cuisine", textTools.getTextFromDBEntry("kitchen"));
        assertEquals("Salle de bain", textTools.getTextFromDBEntry("bathroom"));
        assertEquals("Lit simple", textTools.getTextFromDBEntry("singleBed"));
        assertEquals("Lit double", textTools.getTextFromDBEntry("doubleBed"));
    }

    @Test
    public void testGetTextFromDBEntryNotFound() {
        assertNull("Non-existent entry should return null", textTools.getTextFromDBEntry("nonExistent"));
    }

    @Test
    public void testGetTextFromBool() {
        assertEquals("Oui", textTools.getTextFromBool(true));
        assertEquals("Non", textTools.getTextFromBool(false));
    }
}
