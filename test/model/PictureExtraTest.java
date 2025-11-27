package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PictureExtraTest {

    private Picture picture;

    @BeforeEach
    public void setUp() {
        picture = new Picture();
    }

    @Test
    public void testSetUrl() {
        picture.setUrl("image.jpg");
        assertEquals("image.jpg", picture.getUrl());
    }

    @Test
    public void testEmptyUrl() {
        picture.setUrl("");
        assertEquals("", picture.getUrl());
    }

    @Test
    public void testVeryLongFileName() {
        String longName = "very_long_image_file_name_with_many_characters_" + "x".repeat(100) + ".jpg";
        picture.setUrl(longName);
        assertEquals(longName, picture.getUrl());
    }

    @Test
    public void testDifferentFileExtensions() {
        picture.setUrl("photo.jpg");
        assertEquals("photo.jpg", picture.getUrl());
        
        picture.setUrl("photo.png");
        assertEquals("photo.png", picture.getUrl());
        
        picture.setUrl("photo.gif");
        assertEquals("photo.gif", picture.getUrl());
    }

    @Test
    public void testFileNameWithSpaces() {
        picture.setUrl("my photo.jpg");
        assertEquals("my photo.jpg", picture.getUrl());
    }

    @Test
    public void testFileNameWithSpecialCharacters() {
        picture.setUrl("photo-2024_01.jpg");
        assertEquals("photo-2024_01.jpg", picture.getUrl());
    }

    @Test
    public void testConstructorWithUrl() {
        Picture newPicture = new Picture("test.png");
        assertEquals("test.png", newPicture.getUrl());
    }

    @Test
    public void testToString() {
        picture.setUrl("image.jpg");
        assertNotNull(picture.toString());
        assertTrue(picture.toString().contains("image.jpg"));
    }
}
