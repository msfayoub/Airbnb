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

    @Test
    public void testGetTextFromDBEntryAllKeys() {
        assertEquals("Chambre", textTools.getTextFromDBEntry("bedroom"));
        assertEquals("Cuisine", textTools.getTextFromDBEntry("kitchen"));
        assertEquals("Salle de bain", textTools.getTextFromDBEntry("bathroom"));
        assertEquals("Horloge", textTools.getTextFromDBEntry("clock"));
    }

    @Test
    public void testGetTextFromDBEntryCaseSensitive() {
        assertNull("Uppercase should not match", textTools.getTextFromDBEntry("BEDROOM"));
        assertNull("Mixed case should not match", textTools.getTextFromDBEntry("Bedroom"));
    }

    @Test
    public void testGetTextFromBoolMultipleCalls() {
        assertEquals("Oui", textTools.getTextFromBool(true));
        assertEquals("Oui", textTools.getTextFromBool(true));
        assertEquals("Non", textTools.getTextFromBool(false));
        assertEquals("Non", textTools.getTextFromBool(false));
    }
}
