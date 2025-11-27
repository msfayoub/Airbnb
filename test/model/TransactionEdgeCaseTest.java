package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionEdgeCaseTest {

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void testSetId() {
        transaction.setId(1);
        assertEquals(1, transaction.getId());
    }

    @Test
    public void testSetAmount() {
        transaction.setAmount(100.0);
        assertEquals(100.0, transaction.getAmount());
    }

    @Test
    public void testZeroAmount() {
        transaction.setAmount(0.0);
        assertEquals(0.0, transaction.getAmount());
    }

    @Test
    public void testNegativeAmount() {
        transaction.setAmount(-50.0);
        assertEquals(-50.0, transaction.getAmount());
    }

    @Test
    public void testVeryLargeAmount() {
        transaction.setAmount(999999999.99);
        assertEquals(999999999.99, transaction.getAmount());
    }

    @Test
    public void testDecimalPrecision() {
        transaction.setAmount(12.34);
        assertEquals(12.34, transaction.getAmount());
        
        transaction.setAmount(99.99);
        assertEquals(99.99, transaction.getAmount());
    }

    @Test
    public void testMultipleAmountChanges() {
        transaction.setAmount(10.0);
        transaction.setAmount(20.0);
        transaction.setAmount(15.0);
        assertEquals(15.0, transaction.getAmount());
    }

    @Test
    public void testSetDate() {
        transaction.setDate(null);
        assertNull(transaction.getDate());
    }
}
