package function;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashTest {

    @Test
    public void testSha256Hash() {
        String input = "testPassword123";
        String hash = Hash.sha256(input);
        
        assertNotNull("Hash should not be null", hash);
        assertEquals("Hash should be 64 characters (SHA-256)", 64, hash.length());
        assertTrue("Hash should contain only hexadecimal characters", hash.matches("[0-9a-f]+"));
    }

    @Test
    public void testSha256Consistency() {
        String input = "sameInput";
        String hash1 = Hash.sha256(input);
        String hash2 = Hash.sha256(input);
        
        assertEquals("Same input should produce same hash", hash1, hash2);
    }

    @Test
    public void testSha256DifferentInputs() {
        String input1 = "password1";
        String input2 = "password2";
        String hash1 = Hash.sha256(input1);
        String hash2 = Hash.sha256(input2);
        
        assertNotEquals("Different inputs should produce different hashes", hash1, hash2);
    }

    @Test
    public void testSha256EmptyString() {
        String hash = Hash.sha256("");
        assertNotNull("Empty string hash should not be null", hash);
        assertEquals("Empty string hash should be 64 characters", 64, hash.length());
    }
}
