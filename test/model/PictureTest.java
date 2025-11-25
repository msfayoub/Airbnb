package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PictureTest {

    private Picture picture;

    @BeforeEach
    public void setUp() {
        picture = new Picture("image.jpg");
    }

    @Test
    public void testPictureCreation() {
        assertNotNull(picture);
        assertEquals("image.jpg", picture.getUrl());
    }

    @Test
    public void testSetUrl() {
        picture.setUrl("newimage.png");
        assertEquals("newimage.png", picture.getUrl());
    }

    @Test
    public void testToString() {
        String result = picture.toString();
        assertNotNull(result);
        assertTrue(result.contains("image.jpg"));
        assertTrue(result.contains("Filename"));
    }

    @Test
    public void testNoArgsConstructor() {
        Picture p = new Picture();
        assertNotNull(p);
    }

    @Test
    public void testSetUrlWithDifferentExtensions() {
        picture.setUrl("photo.png");
        assertEquals("photo.png", picture.getUrl());
        
        picture.setUrl("image.gif");
        assertEquals("image.gif", picture.getUrl());
    }

    @Test
    public void testSetUrlWithPath() {
        picture.setUrl("/uploads/images/vacation.jpg");
        assertEquals("/uploads/images/vacation.jpg", picture.getUrl());
    }

    @Test
    public void testEmptyUrl() {
        picture.setUrl("");
        assertEquals("", picture.getUrl());
    }
}
