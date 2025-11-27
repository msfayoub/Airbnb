package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PictureEdgeCaseTest {

    private Picture picture;

    @BeforeEach
    public void setUp() {
        picture = new Picture();
    }

    @Test
    public void testSetId() {
        picture.setId(1);
        assertEquals(1, picture.getId());
    }

    @Test
    public void testSetName() {
        picture.setName("image.jpg");
        assertEquals("image.jpg", picture.getName());
    }

    @Test
    public void testEmptyName() {
        picture.setName("");
        assertEquals("", picture.getName());
    }

    @Test
    public void testVeryLongFileName() {
        String longName = "very_long_image_file_name_with_many_characters_" + "x".repeat(100) + ".jpg";
        picture.setName(longName);
        assertEquals(longName, picture.getName());
    }

    @Test
    public void testDifferentFileExtensions() {
        picture.setName("photo.jpg");
        assertEquals("photo.jpg", picture.getName());
        
        picture.setName("photo.png");
        assertEquals("photo.png", picture.getName());
        
        picture.setName("photo.gif");
        assertEquals("photo.gif", picture.getName());
    }

    @Test
    public void testFileNameWithSpaces() {
        picture.setName("my photo.jpg");
        assertEquals("my photo.jpg", picture.getName());
    }

    @Test
    public void testFileNameWithSpecialCharacters() {
        picture.setName("photo-2024_01.jpg");
        assertEquals("photo-2024_01.jpg", picture.getName());
    }

    @Test
    public void testSetNullAccommodation() {
        picture.setAccommodation(null);
        assertNull(picture.getAccommodation());
    }

    @Test
    public void testMultipleNameChanges() {
        picture.setName("first.jpg");
        picture.setName("second.png");
        picture.setName("third.gif");
        assertEquals("third.gif", picture.getName());
    }
}
