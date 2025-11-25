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
}
